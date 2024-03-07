package com.apiconsumer.apiconsumer.github.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RepoDto {
    private String ownerName;
    private String repoName;
}
