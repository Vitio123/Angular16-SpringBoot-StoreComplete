package com.codeWithProjects.ecom.services.auth;

import com.codeWithProjects.ecom.dto.SignupRequest;
import com.codeWithProjects.ecom.dto.UserDto;
import com.codeWithProjects.ecom.entity.User;
import com.codeWithProjects.ecom.enums.UserRole;
import com.codeWithProjects.ecom.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(SignupRequest signupRequest){
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword())); // ✅ Usar el bean inyectado
        user.setRole(UserRole.CUSTOMER);

        User createUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());
        userDto.setName(createUser.getName()); // ✅ Agregar nombre
        userDto.setEmail(createUser.getEmail()); // ✅ Agregar email
        userDto.setUserRole(createUser.getRole()); // ✅ Mapear role correctamente

        return userDto;
    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if(null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(bCryptPasswordEncoder.encode("admin")); // ✅ Usar el bean inyectado
            userRepository.save(user);
        }
    }
}