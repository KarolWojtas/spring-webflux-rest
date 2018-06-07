package guru.springframework.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import guru.springframework.domain.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String>{

}
