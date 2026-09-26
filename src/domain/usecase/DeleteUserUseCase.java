package domain.usecase;

import domain.repository.UserRepository;
import domain.validator.IdValidator;

public class DeleteUserUseCase {
    private final UserRepository userRepository;
    private final IdValidator idValidator;

    public DeleteUserUseCase(UserRepository userRepository, IdValidator idValidator) {
        this.userRepository = userRepository;
        this.idValidator = idValidator;
    }

    public void execute(int userId) {
        idValidator.validate(userId, "User ID"); // Валидируем
        userRepository.deleteUser(userId); // Удаляем
    }
}
