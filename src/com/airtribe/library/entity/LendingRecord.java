package com.airtribe.library.entity;

import java.time.LocalDate;

public class LendingRecord {
    private final String recordId;
    private final String patronId;
    private final Book book;
    private final LocalDate checkoutDate;
    private LocalDate returnDate;

    public LendingRecord(String recordId, String patronId, Book book, LocalDate checkoutDate) {
        this.recordId = recordId;
        this.patronId = patronId;
        this.book = book;
        this.checkoutDate = checkoutDate;
    }

    public String getRecordId() { return recordId; }
    public String getPatronId() { return patronId; }
    public Book getBook() { return book; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return String.format("Record ID: %s | Book: '%s' | Loan Date: %s | Return Date: %s",
                recordId, book.getTitle(), checkoutDate, (returnDate == null ? "ACTIVE" : returnDate));
    }
}