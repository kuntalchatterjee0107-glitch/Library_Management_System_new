package com.airtribe.library.repository;


import com.airtribe.library.entity.Patron;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationRepository {
    // Static FIFO queue mapped by book ISBN
    private static final Map<String, Queue<Patron>> RESERVATIONS = new ConcurrentHashMap<>();

    public void enqueue(String isbn, Patron patron) {

        RESERVATIONS.computeIfAbsent(isbn, k -> new LinkedList<>()).offer(patron);

    }

    public Optional<Patron> peek(String isbn) {
        Queue<Patron> queue = RESERVATIONS.get(isbn);
        return (queue == null || queue.isEmpty()) ? Optional.empty() : Optional.ofNullable(queue.peek());
    }

    public Optional<Patron> dequeue(String isbn) {
        Queue<Patron> queue = RESERVATIONS.get(isbn);
        return (queue == null || queue.isEmpty()) ? Optional.empty() : Optional.ofNullable(queue.poll());
    }
}