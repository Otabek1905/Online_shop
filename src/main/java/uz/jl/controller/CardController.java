package uz.jl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.Card;
import uz.jl.domains.Product;
import uz.jl.repository.CardRepository;
import uz.jl.services.CardService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardService service;
    private final CardRepository repository;


    @RequestMapping(value = "/cardCreate", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String cardCreate(@RequestParam("product") Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        service.create(id, userDetails);
        return "redirect:/";
    }


    @RequestMapping("/basket")
    @PreAuthorize("isAuthenticated()")
    public String basketPage(@AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        List<Card> cards = repository.findByUserId(userDetails.getUser().getId());
        double totalAmount = 0;

        for (Card card : cards) {
            totalAmount += card.getAmount() * card.getPrice();
        }

        model.addAttribute("cards", cards);
        model.addAttribute("totalAmount", totalAmount);
        return "basket";
    }


    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public String buy(@AuthenticationPrincipal UserDetails userDetails) {
        service.buy(userDetails);
        return "redirect:/";
    }


    @RequestMapping(value = "/cardDelete", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String cardDelete(@RequestParam("cardId") Long id) {
        repository.delete(repository.findById(id).get());
        return "redirect:/basket";
    }

}
