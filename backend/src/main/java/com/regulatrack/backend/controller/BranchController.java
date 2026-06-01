package com.regulatrack.backend.controller;

import com.regulatrack.backend.dto.branch.BranchResponse;
import com.regulatrack.backend.dto.branch.CreateBranchRequest;
import com.regulatrack.backend.dto.branch.UpdateBranchRequest;
import com.regulatrack.backend.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse create(@RequestBody @Valid CreateBranchRequest request) {
        return branchService.create(request);
    }

    @GetMapping
    public List<BranchResponse> findAll(@RequestParam(required = false) Long companyId) {
        return branchService.findAll(companyId);
    }

    @GetMapping("/{id}")
    public BranchResponse findById(@PathVariable Long id) {
        return branchService.findById(id);
    }

    @PutMapping("/{id}")
    public BranchResponse update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBranchRequest request
    ) {
        return branchService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        branchService.delete(id);
    }
}