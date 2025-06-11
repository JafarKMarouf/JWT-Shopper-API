package com.jafarmarouf.jwtshopper.controller;

import com.jafarmarouf.jwtshopper.dtos.UserDto;
import com.jafarmarouf.jwtshopper.exceptions.AlreadyExistsException;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.requests.user.CreateUserRequest;
import com.jafarmarouf.jwtshopper.requests.user.UpdateUserRequest;
import com.jafarmarouf.jwtshopper.response.ApiResponse;
import com.jafarmarouf.jwtshopper.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{id}/user")
    public ResponseEntity<ApiResponse> findUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> storeUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.status(CREATED).body(new ApiResponse("User created", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long id) {
        try {
            User user = userService.updateUser(request, id);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("User updated success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("User deleted success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
