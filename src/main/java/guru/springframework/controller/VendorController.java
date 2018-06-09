package guru.springframework.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public Mono<Void> createNewVendor(@RequestBody Publisher<Vendor> vendorStream){
		return repository.saveAll(vendorStream).then();
	}
	@PutMapping("{id}")
	public Mono<Vendor> updateVendorbyId(@PathVariable String id, @RequestBody Publisher<Vendor> vendorStream){
		return repository.saveAll(Flux.from(vendorStream)
				.map(vendor -> {
					vendor.setId(id);
					return vendor;
				})).single();
				
	}
	@PatchMapping("{id}")
	public Mono<Vendor> patchVendorById(@PathVariable String id, @RequestBody Vendor vendor){
		Mono<Vendor> vendorSaved = repository.findById(id);
		vendorSaved.map(cat->{
			if(!vendor.getFirstName().equals(cat.getFirstName())) {
				cat.setFirstName(vendor.getFirstName());
			}
			if(!vendor.getLastName().equals(vendor.getLastName())) {
				cat.setLastName(vendor.getLastName());
			}
			return cat;
		});
		return repository.saveAll(vendorSaved).single();
	}
}
