package com.airtribe.library.starategy.recommendation;


import com.airtribe.library.entity.Book;
import com.airtribe.library.entity.Patron;

import java.util.Collection;
import java.util.List;

public interface RecommendationStrategy {
    List<Book> recommend(Patron patron, Collection<Book> books);


}