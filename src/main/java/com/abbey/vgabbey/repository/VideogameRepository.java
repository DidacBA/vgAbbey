package com.abbey.vgabbey.repository;

import com.abbey.vgabbey.domain.Videogame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Videogame entity.
 */
@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Long> {

    @Query(value = "select distinct videogame from Videogame videogame left join fetch videogame.developers",
        countQuery = "select count(distinct videogame) from Videogame videogame")
    Page<Videogame> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct videogame from Videogame videogame left join fetch videogame.developers")
    List<Videogame> findAllWithEagerRelationships();

    @Query("select videogame from Videogame videogame left join fetch videogame.developers where videogame.id =:id")
    Optional<Videogame> findOneWithEagerRelationships(@Param("id") Long id);

}
