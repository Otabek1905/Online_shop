package uz.jl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uz.jl.domains.Product;
import uz.jl.dto.ProductCreateDto;
import uz.jl.services.ProductService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;


    @RequestMapping("/productCreate")
    @PreAuthorize("hasRole('ADMIN')")
    public String productCreate(@ModelAttribute ProductCreateDto dto,
                                @RequestParam("file") CommonsMultipartFile file) {
        service.create(dto, file);
        return "redirect:/";
    }


    @RequestMapping("/productList")
    @PreAuthorize("hasRole('ADMIN')")
    public String productList(
            @RequestParam(name = "search") Optional<String> search,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "limit") Optional<Integer> limit,
            Model model) {

        String searchQuery = search.orElse("");

        Page<Product> productPage = service.findAllByPage(page, limit, searchQuery);

        model.addAttribute("page", productPage);
        model.addAttribute("pageNumbers", IntStream.range(0, productPage.getTotalPages()).toArray());

        return "product_list";
    }

    @RequestMapping(value = "/productDelete", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String productDelete(@RequestParam("product") Long id) {
        service.delete(id);
        return "redirect:/productList";
    }

    @RequestMapping(value = "/productUpdate", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public String productUpdate(@RequestParam("product") Long id,
                                @ModelAttribute("color") String color,
                                @ModelAttribute("price") String price) {
        service.update(id,color,price);
        return "redirect:/productUpdateList";
    }

    @GetMapping(value = "/display")
    public void getImage(@RequestParam("img") String path, HttpServletResponse response) throws IOException {
        Path imagePath = Paths.get("C:/src/Spring/online-shop/src/main/resources/static/images/", path);
        ServletOutputStream outputStream = response.getOutputStream();
        Files.copy(imagePath, outputStream);
    }

    @RequestMapping("/productUpdateList")
    @PreAuthorize("hasRole('ADMIN')")
    public String productUpdateList(
            @RequestParam(name = "search") Optional<String> search,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "limit") Optional<Integer> limit,
            Model model) {

        String searchQuery = search.orElse("");

        Page<Product> productPage = service.findAllByPage(page, limit, searchQuery);

        model.addAttribute("page", productPage);
        model.addAttribute("pageNumbers", IntStream.range(0, productPage.getTotalPages()).toArray());

        return "product_update_list";
    }


}
