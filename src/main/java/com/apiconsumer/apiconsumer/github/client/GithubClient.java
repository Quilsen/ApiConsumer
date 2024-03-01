package com.apiconsumer.apiconsumer.github.client;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repo.Repo;

import java.util.List;

public interface GithubClient {
    List<Repo> getReposByUsername(String userName);
    List<Branch> getBranchNameAndSha(String userName, String repoName);
}
