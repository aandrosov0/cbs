package aandrosov.cbs.database.exception;

public class RepositoryRuntimeException extends RuntimeException {

    public RepositoryRuntimeException(Exception exception) {
        super(exception);
    }

    public RepositoryRuntimeException(String message) {
        super(message);
    }
}