package com.example.demo.bootstrap;

import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.DivisionRepository;
import com.example.demo.entities.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public BootStrapData(CustomerRepository customerRepository, DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create customers with Futurama character names
        createCustomer("Philip", "Fry", "1 Robot Lane", "555-1234", "54321", 1L);
        createCustomer("Turanga", "Leela", "22 Universe Street", "555-5678", "98765", 2L);
        createCustomer("Bender", "Rodriguez", "3 Steel Avenue", "555-8765", "12345", 3L);
        createCustomer("Hubert", "Farnsworth", "40 Inventor Boulevard", "555-4321", "67890", 4L);
        createCustomer("Amy", "Wong", "5 Mars Street", "555-7890", "23456", 5L);
    }

    private void createCustomer(String firstName, String lastName, String address, String phone, String postalCode, Long divisionId) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setPostal_code(postalCode);
        customer.setDivision(divisionRepository.getReferenceById(divisionId));
        customerRepository.save(customer);
    }
}