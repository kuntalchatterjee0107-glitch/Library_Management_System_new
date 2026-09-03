package com.airtribe.library.service;


import com.airtribe.library.entity.Book;
import com.airtribe.library.repository.BookRepository;
import com.airtribe.library.starategy.search.SearchStrategy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {
    private final BookRepository bookRepository = new BookRepository();

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public boolean updateBook(String isbn, String title, String author, int year, String genre) {
        Optional<Book> opt = bookRepository.findByIsbn(isbn);
        if (opt.isPresent()) {
            Book book = opt.get();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublicationYear(year);
            book.setGenre(genre);
            return true;
        }
        return false;
    }

    public boolean removeBook(String isbn) {
        return bookRepository.delete(isbn);
    }

    public Optional<Book> getBook(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> search(SearchStrategy strategy, String query, String branchId) {
        List<Book> books = bookRepository.findAll();
        if (branchId != null && !branchId.isBlank()) {
            books = books.stream()
                    .filter(b -> b.getBranchId().equalsIgnoreCase(branchId))
                    .collect(Collectors.toList());
        }
        return strategy.search(books, query);
    }
}