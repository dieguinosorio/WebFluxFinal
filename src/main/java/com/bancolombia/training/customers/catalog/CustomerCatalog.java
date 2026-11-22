package com.bancolombia.training.customers.catalog;

import com.bancolombia.training.customers.model.Customer;
import com.bancolombia.training.customers.model.CustomerRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerCatalog {
    public static final UUID MOCK_CUSTOMER_ID = UUID.fromString("11111111-1111-4111-7859-111111111111");
    private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

    CustomerCatalog() {
        customers.put(MOCK_CUSTOMER_ID, new Customer(MOCK_CUSTOMER_ID, "Diego Osorio", "diego@gmail.com"));
    }

    public Flux<Customer> getAllCustomers() {
        return Flux.fromIterable(customers.values());
    }

    public Mono<Customer> getCustomerById(UUID id) {
        Customer customer = customers.get(id);
        if (customer != null) {
            return Mono.just(customer);
        } else {
            return Mono.error(new CustomerNotFoundException("Customer not found with id: " + id));
        }
    }

    public Mono<Customer> createCustomer(CustomerRequest customer) {
        UUID id = UUID.randomUUID();
        Customer newCustomer = new Customer(id, customer.name(), customer.email());
        customers.put(id, newCustomer);
        return Mono.just(newCustomer);
    }

    public Mono<Void> deleteCustomer(UUID id) {
        Customer existingCustomer = customers.get(id);
        if (existingCustomer != null) {
            customers.remove(id);
            return Mono.empty();
        } else {
            return Mono.error(new CustomerNotFoundException("Customer not found with id: " + id));
        }
    }

    public Mono<Customer> updateCustomer(UUID id, CustomerRequest customer) {
        Customer existingCustomer = customers.get(id);
        if (existingCustomer != null) {
            Customer updatedCustomer = new Customer(id, customer.name(), customer.email());
            customers.put(id, updatedCustomer);
            return Mono.just(updatedCustomer);
        } else {
            return Mono.error(new CustomerNotFoundException("Customer not found with id: " + id));
        }
    }
}
