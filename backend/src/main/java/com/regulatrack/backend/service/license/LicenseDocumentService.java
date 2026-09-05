package com.regulatrack.backend.service.license;

import com.regulatrack.backend.domain.license.License;
import com.regulatrack.backend.domain.license.LicenseDocument;
import com.regulatrack.backend.dto.license.LicenseDocumentResponse;
import com.regulatrack.backend.exception.ResourceNotFoundException;
import com.regulatrack.backend.repository.license.LicenseDocumentRepository;
import com.regulatrack.backend.repository.license.LicenseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class LicenseDocumentService {
    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel",
            "image/png",
            "image/jpeg",
            "text/plain"
    );

    private final LicenseRepository licenseRepository;
    private final LicenseDocumentRepository documentRepository;
    private final Path storageDirectory;

    public LicenseDocumentService(
            LicenseRepository licenseRepository,
            LicenseDocumentRepository documentRepository,
            @Value("${app.documents.storage-path:/app/storage/documents}") String storagePath
    ) {
        this.licenseRepository = licenseRepository;
        this.documentRepository = documentRepository;
        this.storageDirectory = Paths.get(storagePath).toAbsolutePath().normalize();
    }

    @Transactional(readOnly = true)
    public List<LicenseDocumentResponse> list(Long licenseId) {
        requireLicense(licenseId);
        return documentRepository.findByLicenseIdOrderByUploadedAtDesc(licenseId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public LicenseDocumentResponse upload(Long licenseId, MultipartFile file) {
        License license = requireLicense(licenseId);
        validate(file);

        try {
            Files.createDirectories(storageDirectory);
            String originalName = sanitizeOriginalName(file.getOriginalFilename());
            String storedName = UUID.randomUUID() + "-" + originalName;
            Path target = storageDirectory.resolve(storedName).normalize();
            if (!target.startsWith(storageDirectory)) {
                throw new IllegalArgumentException("Invalid file name.");
            }
            Files.copy(file.getInputStream(), target);

            LicenseDocument document = new LicenseDocument();
            document.setLicense(license);
            document.setOriginalName(originalName);
            document.setStoredName(storedName);
            document.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
            document.setSize(file.getSize());
            document.setStoragePath(target.toString());
            return toResponse(documentRepository.save(document));
        } catch (IOException ex) {
            throw new IllegalStateException("Could not store document.", ex);
        }
    }

    @Transactional(readOnly = true)
    public StoredDocument load(Long licenseId, Long documentId) {
        LicenseDocument document = documentRepository.findByIdAndLicenseId(documentId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found."));
        try {
            Path path = Paths.get(document.getStoragePath()).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Document file not found.");
            }
            return new StoredDocument(document, resource);
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Document file not found.");
        }
    }

    @Transactional
    public void delete(Long licenseId, Long documentId) {
        LicenseDocument document = documentRepository.findByIdAndLicenseId(documentId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found."));
        try {
            Files.deleteIfExists(Paths.get(document.getStoragePath()));
        } catch (IOException ex) {
            throw new IllegalStateException("Could not delete document file.", ex);
        }
        documentRepository.delete(document);
    }

    private License requireLicense(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("License not found."));
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Please select a file.");
        if (file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("File size must be 10 MB or less.");
        String type = file.getContentType();
        if (type == null || !ALLOWED_TYPES.contains(type.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Unsupported file type. Allowed: PDF, DOC, DOCX, XLS, XLSX, PNG, JPG and TXT.");
        }
    }

    private String sanitizeOriginalName(String value) {
        String name = value == null ? "document" : Paths.get(value).getFileName().toString();
        name = name.replaceAll("[^a-zA-Z0-9._() -]", "_").trim();
        if (name.isBlank() || name.length() > 180) name = "document";
        return name;
    }

    private LicenseDocumentResponse toResponse(LicenseDocument d) {
        return new LicenseDocumentResponse(d.getId(), d.getOriginalName(), d.getContentType(), d.getSize(), d.getUploadedAt());
    }

    public record StoredDocument(LicenseDocument document, Resource resource) {}
}
