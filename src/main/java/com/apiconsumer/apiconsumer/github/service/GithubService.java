package com.apiconsumer.apiconsumer.github.service;

import com.apiconsumer.apiconsumer.github.client.GithubApiOpenFeign;
import com.apiconsumer.apiconsumer.github.client.GithubApiRestClient;
import com.apiconsumer.apiconsumer.github.client.GithubClient;
import com.apiconsumer.apiconsumer.github.database.Repo;
import com.apiconsumer.apiconsumer.github.database.RepoDto;
import com.apiconsumer.apiconsumer.github.database.RepoDtoMapper;
import com.apiconsumer.apiconsumer.github.database.RepoRepository;
import com.apiconsumer.apiconsumer.github.exception.RepoForThisIdNotFound;
import com.apiconsumer.apiconsumer.github.exception.RepoForThisUserNameNotFound;
import com.apiconsumer.apiconsumer.github.repository.Repository;
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
    private final RepoRepository repoRepository;
    private final RepoDtoMapper repoDtoMapper;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public List<Response> getUserRepositoriesOpenFeign(String userName) throws ExecutionException, InterruptedException {
        return getUserRepositories(userName, githubApiOpenFeign);
    }

    public List<Response> getUserReposRestClient(String userName) throws ExecutionException, InterruptedException {
        return getUserRepositories(userName, githubApiRestClient);
    }

    public List<RepoDto> getRepoByUsername(String userName) {
        List<Repo> allByOwnerName = repoRepository.findAllByOwnerName(userName);
        if (allByOwnerName.isEmpty()) {
            throw new RepoForThisUserNameNotFound(userName);
        }
        return allByOwnerName.stream()
                .map(repoDtoMapper::map)
                .toList();
    }

    public RepoDto saveRepo(RepoDto repoDto) {
        Repo repo = repoDtoMapper.map(repoDto);
        Repo savedRepo = repoRepository.save(repo);
        return repoDtoMapper.map(savedRepo);
    }

    public void deleteRepoById(Long id) {
        Repo repo = repoRepository.findById(id)
                .orElseThrow(() -> new RepoForThisIdNotFound(id));
        repoRepository.delete(repo);
    }

    public RepoDto patchRepoById(Long id, RepoDto repoDto) {
        Repo repo = repoRepository.findById(id)
                .orElseThrow(() -> new RepoForThisIdNotFound(id));
        if (repoDto.getOwnerName() != null) {
            repo.setOwnerName(repoDto.getOwnerName());
        }
        if (repoDto.getRepoName() != null) {
            repo.setRepoName(repoDto.getRepoName());
        }
        Repo saved = repoRepository.save(repo);
        return repoDtoMapper.map(saved);
    }

    private List<Response> getUserRepositories(String userName, GithubClient githubClient) throws ExecutionException, InterruptedException {
        List<Repository> reposByUsername = githubClient.getRepositoriesByUsername(userName);
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

    private void saveRepoEntity(List<Response> responseList) {
        responseList.stream()
                .map(response -> new Repo(response.repoOwnerLogin(), response.repoName()))
                .filter(repo -> repoRepository.findAllByOwnerNameAndRepoName(repo.getOwnerName(), repo.getRepoName()).isEmpty())
                .forEach(repoRepository::save);

    }

}
