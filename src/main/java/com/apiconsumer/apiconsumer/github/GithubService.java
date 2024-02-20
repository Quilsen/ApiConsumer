package com.apiconsumer.apiconsumer.github;

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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GithubService {
    private final GithubApiClient githubApiClient;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public List<Response> getUserRepos(String username) throws ExecutionException, InterruptedException {
        List<Repo> reposByUsername = githubApiClient.getReposByUsername(username);
        List<CompletableFuture<Response>> futures = reposByUsername.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> CompletableFuture.supplyAsync(() -> new Response(
                        repo.name(),
                        repo.owner().login(),
                        getResponseBranch(repo)
                ), executorService))
                .toList();


        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private List<ResponseBranch> getResponseBranch(Repo repo) {
        return githubApiClient.getBranchNameAndSha(repo.owner().login(), repo.name()).stream()
                .map(branch -> new ResponseBranch(branch.name(), branch.commit().sha()))
                .toList();
    }
}
