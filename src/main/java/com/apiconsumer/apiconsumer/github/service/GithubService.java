package com.apiconsumer.apiconsumer.github.service;

import com.apiconsumer.apiconsumer.github.client.GithubApiOpenFeign;
import com.apiconsumer.apiconsumer.github.client.GithubApiRestClient;
import com.apiconsumer.apiconsumer.github.client.GithubClient;
import com.apiconsumer.apiconsumer.github.database.Repo;
import com.apiconsumer.apiconsumer.github.database.RepoRepository;
import com.apiconsumer.apiconsumer.github.repository.Repository;
import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.response.ResponseBranch;
import lombok.AllArgsConstructor;
import lombok.val;
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
    private final RepoRepository repoRepository;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public List<Response> getUserReposOpenFeign(String userName) throws ExecutionException, InterruptedException {
        return getUserRepos(userName, githubApiOpenFeign);
    }

    public List<Response> getUserReposRestClient(String userName) throws ExecutionException, InterruptedException {
        return getUserRepos(userName, githubApiRestClient);
    }

    private List<Response> getUserRepos(String userName, GithubClient githubClient) throws ExecutionException, InterruptedException {
        List<Repository> reposByUsername = githubClient.getReposByUsername(userName);
        List<CompletableFuture<Response>> futures = reposByUsername.stream()
                .filter(repository -> !repository.fork())
                .map(repository -> CompletableFuture.supplyAsync(() -> new Response(
                        repository.name(),
                        repository.owner().login(),
                        getResponseBranch(repository, githubClient)
                ), executorService))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        List<Response> responseList = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        saveRepoEntity(responseList);

        return responseList;
    }

    private List<ResponseBranch> getResponseBranch(Repository repository, GithubClient githubClient) {
        return githubClient.getBranchNameAndSha(repository.owner().login(), repository.name()).stream()
                .map(branch -> new ResponseBranch(branch.name(), branch.commit().sha()))
                .toList();
    }

    private void saveRepoEntity(List<Response> responseList){
        responseList.stream()
                .map(response -> new Repo(response.repoOwnerLogin(), response.repoName()))
                .filter(repo -> !repoRepository.findAllByOwnerNameAndRepoName(repo.getOwnerName(), repo.getRepoName()).isPresent())
                .forEach(repoRepository::save);

    }


}
