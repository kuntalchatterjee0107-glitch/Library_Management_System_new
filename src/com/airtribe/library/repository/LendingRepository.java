package com.airtribe.library.repository;


import com.airtribe.library.entity.LendingRecord;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LendingRepository {
    // Static storage for active checkouts mapped by ISBN
    private static final Map<String, LendingRecord> ACTIVE_LOANS = new ConcurrentHashMap<>();


    public void saveActiveLoan(String isbn, LendingRecord record) {
        ACTIVE_LOANS.put(isbn, record);
    }

    public Optional<LendingRecord> findActiveLoan(String isbn) {
        return Optional.ofNullable(ACTIVE_LOANS.get(isbn));
    }

    public Optional<LendingRecord> removeActiveLoan(String isbn) {
        return Optional.ofNullable(ACTIVE_LOANS.remove(isbn));
    }
}