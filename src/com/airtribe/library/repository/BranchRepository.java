package com.airtribe.library.repository;


import com.airtribe.library.entity.Branch;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BranchRepository {
    // Static collection stores all library branches
    private static final Map<String, Branch> BRANCH_STORAGE = new ConcurrentHashMap<>();


    public void save(Branch branch) {
        BRANCH_STORAGE.put(branch.getBranchId(), branch);
    }

    public Optional<Branch> findById(String branchId) {
        return Optional.ofNullable(BRANCH_STORAGE.get(branchId));
    }

    public List<Branch> findAll() {
        return new ArrayList<>(BRANCH_STORAGE.values());
    }
}