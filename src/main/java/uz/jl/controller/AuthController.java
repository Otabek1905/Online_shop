package uz.jl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.jl.dto.UserCreateDto;
import uz.jl.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }


    @GetMapping("/register")
    public String registerPage(@ModelAttribute("dto") UserCreateDto dto) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@NotNull @ModelAttribute("dto") @Valid UserCreateDto dto, BindingResult result) {
        userService.register(dto, result);

        if (result.hasErrors()) {
            return "auth/register";
        }
        return "auth/login";
    }
}
