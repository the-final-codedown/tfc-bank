package fr.polytech.al.tfc.profile.repository;

import fr.polytech.al.tfc.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {

    Profile findProfileByEmail(String email);
}
