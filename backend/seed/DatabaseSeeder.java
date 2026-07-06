package com.regulatrack.backend.seed;

import com.regulatrack.backend.domain.branch.Branch;
import com.regulatrack.backend.domain.company.Company;
import com.regulatrack.backend.domain.license.License;
import com.regulatrack.backend.domain.user.Role;
import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.repository.branch.BranchRepository;
import com.regulatrack.backend.repository.company.CompanyRepository;
import com.regulatrack.backend.repository.license.LicenseRepository;
import com.regulatrack.backend.repository.user.UserRepository;
import com.regulatrack.backend.service.license.LicenseStatusService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DatabaseSeeder {

    @Bean
    public ApplicationRunner seedDatabase(
            CompanyRepository companyRepository,
            BranchRepository branchRepository,
            LicenseRepository licenseRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            LicenseStatusService licenseStatusService
    ) {
        return args -> {

            // =========================================
            // 🔐 USER SEED (LOGIN FUNCIONANDO)
            // =========================================
            if (userRepository.count() == 0) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(Role.USER);

                userRepository.save(user);
            }

            // =========================================
            // 🏢 COMPANY SEED
            // =========================================
            if (companyRepository.count() > 0) {
                return;
            }

            Company company = new Company();
            company.setName("RegulaTech Industries");
            company.setDocumentNumber("12345678000199");
            company.setSegment("Tecnologia");
            company.setCountry("Brasil");
            company.setCity("Curitiba");

            Company savedCompany = companyRepository.save(company);

            // =========================================
            // 🏢 BRANCH SEED
            // =========================================
            Branch branch = new Branch();
            branch.setCompany(savedCompany);
            branch.setName("Unidade Curitiba");
            branch.setAddress("Rua Central, 100");
            branch.setCity("Curitiba");
            branch.setState("PR");
            branch.setCountry("Brasil");

            Branch savedBranch = branchRepository.save(branch);

            // =========================================
            // 📄 LICENSES
            // =========================================
            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Ambiental Vencida",
                    "Licenca EXPIRED",
                    "Ambiental",
                    "ENV-2024-001",
                    "Orgao Ambiental",
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 12, 31),
                    "Status calculado automaticamente"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Sanitaria Ativa",
                    "Licenca ACTIVE",
                    "Sanitaria",
                    "SAN-2027-001",
                    "Vigilancia Sanitaria",
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2027, 12, 31),
                    "Valida"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Proxima do Vencimento",
                    "EXPIRING_SOON",
                    "Operacional",
                    "OPR-2026-001",
                    "Orgao Regulador",
                    LocalDate.of(2026, 1, 1),
                    LocalDate.now().plusDays(15),
                    "Vence em breve"
            );

            createLicense(
                    licenseRepository,
                    licenseStatusService,
                    savedCompany,
                    savedBranch,
                    "Licenca Pendente",
                    "PENDING",
                    "Documental",
                    "DOC-0001",
                    "Orgao Regulador",
                    LocalDate.of(2026, 1, 1),
                    null,
                    "Sem vencimento"
            );
        };
    }

    // =========================================
    // 🔧 HELPER
    // =========================================
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