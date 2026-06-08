package com.regulatrack.backend.service;

import com.regulatrack.backend.domain.Branch;
import com.regulatrack.backend.domain.Company;
import com.regulatrack.backend.domain.license.License;
import com.regulatrack.backend.domain.license.LicenseStatus;
import com.regulatrack.backend.dto.license.CreateLicenseRequest;
import com.regulatrack.backend.dto.license.LicenseResponse;
import com.regulatrack.backend.dto.license.UpdateLicenseRequest;
import com.regulatrack.backend.repository.BranchRepository;
import com.regulatrack.backend.repository.CompanyRepository;
import com.regulatrack.backend.repository.LicenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    public LicenseService(
            LicenseRepository licenseRepository,
            CompanyRepository companyRepository,
            BranchRepository branchRepository
    ) {
        this.licenseRepository = licenseRepository;
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
    }

    public List<LicenseResponse> findAll() {
        return licenseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LicenseResponse findById(Long id) {
        License license = findLicenseById(id);
        return toResponse(license);
    }

    public List<LicenseResponse> findByCompanyId(Long companyId) {
        return licenseRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LicenseResponse> findByBranchId(Long branchId) {
        return licenseRepository.findByBranchId(branchId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LicenseResponse create(CreateLicenseRequest request) {
        Company company = findCompanyById(request.companyId());
        Branch branch = findBranchById(request.branchId());

        validateBranchBelongsToCompany(branch, company);

        License license = new License();
        license.setCompany(company);
        license.setBranch(branch);
        license.setName(request.name());
        license.setDescription(request.description());
        license.setCategory(request.category());
        license.setIssuer(request.issuer());
        license.setExpiresAt(request.expiresAt());
        license.setStatus(request.status() != null ? request.status() : LicenseStatus.PENDING);

        License savedLicense = licenseRepository.save(license);

        return toResponse(savedLicense);
    }

    public LicenseResponse update(Long id, UpdateLicenseRequest request) {
        License license = findLicenseById(id);

        Company company = findCompanyById(request.companyId());
        Branch branch = findBranchById(request.branchId());

        validateBranchBelongsToCompany(branch, company);

        license.setCompany(company);
        license.setBranch(branch);
        license.setName(request.name());
        license.setDescription(request.description());
        license.setCategory(request.category());
        license.setIssuer(request.issuer());
        license.setExpiresAt(request.expiresAt());
        license.setStatus(request.status() != null ? request.status() : license.getStatus());

        License updatedLicense = licenseRepository.save(license);

        return toResponse(updatedLicense);
    }

    public void delete(Long id) {
        License license = findLicenseById(id);
        licenseRepository.delete(license);
    }

    private License findLicenseById(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Licença não encontrada"));
    }

    private Company findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    private Branch findBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Filial não encontrada"));
    }

    private void validateBranchBelongsToCompany(Branch branch, Company company) {
        if (!branch.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("A filial informada não pertence à empresa informada");
        }
    }

    private LicenseResponse toResponse(License license) {
        return new LicenseResponse(
                license.getId(),
                license.getCompany().getId(),
                license.getBranch().getId(),
                license.getName(),
                license.getDescription(),
                license.getCategory(),
                license.getIssuer(),
                license.getExpiresAt(),
                license.getStatus(),
                license.getCreatedAt(),
                license.getUpdatedAt()
        );
    }
}