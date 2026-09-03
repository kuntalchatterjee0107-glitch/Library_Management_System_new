package com.airtribe.library.entity;

import java.util.Objects;

public class Book {
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private String branchId;
    private boolean isBorrowed;


    public Book(String isbn, String title, String author, int publicationYear, String genre, String branchId) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.branchId = branchId;
        this.isBorrowed = false;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() { return Objects.hash(isbn); }

    @Override
    public String toString() {
        return String.format("[%s] '%s' by %s (%d) | Genre: %s | Branch: %s | Status: %s",
                isbn, title, author, publicationYear, genre, branchId, (isBorrowed ? "CHECKED OUT" : "AVAILABLE"));
    }
}