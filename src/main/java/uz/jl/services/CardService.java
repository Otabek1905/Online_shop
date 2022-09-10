package uz.jl.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.Card;
import uz.jl.domains.Product;
import uz.jl.repository.CardRepository;
import uz.jl.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ProductRepository productRepository;

    public void create(Long id, UserDetails userDetails) {
        Product product = productRepository.findById(id).orElse(new Product());

        List<Card> cards = cardRepository.findByUserId(userDetails.getUser().getId());
        for (Card card : cards) {
            if (card.getProductName().equals(product.getName())){
                card.setAmount(card.getAmount()+1);
                card.setTotal(card.getPrice()*card.getAmount());
                cardRepository.save(card);
                return;
            }
        }

        Card card = Card.builder()
                .productName(product.getName())
                .productColor(product.getColor())
                .price(product.getPrice())
                .user_id(userDetails.getUser().getId())
                .amount(1)
                .total(product.getPrice()*1)
                .productCoverGeneratedName(product.getCover().getGeneratedName())
                .build();

        cardRepository.save(card);
    }

    public void buy(UserDetails userDetails) {
        cardRepository.deleteByUserId(userDetails.getUser().getId());

    }
}
