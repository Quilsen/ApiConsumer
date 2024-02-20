package com.apiconsumer.apiconsumer.github;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repo.Repo;

import java.util.List;

interface GithubClient {
    List<Repo> getReposByUsername(String username);
    List<Branch> getBranchNameAndSha(String username, String reponame);
}
