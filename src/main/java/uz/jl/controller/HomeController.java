package uz.jl.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.Product;
import uz.jl.repository.ProductRepository;
import uz.jl.services.ProductService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @RequestMapping
    @PreAuthorize("permitAll()")
    public String homePage(
            @RequestParam(name = "search") Optional<String> search,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "limit") Optional<Integer> limit,
            Model model) {

        String searchQuery = search.orElse("");

        Page<Product> productPage = productService.findAllByPage(page,limit,searchQuery);

        model.addAttribute("page", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("pageNumbers", IntStream.range(0, productPage.getTotalPages()).toArray());

        return "home";
    }


    @ResponseBody
    @RequestMapping("/open-1")
    public String open1Page() {
        return "OPEN URL";
    }

    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin() {
        return "ADMIN PAGE";
    }

    @ResponseBody
    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping("/mana")
    public String manager() {
        return "MANAGER PAGE";
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @RequestMapping("/admin-manager")
    public String adminManager() {
        return "ADMIN AND MANAGER PAGE";
    }

    @ResponseBody
    @PreAuthorize("isAuthenticated() || hasAuthority('create')")
    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal UserDetails userDetails) {
        return "" + userDetails;
    }


    @ResponseBody
    @RequestMapping("/create")
    public String hasAuthorityCreate() {
        return "hasAuthorityCreate";
    }


}
