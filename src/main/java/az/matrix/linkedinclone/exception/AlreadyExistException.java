package az.matrix.linkedinclone.exception;


public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(Class<?> entity) {
        super(entity.getSimpleName().toUpperCase() + "_ALREADY_EXIST");
    }
}
