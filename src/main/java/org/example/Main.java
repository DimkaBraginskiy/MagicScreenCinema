package org.example;

import org.example.persistence.ObjectCollection;
import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.model.Author;
import org.example.persistence.PersistenceInitializer;
import org.example.persistence.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceInitializer.run();

            /*ObjectCollection<Author> authorCollection = ObjectCollectionRegistry.getCollection(Author.class);
            ObjectCollection<Book> bookCollection = ObjectCollectionRegistry.getCollection(Book.class);

            Author author = new Author();
            author.setId(UUID.randomUUID());

            List<Book> books = new ArrayList<>();
            for(int i = 1; i <= 5; i++) {
                Book book = new Book();
                book.setId(UUID.randomUUID());
                book.setTitle("Book " + i);
                books.add(book);
            }
            author.setBooks(books);

            authorCollection.save(author);

            System.out.println(bookCollection.findAll());
            System.out.println();
            System.out.println(authorCollection.findById(UUID.fromString("1f172d03-406b-4ce0-9483-933c9d37750f")));*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}