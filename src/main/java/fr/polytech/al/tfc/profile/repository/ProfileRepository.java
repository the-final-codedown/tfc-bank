package fr.polytech.al.tfc.profile.repository;

import fr.polytech.al.tfc.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, Long> {

    Profile findProfileByEmail(@PathVariable(value = "email") String email);
}
