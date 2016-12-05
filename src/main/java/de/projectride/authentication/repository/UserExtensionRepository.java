package de.projectride.authentication.repository;

import de.projectride.authentication.domain.UserExtension;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserExtension entity.
 */
@SuppressWarnings("unused")
public interface UserExtensionRepository extends JpaRepository<UserExtension,Long> {

}
