package com.airtribe.library.repository;


import com.airtribe.library.entity.Book;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookRepository {
    // Static collection stores all books in memory
    private static final Map<String, Book> BOOK_STORAGE = new ConcurrentHashMap<>();


    public void save(Book book) {
        BOOK_STORAGE.put(book.getIsbn(), book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(BOOK_STORAGE.get(isbn));
    }

    public List<Book> findAll() {
        return new ArrayList<>(BOOK_STORAGE.values());
    }

    public boolean delete(String isbn) {
        return BOOK_STORAGE.remove(isbn) != null;
    }
}