package fr.polytech.al.tfc.profile.repository;

import fr.polytech.al.tfc.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {

    Optional<Profile> findProfileByEmail(String email);
}
