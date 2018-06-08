package guru.springframework.bootstrap;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import guru.springframework.domain.Category;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
@Slf4j
@Component
public class DomainBootstrap implements ApplicationListener<ContextRefreshedEvent> {
	private VendorRepository vendorRepository;
	private CategoryRepository categoryRepository;
	@Autowired
	public DomainBootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
		super();
		this.vendorRepository = vendorRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Profile("dev")
	public void onApplicationEvent(ContextRefreshedEvent event) {
		vendorRepository.count().filter(count -> count > 0).subscribe(count -> {
			
			vendorRepository.deleteAll().block();
			categoryRepository.deleteAll().block();
			
			bootstrap();
		});
	
		
		
		
		
	}
	private void bootstrap() {
		vendorRepository.saveAll(Arrays.asList(
				Vendor.builder()
					.firstName("Apple")
					.lastName("Inc")
					.build(),
				Vendor.builder()
					.firstName("Sony")
					.lastName("Company")
					.build()
			)).blockFirst();
	categoryRepository.saveAll(Arrays.asList(
				Category.builder()
					.description("Notebooks")
					.build(),
				Category.builder()
					.description("Books")
					.build(),
				Category.builder()
					.description("Cats")
					.build()
			)).blockFirst();
	System.out.println(vendorRepository.count().block().toString()+" vendor");
	System.out.println(categoryRepository.count().block().toString()+" category");
	}

	

}
