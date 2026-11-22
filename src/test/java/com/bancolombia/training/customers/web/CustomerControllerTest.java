package com.bancolombia.training.customers.web;

import com.bancolombia.training.customers.catalog.CustomerCatalog;
import com.bancolombia.training.customers.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.*;

class CustomerControllerTest {
    private final CustomerCatalog catalog = mock(CustomerCatalog.class);
    private final UUID id = UUID.fromString("11111111-1111-4111-7859-111111111111");
    private final Customer customer = new Customer(id, "Customer", "customer@example.test");
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(new CustomerController(catalog)).build();
    }

    @Test
    void listsCustomersWithoutId() {
        when(catalog.getAllCustomers()).thenReturn(Flux.just(customer));

        client.get().uri("/api/customers").exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$[0].id").isEqualTo(id.toString());

        verify(catalog).getAllCustomers();
        verifyNoMoreInteractions(catalog);
    }

    @Test
    void findsCustomerByQueryId() {
        when(catalog.getCustomerById(id)).thenReturn(Mono.just(customer));

        client.get().uri("/api/customers?id=" + id).exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.id").isEqualTo(id.toString());

        verify(catalog).getCustomerById(id);
        verifyNoMoreInteractions(catalog);
    }

    @Test
    void preservesCustomerLookupByPath() {
        when(catalog.getCustomerById(id)).thenReturn(Mono.just(customer));

        client.get().uri("/api/customers/" + id).exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.id").isEqualTo(id.toString());

        verify(catalog).getCustomerById(id);
        verifyNoMoreInteractions(catalog);
    }

    @Test
    void rejectsInvalidQueryId() {
        client.get().uri("/api/customers?id=not-a-uuid").exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(catalog);
    }

    @Test
    void rejectsEmptyQueryId() {
        client.get().uri("/api/customers?id=").exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(catalog);
    }
}
