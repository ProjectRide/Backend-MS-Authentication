package de.projectride.authentication.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.projectride.authentication.domain.UserExtension;
import de.projectride.authentication.service.UserExtensionService;
import de.projectride.authentication.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserExtension.
 */
@RestController
@RequestMapping("/api")
public class UserExtensionResource {

    private final Logger log = LoggerFactory.getLogger(UserExtensionResource.class);
        
    @Inject
    private UserExtensionService userExtensionService;

    /**
     * POST  /user-extensions : Create a new userExtension.
     *
     * @param userExtension the userExtension to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userExtension, or with status 400 (Bad Request) if the userExtension has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-extensions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtension> createUserExtension(@Valid @RequestBody UserExtension userExtension) throws URISyntaxException {
        log.debug("REST request to save UserExtension : {}", userExtension);
        if (userExtension.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userExtension", "idexists", "A new userExtension cannot already have an ID")).body(null);
        }
        UserExtension result = userExtensionService.save(userExtension);
        return ResponseEntity.created(new URI("/api/user-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userExtension", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-extensions : Updates an existing userExtension.
     *
     * @param userExtension the userExtension to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userExtension,
     * or with status 400 (Bad Request) if the userExtension is not valid,
     * or with status 500 (Internal Server Error) if the userExtension couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-extensions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtension> updateUserExtension(@Valid @RequestBody UserExtension userExtension) throws URISyntaxException {
        log.debug("REST request to update UserExtension : {}", userExtension);
        if (userExtension.getId() == null) {
            return createUserExtension(userExtension);
        }
        UserExtension result = userExtensionService.save(userExtension);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userExtension", userExtension.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-extensions : get all the userExtensions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userExtensions in body
     */
    @RequestMapping(value = "/user-extensions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserExtension> getAllUserExtensions() {
        log.debug("REST request to get all UserExtensions");
        return userExtensionService.findAll();
    }

    /**
     * GET  /user-extensions/:id : get the "id" userExtension.
     *
     * @param id the id of the userExtension to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userExtension, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-extensions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtension> getUserExtension(@PathVariable Long id) {
        log.debug("REST request to get UserExtension : {}", id);
        UserExtension userExtension = userExtensionService.findOne(id);
        return Optional.ofNullable(userExtension)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-extensions/:id : delete the "id" userExtension.
     *
     * @param id the id of the userExtension to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-extensions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserExtension(@PathVariable Long id) {
        log.debug("REST request to delete UserExtension : {}", id);
        userExtensionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userExtension", id.toString())).build();
    }

}
