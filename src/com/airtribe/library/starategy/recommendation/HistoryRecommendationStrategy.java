package com.airtribe.library.starategy.recommendation;


import com.airtribe.library.entity.Book;
import com.airtribe.library.entity.Patron;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryRecommendationStrategy implements RecommendationStrategy {
    @Override
    public List<Book> recommend(Patron patron, Collection<Book> books) {
        Set<String> readIsbns = patron.getBorrowingHistory().stream()
                .map(r -> r.getBook().getIsbn())
                .collect(Collectors.toSet());

        Set<String> favoredAuthors = patron.getBorrowingHistory().stream()
                .map(r -> r.getBook().getAuthor().toLowerCase())
                .collect(Collectors.toSet());

        return books.stream()
                .filter(b -> !b.isBorrowed())
                .filter(b -> !readIsbns.contains(b.getIsbn()))
                .filter(b -> patron.getPreferredGenres().contains(b.getGenre().toLowerCase())
                        || favoredAuthors.contains(b.getAuthor().toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());
    }
}