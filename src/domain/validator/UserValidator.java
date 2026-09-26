package domain.validator;

import domain.model.User;

public class UserValidator {
    // Минимальные/Максимальные константы + константа паттерна email
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 100;
    private static final int EMAIL_MAX_LENGTH = 255;
    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    public void validate(User user) {
        // Проверка существования пользователя
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        // Валидируем все столбцы
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validatePasswordHash(user.getPasswordHash());
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role is required"); // Если у пользователя нет роли выбрасываем ошибку
        }
    }

    // Метод валидации имени пользователя (!null & удовл min и max длине)
    public void validateUsername(String username) {
        if (isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < USERNAME_MIN_LENGTH) {
            throw new IllegalArgumentException("Username must be at least " + USERNAME_MIN_LENGTH + " characters");
        }
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("Username must be at most " + USERNAME_MAX_LENGTH + " characters");
        }
    }

    // Метод валидации email пользователя (!null & удовл max длине и паттерну)
    public void validateEmail(String email) {
        if (isBlank(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new IllegalArgumentException("Email must be at most " + EMAIL_MAX_LENGTH + " characters");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Email is invalid");
        }
    }

    // Метод валидации hash-пароля пользователя (!null)
    public void validatePasswordHash(String passwordHash) {
        if (isBlank(passwordHash)) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
    }

    // Метод проверки на пустое значение
    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
