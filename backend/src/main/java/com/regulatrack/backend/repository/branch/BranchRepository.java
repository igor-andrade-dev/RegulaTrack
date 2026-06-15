package com.regulatrack.backend.repository.branch;

import com.regulatrack.backend.domain.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByCompany_Id(Long companyId);
}