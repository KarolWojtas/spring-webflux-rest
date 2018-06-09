package guru.springframework.controller;


import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


import guru.springframework.domain.Category;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {
	WebTestClient testClient;
	VendorRepository repository;
	VendorController controller;
	@Before
	public void setUp() throws Exception {
		repository = Mockito.mock(VendorRepository.class);
		controller = new VendorController(repository);
		testClient = WebTestClient.bindToController(controller).build();
	}

	@Test
	public void testGetAllVendors() {
		BDDMockito.given(repository.findAll())
			.willReturn(Flux.just(
					Vendor.builder().firstName("Microsoft").build(),
					Vendor.builder().firstName("Sony").build()));
		testClient.get().uri("/api/vendor/")
						.accept(MediaType.APPLICATION_JSON)
					.exchange()
						.expectBodyList(Vendor.class)
						.hasSize(2);
	}

	@Test
	public void testFindVendorbyId() {
		BDDMockito.given(repository.findById(Mockito.anyString()))
			.willReturn(Mono.just(new Vendor()));
		
		testClient.get().uri("/api/vendor/1")
						.accept(MediaType.APPLICATION_JSON)
					.exchange()
						.expectBody(Vendor.class)
						.consumeWith(response -> {
							assertEquals(new Vendor(), response.getResponseBody());
						});
	}
	@Test
	public void createNewvendor() {
		BDDMockito.given(repository.saveAll(Mockito.any(Publisher.class)))
			.willReturn(Flux.empty());
		
		testClient.post().uri("/api/vendor/").contentType(MediaType.APPLICATION_JSON)
							.body(Mono.just(new Vendor()), Vendor.class)
							.exchange()
								.expectStatus().isCreated();
					
		
	}
	@Test
	public void updateCategory() {
		BDDMockito.given(repository.saveAll(Mockito.any(Publisher.class))).willReturn(Flux.just(new Vendor()));
		
		testClient.put().uri("/api/vendor/1")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(new Vendor()), Vendor.class)
			.exchange()
				.expectStatus().isOk()
				.expectBody(Vendor.class);
				
			
	}
	@Test
	public void testPatch() {

		BDDMockito.given(repository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        BDDMockito.given(repository.save(Mockito.any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jimmy").lastName("!").build());

        testClient.patch()
                .uri("/api/vendor/1")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository, times(1)).save(Mockito.any());

	}

}
