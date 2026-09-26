package domain.usecase;

import domain.model.User;
import domain.repository.UserRepository;
import domain.validator.UserValidator;

public class AddUserUseCase {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public AddUserUseCase(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public void execute(User user) {
        userValidator.validate(user); // Валидируем
        userRepository.addUser(user); // Добавляем
    }
}
