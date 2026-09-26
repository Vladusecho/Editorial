package domain.usecase;

import domain.model.User;
import domain.repository.UserRepository;
import domain.validator.IdValidator;

public class GetUserByIdUseCase {
    private final UserRepository userRepository;
    private final IdValidator idValidator;

    public GetUserByIdUseCase(UserRepository userRepository, IdValidator idValidator) {
        this.userRepository = userRepository;
        this.idValidator = idValidator;
    }

    public User execute(int userId) {
        idValidator.validate(userId, "User ID"); // Валидируем id
        return userRepository.getUserById(userId); // Вовращаем пользователя
    }
}
