package com.jafarmarouf.jwtshopper.services.user;

import com.jafarmarouf.jwtshopper.dtos.UserDto;
import com.jafarmarouf.jwtshopper.exceptions.AlreadyExistsException;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Role;
import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.repository.RolesRepository;
import com.jafarmarouf.jwtshopper.repository.UserRepository;
import com.jafarmarouf.jwtshopper.requests.user.CreateUserRequest;
import com.jafarmarouf.jwtshopper.requests.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;


    /**
     * @param id Long
     * @return User
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    /**
     * @param request CreateUserRequest
     * @return User
     */
    @Override
    public User createUser(CreateUserRequest request) {
        Role role = rolesRepository.findByName("ROLE_CUSTOMER").get();

        return Optional.of(request)
                .filter(_ -> !userRepository.existsUserByEmail(request.getEmail()))
                .map(_ -> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setCreatedAt(LocalDateTime.now());
                    user.setRoles(Set.of(role));
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("Oops!," + request.getEmail() + " already exists!"));
    }

    /**
     * @param request UpdateUserRequest
     * @param id      Long
     * @return User
     */
    @Override
    public User updateUser(UpdateUserRequest request, Long id) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
                        existingUser.setFirstName(request.getFirstName());
                    }
                    if (request.getLastName() != null && !request.getLastName().isBlank()) {
                        existingUser.setLastName(request.getLastName());
                    }
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    /**
     * @param id Long
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found!");
                });
    }

    /**
     * @param user User
     * @return UserDto
     */
    @Override
    public UserDto convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * @return User
     */
    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email);
    }
}
