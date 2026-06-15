package com.regulatrack.backend.repository.company;

import com.regulatrack.backend.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}