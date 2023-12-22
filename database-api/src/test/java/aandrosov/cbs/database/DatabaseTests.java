package aandrosov.cbs.database;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import aandrosov.cbs.database.Database;

import static org.junit.jupiter.api.Assertions.fail;

class DatabaseTests {

    @Test 
    void getConnectionTest() {
        try (var connection = Database.getConnection()) {
        } catch (Exception exception) {
            fail(exception.getMessage(), exception);
        }
    }
}