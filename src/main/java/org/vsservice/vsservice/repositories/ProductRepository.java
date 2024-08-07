package org.vsservice.vsservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vsservice.vsservice.models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
