package domain.usecase;

import domain.model.User;
import domain.repository.UserRepository;
import domain.validator.IdValidator;
import domain.validator.UserValidator;

public class EditUserUseCase {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final IdValidator idValidator;

    public EditUserUseCase(
            UserRepository userRepository,
            UserValidator userValidator,
            IdValidator idValidator
    ) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.idValidator = idValidator;
    }

    public void execute(
            int userId,
            String username,
            String email,
            String passwordHash,
            User.Role role
    ) {
        idValidator.validate(userId, "User ID"); // Валидируем id

        User user = userRepository.getUserById(userId); // Получаем пользователя по id

        // Редактируем пользователя
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(role);

        userValidator.validate(user); // Валидируем
        userRepository.editUser(user); // Изменяем в БД
    }
}
