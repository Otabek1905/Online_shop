package uz.jl.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import uz.jl.domains.AuthRole;
import uz.jl.domains.AuthUser;
import uz.jl.dto.UserCreateDto;
import uz.jl.repository.AuthRepository;
import uz.jl.repository.AuthRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository repository;
    private final AuthRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserCreateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return;
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.addError(new FieldError("dto","password","Passwords didn't match"));
            return;
        }

        AuthUser user = repository.findByUsername(dto.getUsername()).orElse(null);

        if (Objects.nonNull(user)) {
            result.addError(new FieldError("dto","username","Username already exists"));
            return;
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .active(true)
                .build();

        repository.save(authUser);
    }
}
