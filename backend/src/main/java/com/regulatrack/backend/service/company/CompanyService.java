package com.regulatrack.backend.service.company;

import com.regulatrack.backend.domain.company.Company;
import com.regulatrack.backend.dto.company.CompanyResponse;
import com.regulatrack.backend.dto.company.CreateCompanyRequest;
import com.regulatrack.backend.dto.company.UpdateCompanyRequest;
import com.regulatrack.backend.exception.ResourceNotFoundException;
import com.regulatrack.backend.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CompanyResponse create(CreateCompanyRequest request) {
        Company company = Company.builder()
                .name(request.name())
                .documentNumber(request.documentNumber())
                .segment(request.segment())
                .country(request.country())
                .city(request.city())
                .build();

        Company savedCompany = companyRepository.save(company);

        return toResponse(savedCompany);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompanyResponse findById(Long id) {
        Company company = getCompanyById(id);
        return toResponse(company);
    }

    @Transactional
    public CompanyResponse update(Long id, UpdateCompanyRequest request) {
        Company company = getCompanyById(id);

        company.setName(request.name());
        company.setDocumentNumber(request.documentNumber());
        company.setSegment(request.segment());
        company.setCountry(request.country());
        company.setCity(request.city());

        Company updatedCompany = companyRepository.save(company);

        return toResponse(updatedCompany);
    }

    @Transactional
    public void delete(Long id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    private Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDocumentNumber(),
                company.getSegment(),
                company.getCountry(),
                company.getCity(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}