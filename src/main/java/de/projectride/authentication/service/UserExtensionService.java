package de.projectride.authentication.service;

import de.projectride.authentication.domain.UserExtension;
import de.projectride.authentication.repository.UserExtensionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing UserExtension.
 */
@Service
@Transactional
public class UserExtensionService {

    private final Logger log = LoggerFactory.getLogger(UserExtensionService.class);
    
    @Inject
    private UserExtensionRepository userExtensionRepository;

    /**
     * Save a userExtension.
     *
     * @param userExtension the entity to save
     * @return the persisted entity
     */
    public UserExtension save(UserExtension userExtension) {
        log.debug("Request to save UserExtension : {}", userExtension);
        UserExtension result = userExtensionRepository.save(userExtension);
        return result;
    }

    /**
     *  Get all the userExtensions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UserExtension> findAll() {
        log.debug("Request to get all UserExtensions");
        List<UserExtension> result = userExtensionRepository.findAll();

        return result;
    }

    /**
     *  Get one userExtension by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UserExtension findOne(Long id) {
        log.debug("Request to get UserExtension : {}", id);
        UserExtension userExtension = userExtensionRepository.findOne(id);
        return userExtension;
    }

    /**
     *  Delete the  userExtension by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtension : {}", id);
        userExtensionRepository.delete(id);
    }
}
