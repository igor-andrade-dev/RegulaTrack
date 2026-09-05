package com.regulatrack.backend.controller.license;

import com.regulatrack.backend.domain.license.LicenseDocument;
import com.regulatrack.backend.dto.license.LicenseDocumentResponse;
import com.regulatrack.backend.service.license.LicenseDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/licenses/{licenseId}/documents")
@RequiredArgsConstructor
public class LicenseDocumentController {
    private final LicenseDocumentService documentService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_LICENSE_DOCUMENT_VIEW')")
    public List<LicenseDocumentResponse> list(@PathVariable Long licenseId) {
        return documentService.list(licenseId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PERM_LICENSE_DOCUMENT_UPLOAD')")
    public ResponseEntity<LicenseDocumentResponse> upload(
            @PathVariable Long licenseId,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.status(201).body(documentService.upload(licenseId, file));
    }

    @GetMapping("/{documentId}/content")
    @PreAuthorize("hasAuthority('PERM_LICENSE_DOCUMENT_VIEW')")
    public ResponseEntity<Resource> content(
            @PathVariable Long licenseId,
            @PathVariable Long documentId
    ) {
        var stored = documentService.load(licenseId, documentId);
        LicenseDocument document = stored.document();
        MediaType mediaType;
        try { mediaType = MediaType.parseMediaType(document.getContentType()); }
        catch (Exception ex) { mediaType = MediaType.APPLICATION_OCTET_STREAM; }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(document.getOriginalName()).build().toString())
                .body(stored.resource());
    }

    @GetMapping("/{documentId}/download")
    @PreAuthorize("hasAuthority('PERM_LICENSE_DOCUMENT_VIEW')")
    public ResponseEntity<Resource> download(
            @PathVariable Long licenseId,
            @PathVariable Long documentId
    ) {
        var stored = documentService.load(licenseId, documentId);
        LicenseDocument document = stored.document();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(document.getOriginalName()).build().toString())
                .body(stored.resource());
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAuthority('PERM_LICENSE_DOCUMENT_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long licenseId,
            @PathVariable Long documentId
    ) {
        documentService.delete(licenseId, documentId);
        return ResponseEntity.noContent().build();
    }
}
