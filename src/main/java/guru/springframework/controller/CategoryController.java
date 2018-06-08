package guru.springframework.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {

	private final CategoryRepository categoryRepository;
	@Autowired
	public CategoryController(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}
	@GetMapping
	public Flux<Category> getAllCategories(){
		return categoryRepository.findAll();
	}
	@GetMapping("{id}")
	Mono<Category> findById(@PathVariable String id){
		return categoryRepository.findById(id);
	}
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	Mono<Void> create(@RequestBody Publisher<Category> categoryStream){
		return categoryRepository.saveAll(categoryStream).then();
	}
}
