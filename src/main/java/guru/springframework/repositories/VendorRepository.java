package guru.springframework.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import guru.springframework.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {

}
