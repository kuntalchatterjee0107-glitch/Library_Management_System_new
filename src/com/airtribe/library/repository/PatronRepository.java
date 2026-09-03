package com.airtribe.library.repository;


import com.airtribe.library.entity.Patron;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PatronRepository {
    // Static collection stores all patrons in memory
    private static final Map<String, Patron> PATRON_STORAGE = new ConcurrentHashMap<>();

    public void save(Patron patron) {

        PATRON_STORAGE.put(patron.getPatronId(), patron);

    }

    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(PATRON_STORAGE.get(patronId));
    }

    public List<Patron> findAll() {
        return new ArrayList<>(PATRON_STORAGE.values());
    }
}