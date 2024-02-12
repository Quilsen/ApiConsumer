package com.apiconsumer.apiconsumer.github;

import com.apiconsumer.apiconsumer.github.repo.Repo;
import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.response.ResponseBranch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GithubService {
    private final GithubApiClient githubApiClient;

    public ArrayList<Response> getUserRepos(String username) {
        List<Repo> reposByUsername = githubApiClient.getReposByUsername(username);
        return reposByUsername.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new Response(
                                repo.name(),
                                repo.owner().login(),
                                getResponseBranch(repo))
                )
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<ResponseBranch> getResponseBranch(Repo repo) {
        return githubApiClient.getBranchNameAndSha(repo.owner().login(), repo.name()).stream()
                .map(branch -> new ResponseBranch(branch.name(), branch.commit().sha()))
                .collect(Collectors.toList());
    }

}
