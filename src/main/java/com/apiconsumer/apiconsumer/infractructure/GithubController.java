package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.database.RepoDto;
import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.service.GithubService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api")
class GithubController {
    private final GithubService githubService;

    @GetMapping(value = "/openfeign/{userName}", headers = "Accept=application/json")
    ResponseEntity<List<Response>> listUserRepositories(@PathVariable String userName) throws ExecutionException, InterruptedException {
        log.info("There is openFeign request for username: " + userName);
        List<Response> userRepos = githubService.getUserReposOpenFeign(userName);
        return ResponseEntity.ok(userRepos);
    }

    @GetMapping(value = "/restclient/{userName}", headers = "Accept=application/json")
    ResponseEntity<List<Response>> listUserRepositoriesRestClient(@PathVariable String userName) throws ExecutionException, InterruptedException {
        log.info("There is restClient request for username: " + userName);
        List<Response> userRepos = githubService.getUserReposRestClient(userName);
        return ResponseEntity.ok(userRepos);
    }

    @GetMapping("/{userName}")
    ResponseEntity<List<RepoDto>> getRepoByUserName(@PathVariable String userName) {
        log.info("Request for database entities: " + userName);
        List<RepoDto> repoDtoList = githubService.getRepoByUsername(userName);
        return ResponseEntity.ok(repoDtoList);
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    RepoDto saveRepo(@RequestBody RepoDto repoDto) {
        return githubService.saveRepo(repoDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRepoById(@PathVariable Long id) {
        githubService.deleteRepoById(id);
    }
}
