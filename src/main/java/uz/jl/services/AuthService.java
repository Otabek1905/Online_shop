package uz.jl.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.AuthUser;
import uz.jl.repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        return new UserDetails(authUser);
    }
}
