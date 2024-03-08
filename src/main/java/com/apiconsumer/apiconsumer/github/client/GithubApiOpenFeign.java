package com.apiconsumer.apiconsumer.github.client;

import com.apiconsumer.apiconsumer.github.branch.Branch;
import com.apiconsumer.apiconsumer.github.repository.Repository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
@FeignClient(name = "githubApiOpenFeign",
        url = "https://api.github.com")
public interface GithubApiOpenFeign extends GithubClient {
    @RequestMapping(method = RequestMethod.GET,
            value = "/users/{userName}/repos",
            headers = "Accept: application/json")
    List<Repository> getRepositoriesByUsername(@PathVariable("userName") String userName);

    @RequestMapping(method = RequestMethod.GET,
            value = "/repos/{userName}/{repoName}/branches",
            headers = "Accept: application/json")
    List<Branch> getBranchNameAndSha(@PathVariable("userName") String userName,
                                     @PathVariable("repoName") String repoName);

}
