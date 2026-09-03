package com.airtribe.library.service;


import com.airtribe.library.entity.Book;
import com.airtribe.library.entity.Branch;
import com.airtribe.library.repository.BookRepository;
import com.airtribe.library.repository.BranchRepository;

import java.util.List;
import java.util.Optional;

public class BranchService {
    private final BranchRepository branchRepository = new BranchRepository();
    private final BookRepository bookRepository = new BookRepository();

    public void registerBranch(Branch branch) {
        branchRepository.save(branch);
    }

    public Optional<Branch> getBranch(String branchId) {
        return branchRepository.findById(branchId);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public void transferBook(String isbn, String targetBranchId) {

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found."));
        if (book.isBorrowed()) {
            throw new IllegalStateException("Cannot transfer a book that is currently checked out.");
        }
        branchRepository.findById(targetBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Target branch does not exist."));

        book.setBranchId(targetBranchId);

    }
}