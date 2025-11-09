package org.example;

import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.model.Author;
import org.example.persistence.PersistenceManager;
import org.example.persistence.SimpleObjectCollection;
import org.example.persistence.model.Book;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceManager.run();

            SimpleObjectCollection<Author> authorCollection = new SimpleObjectCollection<>(Author.class);
            SimpleObjectCollection<Book> bookCollection = new SimpleObjectCollection<>(Book.class);

            ObjectCollectionRegistry.registerCollection("authors", authorCollection);
            ObjectCollectionRegistry.registerCollection("books", bookCollection);


            Author author = authorCollection.findById(UUID.fromString("37c5b371-d3a3-42d2-82cc-06553db3df36"))
                    .orElse(new Author());


            System.out.println(author);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}