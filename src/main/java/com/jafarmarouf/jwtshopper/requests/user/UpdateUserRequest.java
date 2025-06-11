package com.jafarmarouf.jwtshopper.requests.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
}
