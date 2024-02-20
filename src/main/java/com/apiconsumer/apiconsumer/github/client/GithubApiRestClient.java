package com.apiconsumer.apiconsumer.github.client;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repo.Repo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GithubApiRestClient implements GithubClient{
    private RestClient restClient = RestClient.create("https://api.github.com");

    public List<Repo> getReposByUsername(String username){
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Branch> getBranchNameAndSha(String username, String reponame){
        return restClient.get()
                .uri("/repos/{username}/{reponame}/branches", username, reponame)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

}
