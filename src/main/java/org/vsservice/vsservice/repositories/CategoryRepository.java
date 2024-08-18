package org.vsservice.vsservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vsservice.vsservice.models.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

}
