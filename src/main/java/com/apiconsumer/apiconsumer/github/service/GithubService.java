package com.apiconsumer.apiconsumer.github.service;

import com.apiconsumer.apiconsumer.github.client.GithubApiOpenFeign;
import com.apiconsumer.apiconsumer.github.client.GithubApiRestClient;
import com.apiconsumer.apiconsumer.github.client.GithubClient;
import com.apiconsumer.apiconsumer.github.repo.Repo;
import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.response.ResponseBranch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
@Service
public class GithubService {
    private final GithubApiOpenFeign githubApiOpenFeign;
    private final GithubApiRestClient githubApiRestClient;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public List<Response> getUserReposOpenFeign(String username) throws ExecutionException, InterruptedException {
        return getUserRepos(username, githubApiOpenFeign);
    }

    public List<Response> getUserReposRestClient(String username) throws ExecutionException, InterruptedException {
        return getUserRepos(username, githubApiRestClient);
    }

    private List<Response> getUserRepos(String username, GithubClient githubClient) throws ExecutionException, InterruptedException {
        List<Repo> reposByUsername = githubClient.getReposByUsername(username);
        List<CompletableFuture<Response>> futures = reposByUsername.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> CompletableFuture.supplyAsync(() -> new Response(
                        repo.name(),
                        repo.owner().login(),
                        getResponseBranch(repo, githubClient)
                ), executorService))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private List<ResponseBranch> getResponseBranch(Repo repo, GithubClient githubClient) {
        return githubClient.getBranchNameAndSha(repo.owner().login(), repo.name()).stream()
                .map(branch -> new ResponseBranch(branch.name(), branch.commit().sha()))
                .toList();
    }


}
