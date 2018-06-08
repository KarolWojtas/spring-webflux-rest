package guru.springframework.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

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

}
