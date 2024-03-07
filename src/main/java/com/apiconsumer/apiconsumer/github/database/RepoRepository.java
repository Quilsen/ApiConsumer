package com.apiconsumer.apiconsumer.github.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RepoRepository extends CrudRepository<Repo, Long> {
    List<Repo> findAllByOwnerNameAndRepoName(String ownerName, String repoName);

    List<Repo> findAllByOwnerName(String ownerName);
}
