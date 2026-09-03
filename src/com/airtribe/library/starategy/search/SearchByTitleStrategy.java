package com.airtribe.library.starategy.search;


import com.airtribe.library.entity.Book;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SearchByTitleStrategy implements SearchStrategy {
    @Override
    public List<Book> search(Collection<Book> books, String query) {

        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

    }
}