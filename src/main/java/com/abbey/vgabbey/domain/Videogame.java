package com.abbey.vgabbey.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Videogame.
 */
@Entity
@Table(name = "videogame")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Videogame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Size(min = 5, max = 256)
    @Column(name = "description", length = 256)
    private String description;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @NotNull
    @Size(max = 256)
    @Column(name = "repository_url", length = 256, nullable = false)
    private String repositoryUrl;

    @Size(max = 256)
    @Column(name = "deploy_url", length = 256)
    private String deployUrl;

    @Column(name = "release_date")
    private ZonedDateTime releaseDate;

    @NotNull
    @Column(name = "is_searching_collab", nullable = false)
    private Boolean isSearchingCollab;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "videogame_developers",
               joinColumns = @JoinColumn(name = "videogame_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "developers_id", referencedColumnName = "id"))
    private Set<User> developers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Videogame title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Videogame description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Videogame imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public Videogame repositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        return this;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getDeployUrl() {
        return deployUrl;
    }

    public Videogame deployUrl(String deployUrl) {
        this.deployUrl = deployUrl;
        return this;
    }

    public void setDeployUrl(String deployUrl) {
        this.deployUrl = deployUrl;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public Videogame releaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isIsSearchingCollab() {
        return isSearchingCollab;
    }

    public Videogame isSearchingCollab(Boolean isSearchingCollab) {
        this.isSearchingCollab = isSearchingCollab;
        return this;
    }

    public void setIsSearchingCollab(Boolean isSearchingCollab) {
        this.isSearchingCollab = isSearchingCollab;
    }

    public Set<User> getDevelopers() {
        return developers;
    }

    public Videogame developers(Set<User> users) {
        this.developers = users;
        return this;
    }

    public Videogame addDevelopers(User user) {
        this.developers.add(user);
        return this;
    }

    public Videogame removeDevelopers(User user) {
        this.developers.remove(user);
        return this;
    }

    public void setDevelopers(Set<User> users) {
        this.developers = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Videogame)) {
            return false;
        }
        return id != null && id.equals(((Videogame) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Videogame{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", repositoryUrl='" + getRepositoryUrl() + "'" +
            ", deployUrl='" + getDeployUrl() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", isSearchingCollab='" + isIsSearchingCollab() + "'" +
            "}";
    }
}
