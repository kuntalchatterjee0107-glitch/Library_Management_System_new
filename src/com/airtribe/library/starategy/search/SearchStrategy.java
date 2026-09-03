package com.airtribe.library.starategy.search;

import com.airtribe.library.entity.Book;
import java.util.Collection;
import java.util.List;

public interface SearchStrategy {

    List<Book> search(Collection<Book> books, String query);

}