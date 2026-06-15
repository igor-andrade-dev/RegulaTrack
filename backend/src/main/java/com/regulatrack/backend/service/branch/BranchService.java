package com.regulatrack.backend.service.branch;

import com.regulatrack.backend.domain.branch.Branch;
import com.regulatrack.backend.domain.company.Company;
import com.regulatrack.backend.dto.branch.BranchResponse;
import com.regulatrack.backend.dto.branch.CreateBranchRequest;
import com.regulatrack.backend.dto.branch.UpdateBranchRequest;
import com.regulatrack.backend.exception.ResourceNotFoundException;
import com.regulatrack.backend.repository.branch.BranchRepository;
import com.regulatrack.backend.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public BranchResponse create(CreateBranchRequest request) {
        Company company = getCompanyById(request.companyId());

        Branch branch = Branch.builder()
                .company(company)
                .name(request.name())
                .address(request.address())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .build();

        Branch savedBranch = branchRepository.save(branch);

        return toResponse(savedBranch);
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> findAll(Long companyId) {
        List<Branch> branches;

        if (companyId != null) {
            branches = branchRepository.findByCompany_Id(companyId);
        } else {
            branches = branchRepository.findAll();
        }

        return branches.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BranchResponse findById(Long id) {
        Branch branch = getBranchById(id);

        return toResponse(branch);
    }

    @Transactional
    public BranchResponse update(Long id, UpdateBranchRequest request) {
        Branch branch = getBranchById(id);
        Company company = getCompanyById(request.companyId());

        branch.setCompany(company);
        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setCity(request.city());
        branch.setState(request.state());
        branch.setCountry(request.country());

        Branch updatedBranch = branchRepository.save(branch);

        return toResponse(updatedBranch);
    }

    @Transactional
    public void delete(Long id) {
        Branch branch = getBranchById(id);

        branchRepository.delete(branch);
    }

    private Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com id: " + id));
    }

    private Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
    }

    private BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getCompany().getId(),
                branch.getCompany().getName(),
                branch.getName(),
                branch.getAddress(),
                branch.getCity(),
                branch.getState(),
                branch.getCountry(),
                branch.getCreatedAt(),
                branch.getUpdatedAt()
        );
    }
}