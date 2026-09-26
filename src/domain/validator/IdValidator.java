package domain.validator;

public class IdValidator {
    // Метод проверки id > 0
    public void validate(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }
    }
}
