package org.example;

import org.example.persistence.model.Author;
import org.example.persistence.PersistenceManager;
import org.example.persistence.SimpleObjectCollection;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceManager.run();

            SimpleObjectCollection<Author> authorCollection = new SimpleObjectCollection<>(Author.class);

            Author author = authorCollection.findById(UUID.fromString("c1403cf6-15db-4bc8-b471-9320d0341a54"))
                    .orElse(new Author());

            System.out.println(author);
            System.out.println(authorCollection.findAll());
            System.out.println(authorCollection.existsById(UUID.fromString("c1403cf6-15db-4bc8-b471-9320d0341a54")));
            System.out.println(authorCollection.existsById(UUID.randomUUID()));
            System.out.println(authorCollection.deleteById(UUID.fromString("c1403cf6-15db-4bc8-b471-9320d0341a54")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}