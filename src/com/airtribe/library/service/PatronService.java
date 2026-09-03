package com.airtribe.library.service;


import com.airtribe.library.entity.Patron;
import com.airtribe.library.repository.PatronRepository;

import java.util.List;
import java.util.Optional;

public class PatronService {
    private final PatronRepository patronRepository = new PatronRepository();

    public void registerPatron(Patron patron) {
        patronRepository.save(patron);
    }

    public Optional<Patron> getPatron(String patronId) {
        return patronRepository.findById(patronId);
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public boolean updateEmail(String patronId, String newEmail) {
        Optional<Patron> opt = patronRepository.findById(patronId);
        if (opt.isPresent()) {
            opt.get().setEmail(newEmail);
            return true;
        }
        return false;
    }

    public boolean addPreference(String patronId, String genre) {

        Optional<Patron> opt = patronRepository.findById(patronId);
        if (opt.isPresent()) {
            opt.get().addPreferredGenre(genre);
            return true;
        }
        return false;

    }
}