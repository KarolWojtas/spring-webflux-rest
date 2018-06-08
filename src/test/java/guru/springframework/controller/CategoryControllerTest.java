package guru.springframework.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CategoryControllerTest {
	private WebTestClient client;
	private CategoryRepository repository;
	private CategoryController controller;
	@Before
	public void setUp() throws Exception {
		repository = Mockito.mock(CategoryRepository.class);
		controller = new CategoryController(repository);
		client = WebTestClient.bindToController(controller).build();
	}

	@Test
	public void testGetAllCategories() {
		BDDMockito.given(repository.findAll()).willReturn(Flux.just(
					Category.builder().description("Book").build(),
					Category.builder().description("Apple").build()
				));
		
		client.get().uri("/api/category/").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectBodyList(Category.class)
				.hasSize(2);
	}

	@Test
	public void testFindById() {
		BDDMockito.given(repository.findById(Mockito.anyString()))
			.willReturn(Mono.just(Category.builder().description("Any").build()));
		client.get().uri("/api/category/1")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectBody(Category.class)
					.consumeWith(response -> {
						assertEquals("Any", response.getResponseBody().getDescription());
					});
	}
	@Test
	public void testCreate() {
		Category body = Category.builder().description("deski").build();
		BDDMockito.given(repository.saveAll(Mockito.any(Publisher.class)))
			.willReturn(Flux.just(Category.builder().build()));
		
		client.post().uri("/api/category/")
						.contentType(MediaType.APPLICATION_JSON)
						.body(Mono.just(body), Category.class)
						.exchange()
							.expectStatus().isCreated();
						
	}
	@Test
	public void updateCategory() {
		BDDMockito.given(repository.saveAll(Mockito.any(Publisher.class))).willReturn(Flux.just(new Category()));
		
		client.put().uri("/api/category/1")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(new Category()), Category.class)
			.exchange()
				.expectStatus().isOk()
				.expectBody(Category.class);
				
			
	}

}
