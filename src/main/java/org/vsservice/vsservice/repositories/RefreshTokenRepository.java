package org.vsservice.vsservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vsservice.vsservice.models.security.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String > {
    Optional<RefreshToken> findByToken(String token);

    int deleteByUsername(String username);
}
