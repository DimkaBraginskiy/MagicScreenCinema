package org.example;

import org.example.persistence.ObjectCollection;
import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.model.Author;
import org.example.persistence.PersistenceInitializer;
import org.example.persistence.model.Book;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceInitializer.run();

            ObjectCollection<Author> authorCollection = ObjectCollectionRegistry.getCollection(Author.class);
            ObjectCollection<Book> bookCollection = ObjectCollectionRegistry.getCollection(Book.class);

            Book book = new Book();
            book.setId(UUID.randomUUID());
            book.setTitle("Harry Potter and the Philosopher's Stone");

            Author author = new Author();
            author.setId(UUID.randomUUID());
            author.setName("J.K. Rowling");
            author.setBooks(List.of(book));

            authorCollection.save(author);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}