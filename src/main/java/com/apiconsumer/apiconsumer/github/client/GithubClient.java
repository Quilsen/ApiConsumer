package com.apiconsumer.apiconsumer.github.client;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repository.Repository;

import java.util.List;

public interface GithubClient {
    List<Repository> getReposByUsername(String userName);
    List<Branch> getBranchNameAndSha(String userName, String repoName);
}
