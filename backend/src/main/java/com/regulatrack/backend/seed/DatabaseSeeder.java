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

            System.out.println("🔥 SEED EXECUTANDO...");

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

            Company company;
            if (companyRepository.count() == 0) {

                company = new Company();
                company.setName("RegulaTech Industries");
                company.setDocumentNumber("12345678000199");
                company.setSegment("Tecnologia");
                company.setCountry("Brasil");
                company.setCity("Curitiba");

                company = companyRepository.save(company);

            } else {
                company = companyRepository.findAll().get(0);
            }

            Branch branch;
            if (branchRepository.count() == 0) {

                branch = new Branch();
                branch.setCompany(company);
                branch.setName("Unidade Curitiba");
                branch.setAddress("Rua Central, 100");
                branch.setCity("Curitiba");
                branch.setState("PR");
                branch.setCountry("Brasil");

                branch = branchRepository.save(branch);

            } else {
                branch = branchRepository.findAll().get(0);
            }

            if (licenseRepository.count() == 0) {

                createLicense(
                        licenseRepository,
                        licenseStatusService,
                        company,
                        branch,
                        "Licenca Ambiental Vencida",
                        "Licenca EXPIRED",
                        "Ambiental",
                        "ENV-2024-001",
                        "Orgao Ambiental",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 12, 31),
                        "Status calculado automaticamente"
                );
            }
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