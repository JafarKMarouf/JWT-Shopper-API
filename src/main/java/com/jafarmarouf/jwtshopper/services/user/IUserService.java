package com.jafarmarouf.jwtshopper.services.user;

import com.jafarmarouf.jwtshopper.dtos.UserDto;
import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.requests.user.CreateUserRequest;
import com.jafarmarouf.jwtshopper.requests.user.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long id);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long id);
    void deleteUser(Long id);

    UserDto convertUserToUserDto(User user);

    User getAuthenticatedUser();
}
