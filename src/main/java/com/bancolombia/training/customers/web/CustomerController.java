package com.bancolombia.training.customers.web;

import com.bancolombia.training.customers.catalog.CustomerCatalog;
import com.bancolombia.training.customers.model.Customer;
import com.bancolombia.training.customers.model.CustomerRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerCatalog customerCatalog;
    CustomerController(CustomerCatalog customerCatalog) {
        this.customerCatalog = customerCatalog;
    }

    @GetMapping()
    public Flux<Customer> getAllCustomers() {
        return customerCatalog.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable("id") UUID id) {
        return customerCatalog.getCustomerById(id);
    }

    @PostMapping()
    public Mono<Customer> createCustomer(@RequestBody Mono<CustomerRequest> request) {
        return request.flatMap(customerCatalog::createCustomer);
    }

    @PutMapping("/{id}")
    public Mono<Customer> updateCustomer(@PathVariable("id") UUID id, @RequestBody Mono<CustomerRequest> request) {
        return request.flatMap(customer -> customerCatalog.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") UUID id) {
        return customerCatalog.deleteCustomer(id);
    }
}
