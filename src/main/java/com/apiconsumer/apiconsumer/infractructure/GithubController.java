package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.GithubService;
import com.apiconsumer.apiconsumer.github.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
class GithubController {
    private final GithubService githubService;

    @GetMapping("/openfeign/{username}")
    ResponseEntity<List<Response>> listUserRepositories(@PathVariable String username) throws ExecutionException, InterruptedException {
        List<Response> userRepos = githubService.getUserReposOpenFeign(username);
        return ResponseEntity.ok(userRepos);
    }

    @GetMapping("/restclient/{username}")
    ResponseEntity<List<Response>> listUserRepositoriesRestClient(@PathVariable String username) throws ExecutionException, InterruptedException {
        List<Response> userRepos = githubService.getUserReposRestClient(username);
        return ResponseEntity.ok(userRepos);
    }
}
