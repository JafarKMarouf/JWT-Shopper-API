package com.jafarmarouf.jwtshopper.services.user;

import com.jafarmarouf.jwtshopper.Dtos.UserDto;
import com.jafarmarouf.jwtshopper.exceptions.AlreadyExistsException;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.repository.UserRepository;
import com.jafarmarouf.jwtshopper.requests.user.CreateUserRequest;
import com.jafarmarouf.jwtshopper.requests.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        return Optional.of(request)
                .filter(_ -> !userRepository.existsUserByEmail(request.getEmail()))
                .map(_ -> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setCreatedAt(LocalDateTime.now());
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setTotalAmount(BigDecimal.ZERO);
                    user.setCart(cart);
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
                    existingUser.setFirstName(Boolean.parseBoolean(request.getFirstName()) ? request.getFirstName() : existingUser.getFirstName());
                    existingUser.setLastName(Boolean.parseBoolean(request.getLastName()) ? request.getLastName() : existingUser.getLastName());
                    existingUser.setEmail(Boolean.parseBoolean(request.getEmail()) ? request.getEmail() : existingUser.getEmail());
                    existingUser.setPassword(Boolean.parseBoolean(request.getPassword()) ? request.getPassword() : existingUser.getPassword());
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
}
