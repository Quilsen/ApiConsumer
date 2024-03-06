package com.apiconsumer.apiconsumer.github.client;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repository.Repository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GithubApiRestClient implements GithubClient {
    private final RestClient restClient = RestClient.create("https://api.github.com");

    public List<Repository> getReposByUsername(String userName) {
        return restClient.get()
                .uri("/users/{userName}/repos", userName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Branch> getBranchNameAndSha(String userName, String repoName) {
        return restClient.get()
                .uri("/repos/{userName}/{repoName}/branches", userName, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
