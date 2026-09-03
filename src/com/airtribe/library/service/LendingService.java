package com.airtribe.library.service;



import com.airtribe.library.entity.Book;
import com.airtribe.library.entity.LendingRecord;
import com.airtribe.library.entity.Patron;
import com.airtribe.library.repository.BookRepository;
import com.airtribe.library.repository.LendingRepository;
import com.airtribe.library.repository.PatronRepository;
import com.airtribe.library.repository.ReservationRepository;
import com.airtribe.library.starategy.recommendation.RecommendationStrategy;

import java.time.LocalDate;
import java.util.*;

public class LendingService {
    private final BookRepository bookRepository = new BookRepository();
    private final PatronRepository patronRepository = new PatronRepository();
    private final LendingRepository lendingRepository = new LendingRepository();
    private final ReservationRepository reservationRepository = new ReservationRepository();

    public synchronized LendingRecord checkout(String isbn, String patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found."));

        if (book.isBorrowed()) {
            throw new IllegalStateException("Book is already checked out.");
        }

        // Reservation check
        Optional<Patron> nextReserved = reservationRepository.peek(isbn);
        if (nextReserved.isPresent() && !nextReserved.get().getPatronId().equals(patronId)) {
            throw new IllegalStateException("Book is reserved for patron: " + nextReserved.get().getName());
        }

        if (nextReserved.isPresent()) {
            reservationRepository.dequeue(isbn); // Claimed by reserved patron
        }

        book.setBorrowed(true);
        String recordId = UUID.randomUUID().toString().substring(0, 8);
        LendingRecord record = new LendingRecord(recordId, patronId, book, LocalDate.now());

        lendingRepository.saveActiveLoan(isbn, record);
        patron.addBorrowingRecord(record);
        return record;
    }

    public synchronized void returnBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));

        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is not currently marked as borrowed.");
        }

        LendingRecord record = lendingRepository.removeActiveLoan(isbn)
                .orElseThrow(() -> new IllegalStateException("No active checkout record found."));
        record.setReturnDate(LocalDate.now());
        book.setBorrowed(false);

        // Notify next patron in line
        reservationRepository.peek(isbn).ifPresent(reservedPatron -> {
            reservedPatron.update("The book '" + book.getTitle() + "' is now returned and ready for you!");
        });
    }

    public synchronized void reserveBook(String isbn, String patronId) {

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found."));

        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is available on shelf. You can check it out directly.");
        }

        reservationRepository.enqueue(isbn, patron);

    }

    public List<Book> recommendBooks(String patronId, RecommendationStrategy strategy) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found."));
        return strategy.recommend(patron, bookRepository.findAll());
    }
}