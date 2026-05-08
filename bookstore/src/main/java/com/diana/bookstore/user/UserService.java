package com.diana.bookstore.user;

import com.diana.bookstore.basket.Basket;
import com.diana.bookstore.basket.BasketRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;

    public UserService(UserRepository userRepository, BasketRepository basketRepository) {
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
    }

    public void save(User user) {
        User savedUser = userRepository.save(user);
        Basket basket = new Basket();
        basket.setUser(savedUser);
        basketRepository.save(basket);
    }

    public UserDto getUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Not found user with email = " + email));
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

}
