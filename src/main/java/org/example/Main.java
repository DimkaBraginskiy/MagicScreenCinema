package org.example;

import org.example.persistence.ObjectCollection;
import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.model.Author;
import org.example.persistence.model.Book;
import org.example.persistence.model.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        /*try {
            ObjectCollection<Author> authorCollection = ObjectCollectionRegistry.getCollection(Author.class);
            ObjectCollection<Book> bookCollection = ObjectCollectionRegistry.getCollection(Book.class);
            ObjectCollection<Publisher> publisherCollection = ObjectCollectionRegistry.getCollection(Publisher.class);

            Author author = new Author();
            author.setId(UUID.randomUUID());
            author.setName("Author 1");

            List<Book> books = new ArrayList<>();
            for(int i = 1; i <= 5; i++) {
                Book book = new Book();
                book.setId(UUID.randomUUID());
                book.setTitle("Book " + i);
                books.add(book);
            }
            books.add(null);

            author.setBooks(books);
            Publisher publisher = new Publisher();
            publisher.setId(UUID.randomUUID());
            publisher.setName("Publisher 1");

            publisher.setBooks(books);
            publisherCollection.save(publisher);

            authorCollection.save(author);

            System.out.println(bookCollection.findAll());
            System.out.println(publisherCollection.findAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}