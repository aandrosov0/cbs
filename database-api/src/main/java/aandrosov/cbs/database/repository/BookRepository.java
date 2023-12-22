package aandrosov.cbs.database.repository;

import java.io.IOException;
import java.sql.SQLException;
import aandrosov.cbs.database.exception.RepositoryRuntimeException;

import java.sql.ResultSet;
import java.sql.Statement;

import aandrosov.cbs.database.Database;
import aandrosov.cbs.model.Book;

public class BookRepository implements CRUDRepository<Book> {

    @Override
    public long create(Book book) {
        String sql = "INSERT INTO book(title, description, cover_url) VALUES(?, ?, ?)";

        try (var connection = Database.getConnection();
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.title());
            preparedStatement.setString(2, book.description());
            preparedStatement.setString(3, book.coverUrl());
                
            if (preparedStatement.executeUpdate() == 0) {
                throw new RepositoryRuntimeException("Cannot create the book, no affected rows.");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new RepositoryRuntimeException("Cannot create the book, no generated key.");
            }

            return generatedKeys.getLong(1);
        } catch (IOException | SQLException exception) {
            throw new RepositoryRuntimeException(exception);
        }
    }

    @Override
    public Book read(long id) {
        String sql = "SELECT * FROM book WHERE id=" + id;
        try (var connection = Database.getConnection();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return new Book(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("cover_url")
                );
            }
        } catch (IOException | SQLException exception) {
            throw new RepositoryRuntimeException(exception);
        }

        return null;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE book SET title = ?, description = ?, cover_url = ? WHERE id=" + book.id();
        
        try (var connection = Database.getConnection();
            var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.title());
            preparedStatement.setString(2, book.description());
            preparedStatement.setString(3, book.coverUrl());

            return preparedStatement.executeUpdate() != 0;
        } catch (IOException | SQLException exception) {
            throw new RepositoryRuntimeException(exception);
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM book WHERE id=" + id;

        try (var connection = Database.getConnection();
            var statement = connection.createStatement()) {
            return statement.executeUpdate(sql) != 0;
        } catch (IOException | SQLException exception) {
            throw new RepositoryRuntimeException(exception);
        }
    }
}