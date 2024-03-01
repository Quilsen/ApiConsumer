package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.service.GithubService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api")
class GithubController {
    private final GithubService githubService;

    @GetMapping(value = "/openfeign/{username}",headers = "Accept=application/json")
    ResponseEntity<List<Response>> listUserRepositories(@PathVariable String username) throws ExecutionException, InterruptedException {
        log.info("There is openFeign request for username: " + username);
        List<Response> userRepos = githubService.getUserReposOpenFeign(username);
        return ResponseEntity.ok(userRepos);
    }

    @GetMapping(value = "/restclient/{username}", headers =  "Accept=application/json")
    ResponseEntity<List<Response>> listUserRepositoriesRestClient(@PathVariable String username) throws ExecutionException, InterruptedException {
        log.info("There is restClient request for username: " + username);
        List<Response> userRepos = githubService.getUserReposRestClient(username);
        return ResponseEntity.ok(userRepos);
    }
}
