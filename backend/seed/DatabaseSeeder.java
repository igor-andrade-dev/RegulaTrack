package com.regulatrack.backend.seed;

import com.regulatrack.backend.domain.branch.Branch;
import com.regulatrack.backend.domain.company.Company;
import com.regulatrack.backend.domain.license.License;
import com.regulatrack.backend.repository.branch.BranchRepository;
import com.regulatrack.backend.repository.company.CompanyRepository;
import com.regulatrack.backend.repository.license.LicenseRepository;
import com.regulatrack.backend.service.license.LicenseStatusService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DatabaseSeeder {

    @Bean
    public ApplicationRunner seedDatabase(
            CompanyRepository companyRepository,
            BranchRepository branchRepository,
            LicenseRepository licenseRepository,
            LicenseStatusService licenseStatusService
    ) {
        return args -> {
            if (companyRepository.count() > 0 || branchRepository.count() > 0 || licenseRepository.count() > 0) {
                return;
            }

            Company company = new Company();
            company.setName("RegulaTech Industries");
            company.setDocumentNumber("12345678000199");
            company.setSegment("Tecnologia");
            company.setCountry("Brasil");
            company.setCity("Curitiba");

            Company savedCompany = companyRepository.save(company);

            Branch branch = new Branch();
            branch.setCompany(savedCompany);
            branch.setName("Unidade Curitiba");
            branch.setAddress("Rua Central, 100");
            branch.setCity("Curitiba");
            branch.setState("PR");
            branch.setCountry("Brasil");

            Branch savedBranch = branchRepository.save(branch);

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Ambiental Vencida",
                    "Licenca usada para demonstrar status automatico EXPIRED",
                    "Ambiental",
                    "ENV-2024-001",
                    "Orgao Ambiental",
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 12, 31),
                    "Status calculado automaticamente pelo vencimento"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Sanitaria Ativa",
                    "Licenca usada para demonstrar status automatico ACTIVE",
                    "Sanitaria",
                    "SAN-2027-001",
                    "Vigilancia Sanitaria",
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2027, 12, 31),
                    "Licenca valida por mais de 30 dias"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Proxima do Vencimento",
                    "Licenca usada para demonstrar status automatico EXPIRING_SOON",
                    "Operacional",
                    "OPR-2026-001",
                    "Orgao Regulador",
                    LocalDate.of(2026, 1, 1),
                    LocalDate.now().plusDays(15),
                    "Vencimento dentro dos proximos 30 dias"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Pendente",
                    "Licenca sem data de vencimento definida",
                    "Documental",
                    "DOC-0001",
                    "Orgao Regulador",
                    LocalDate.of(2026, 1, 1),
                    null,
                    "Sem expiresAt, o status deve ser PENDING"
            );
        };
    }

    private void createLicense(
            LicenseRepository licenseRepository,
            LicenseStatusService licenseStatusService,
            Company company,
            Branch branch,
            String name,
            String description,
            String category,
            String licenseNumber,
            String issuer,
            LocalDate issuedAt,
            LocalDate expiresAt,
            String notes
    ) {
        License license = new License();
        license.setCompany(company);
        license.setBranch(branch);
        license.setName(name);
        license.setDescription(description);
        license.setCategory(category);
        license.setLicenseNumber(licenseNumber);
        license.setIssuer(issuer);
        license.setIssuedAt(issuedAt);
        license.setExpiresAt(expiresAt);
        license.setResponsibleName("Igor Andrade");
        license.setResponsibleEmail("igor@email.com");
        license.setStatus(licenseStatusService.calculateStatus(expiresAt));
        license.setNotes(notes);

        licenseRepository.save(license);
    }
}