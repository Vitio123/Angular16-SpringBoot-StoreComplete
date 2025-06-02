package com.codeWithProjects.ecom.services.jwt.auth;

import com.codeWithProjects.ecom.dto.SignupRequest;
import com.codeWithProjects.ecom.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);
}
