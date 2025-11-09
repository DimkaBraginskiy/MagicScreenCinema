package org.example;

import org.example.persistence.model.Author;
import org.example.persistence.model.Book;
import org.example.persistence.PersistenceManager;
import org.example.persistence.SimpleObjectCollection;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceManager.run();

            Book book = new Book();
            book.setId(UUID.randomUUID());
            book.setTitle("1984");

            SimpleObjectCollection<Book> bookCollection = new SimpleObjectCollection<>(Book.class);
            SimpleObjectCollection<Author> authorCollection = new SimpleObjectCollection<>(Author.class);


            Author author = new Author();
            author.setId(UUID.randomUUID());
            author.setBooks(List.of(book));
            author.setName("George Orwell");
            authorCollection.save(author);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}