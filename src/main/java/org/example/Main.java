package org.example;

import org.example.persistence.Book;
import org.example.persistence.PersistenceManager;
import org.example.persistence.SimpleObjectCollection;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceManager.run();

            Book book = new Book();
            book.setId(UUID.randomUUID());
            book.setTitle("1984");

            SimpleObjectCollection<Book> bookCollection = new SimpleObjectCollection<>();

            bookCollection.save(book);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}