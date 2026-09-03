package com.airtribe.library.entity;


import com.airtribe.library.observer.Observer;

import java.util.*;

public class Patron implements Observer {
    private final String patronId;
    private String name;
    private String email;
    private final List<LendingRecord> borrowingHistory;
    private final Set<String> preferredGenres;

    public Patron(String patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.borrowingHistory = new ArrayList<>();
        this.preferredGenres = new HashSet<>();
    }

    public String getPatronId() { return patronId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<LendingRecord> getBorrowingHistory() { return Collections.unmodifiableList(borrowingHistory); }
    public Set<String> getPreferredGenres() { return preferredGenres; }

    public void addBorrowingRecord(LendingRecord record) { borrowingHistory.add(record); }
    public void addPreferredGenre(String genre) { preferredGenres.add(genre.toLowerCase()); }

    @Override
    public void update(String message) {
        System.out.printf("%n>>> [NOTIFICATION to %s (%s)]: %s%n", name, email, message);
    }

    @Override
    public String toString() {
        return String.format("Patron ID: %s | Name: %s | Email: %s | Preferences: %s",
                patronId, name, email, preferredGenres);
    }
}