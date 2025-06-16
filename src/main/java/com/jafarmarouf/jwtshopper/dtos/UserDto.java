package com.jafarmarouf.jwtshopper.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleDto> roles;
    private CartDto cart;
    private List<OrderDto> orders;
}
