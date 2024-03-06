package com.apiconsumer.apiconsumer.github.database;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RepoRepository extends CrudRepository<Repo, Long> {
    Optional<Repo> findAllByOwnerNameAndRepoName(String ownerName, String repoName);
}
