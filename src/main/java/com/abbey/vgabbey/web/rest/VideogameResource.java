package com.abbey.vgabbey.web.rest;

import com.abbey.vgabbey.domain.Videogame;
import com.abbey.vgabbey.repository.VideogameRepository;
import com.abbey.vgabbey.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.abbey.vgabbey.domain.Videogame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VideogameResource {

    private final Logger log = LoggerFactory.getLogger(VideogameResource.class);

    private static final String ENTITY_NAME = "videogame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideogameRepository videogameRepository;

    public VideogameResource(VideogameRepository videogameRepository) {
        this.videogameRepository = videogameRepository;
    }

    /**
     * {@code POST  /videogames} : Create a new videogame.
     *
     * @param videogame the videogame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videogame, or with status {@code 400 (Bad Request)} if the videogame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/videogames")
    public ResponseEntity<Videogame> createVideogame(@Valid @RequestBody Videogame videogame) throws URISyntaxException {
        log.debug("REST request to save Videogame : {}", videogame);
        if (videogame.getId() != null) {
            throw new BadRequestAlertException("A new videogame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Videogame result = videogameRepository.save(videogame);
        return ResponseEntity.created(new URI("/api/videogames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /videogames} : Updates an existing videogame.
     *
     * @param videogame the videogame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videogame,
     * or with status {@code 400 (Bad Request)} if the videogame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videogame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/videogames")
    public ResponseEntity<Videogame> updateVideogame(@Valid @RequestBody Videogame videogame) throws URISyntaxException {
        log.debug("REST request to update Videogame : {}", videogame);
        if (videogame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Videogame result = videogameRepository.save(videogame);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videogame.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /videogames} : get all the videogames.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videogames in body.
     */
    @GetMapping("/videogames")
    public ResponseEntity<List<Videogame>> getAllVideogames(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Videogames");
        Page<Videogame> page;
        if (eagerload) {
            page = videogameRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = videogameRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /videogames/:id} : get the "id" videogame.
     *
     * @param id the id of the videogame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videogame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/videogames/{id}")
    public ResponseEntity<Videogame> getVideogame(@PathVariable Long id) {
        log.debug("REST request to get Videogame : {}", id);
        Optional<Videogame> videogame = videogameRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(videogame);
    }

    /**
     * {@code DELETE  /videogames/:id} : delete the "id" videogame.
     *
     * @param id the id of the videogame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/videogames/{id}")
    public ResponseEntity<Void> deleteVideogame(@PathVariable Long id) {
        log.debug("REST request to delete Videogame : {}", id);
        videogameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
