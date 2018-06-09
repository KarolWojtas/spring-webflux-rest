package guru.springframework.controller;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

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
	@PutMapping("{id}")
	Mono<Category> updateCategory(@RequestBody Publisher<Category> categoryStream, @PathVariable String id){
		return categoryRepository.saveAll(Mono.from(categoryStream).map(category -> {
			category.setId(id);
			return category;
		})).single();
		
	}
	@PatchMapping("{id}")
	Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category){
		 Mono<Category> categoryStored = categoryRepository.findById(id);
		return categoryRepository.saveAll(categoryStored.map(cat -> {
			if(!category.getDescription().equals(null) || 
					cat.getDescription().equals(category.getDescription())) {
				cat.setDescription(category.getDescription());
			}
			return cat;
		})).single();
		 //return Mono.just(Category.builder().description("Dupa").build());
	}

}
