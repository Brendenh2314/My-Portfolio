package com.example.demo.services;

import com.example.demo.dao.CartRepository;
import com.example.demo.dao.CustomerRepository;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static com.example.demo.entities.StatusType.ordered;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private  CustomerRepository customerRepository;
    private CartRepository cartRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;

    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
try {
    Cart cart = purchase.getCart();

    // Generate order tracking number and set it
    String orderTrackingNumber = generateOrderTrackingNumber();
    cart.setOrderTrackingNumber(orderTrackingNumber);

    // Populate cart with cart items
    Set<CartItem> cartItems = purchase.getCartItems();
    if (cartItems == null || cartItems.isEmpty()) {
        throw new Exception("Check cart items, cart items can not empty");
    }
    cartItems.forEach(item -> cart.add(item));

    //Set status type
    cart.setStatus(ordered);

    // Set the customer for the cart
    Customer customer = purchase.getCustomer();
    customer.getCarts().add(cart);


    // Save customer and cart information to the database
    customerRepository.save(customer);
    cartRepository.save(cart);

    // Return a response with the order tracking number
    return new PurchaseResponse(orderTrackingNumber);
}catch(Exception e) {
    return new PurchaseResponse(e.getMessage());
}
    }

    private String generateOrderTrackingNumber() {
        // Generates a universally unique identifier (UUID) as the order tracking number
        return UUID.randomUUID().toString();
    }

}