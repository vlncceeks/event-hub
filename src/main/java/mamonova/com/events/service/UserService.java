package mamonova.com.events.service;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.SignInRequest;
import mamonova.com.events.dto.request.SignUpRequest;
import mamonova.com.events.dto.request.UpdateUserRequest;
import mamonova.com.events.exception.AccessDeniedException;
import mamonova.com.events.exception.NotFoundException;
import mamonova.com.events.exception.UserAlreadyExistsException;
import mamonova.com.events.exception.WrongPasswordException;
import mamonova.com.events.model.Role;
import mamonova.com.events.model.User;
import mamonova.com.events.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User login(SignInRequest request) {
        User user = findByEmail(request.getEmail());

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new WrongPasswordException("Wrong password");
        }

        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return findById(id);
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User user = findById(id);

        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
        user.setUsername(request.getUsername() != null ? request.getUsername() : user.getUsername());
        if (request.getPassword() != null)
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return user;
    }

    private User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return user;
    }

    public void onlyAdmin(Long userId) {
        User user = getUser(userId);

        if (user.getRole() != Role.ADMIN) throw new AccessDeniedException("Not enough rights");
    }
}
