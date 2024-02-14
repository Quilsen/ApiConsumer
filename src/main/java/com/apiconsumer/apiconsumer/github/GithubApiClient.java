package com.apiconsumer.apiconsumer.github;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repo.Repo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
@FeignClient(name = "githubClient",
        url = "https://api.github.com")
public interface GithubApiClient {
    @RequestMapping(method = RequestMethod.GET,
            value = "/users/{username}/repos",
            headers = "Accept: application/json")
    List<Repo> getReposByUsername(@PathVariable("username") String username);

    @RequestMapping(method = RequestMethod.GET,
            value = "/repos/{username}/{reponame}/branches",
            headers = "Accept: application/json")
    List<Branch> getBranchNameAndSha(@PathVariable("username") String username,
                                     @PathVariable("reponame") String reponame);

}
