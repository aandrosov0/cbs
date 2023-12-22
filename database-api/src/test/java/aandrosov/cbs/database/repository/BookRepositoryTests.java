package aandrosov.cbs.database.repository;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import aandrosov.cbs.model.Book;

import aandrosov.cbs.database.Database;

import aandrosov.cbs.database.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BookRepositoryTests {

    private final BookRepository repository = new BookRepository();

    @BeforeEach
    void initTableBeforeEachTest() {
        String sql = """
            DROP TABLE IF EXISTS book;
            CREATE TABLE book(id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, description text NOT NULL, cover_url VARCHAR(1024) NOT NULL);
            INSERT INTO book(title, description, cover_url) VALUES
            ('Harry Potter', 'Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling.', 'url'),
            ('1984', 'Nineteen Eighty-Four is a dystopian novel and cautionary tale by English writer George Orwell.', 'url'),
            ('Fathers and Sons', 'Fathers and Sons is an 1862 novel by Ivan Turgenev, published in Moscow', 'url');
        """;

        try (var connection = Database.getConnection();
            var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (IOException | SQLException exception) {
            fail(exception.getMessage(), exception);
        }
    }

    @Test 
    void testCreate() {
        Book expectedBook = new Book(4, "Some Title", "Some Description", "Some URL");

        long id = repository.create(expectedBook);
        assertEquals(id, expectedBook.id());

        Book book = repository.read(expectedBook.id());
        assertTrue(expectedBook.equals(book));
    }

    @Test 
    void testRead() {
        Book expectedBook = new Book(2, "1984", "Nineteen Eighty-Four is a dystopian novel and cautionary tale by English writer George Orwell.", "url");
        Book book = repository.read(expectedBook.id());
        assertTrue(expectedBook.equals(book));
    }

    @Test 
    void testUpdate() {
        Book expectedBook = new Book(2, "Some Title", "Some Description", "Some URL");
        boolean result = repository.update(expectedBook);
        assertTrue(result);

        Book book = repository.read(expectedBook.id());
        assertTrue(expectedBook.equals(book));
    }

    @Test 
    void testDelete() {
        long id = 2;

        boolean result = repository.delete(id);
        assertTrue(result);

        Book book = repository.read(id);
        assertNull(book);
    }
}