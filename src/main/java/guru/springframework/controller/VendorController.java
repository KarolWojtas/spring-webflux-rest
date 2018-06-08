package guru.springframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vendor/")
public class VendorController {
	private VendorRepository repository;
	@Autowired
	public VendorController(VendorRepository repository) {
		super();
		this.repository = repository;
	}
	@GetMapping
	public Flux<Vendor> getAllVendors() {
		return repository.findAll();
	}
	@GetMapping("{id}")
	public Mono<Vendor> findVendorbyId(@PathVariable String id){
		return repository.findById(id);
	}
}
