package com.apiconsumer.apiconsumer.github.database;

import org.springframework.stereotype.Service;

@Service
public class RepoDtoMapper {

    public Repo map(RepoDto repoDto){
        return new Repo(repoDto.getOwnerName(), repoDto.getRepoName());
    }

    public RepoDto map(Repo repo){
        return new RepoDto(repo.getOwnerName(), repo.getRepoName());
    }
}
