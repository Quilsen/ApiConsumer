package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.database.RepoDto;
import com.apiconsumer.apiconsumer.github.response.Response;
import com.apiconsumer.apiconsumer.github.service.GithubService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<Response> listUserRepositories(@PathVariable String userName) throws ExecutionException, InterruptedException {
        log.info("There is openFeign request for username: " + userName);
        return githubService.getUserRepositoriesOpenFeign(userName);
    }

    @GetMapping(value = "/restclient/{userName}", headers = "Accept=application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<Response> listUserRepositoriesRestClient(@PathVariable String userName) throws ExecutionException, InterruptedException {
        log.info("There is restClient request for username: " + userName);
        return githubService.getUserReposRestClient(userName);
    }

    @GetMapping("/get/{userName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<RepoDto> getRepoByUserName(@PathVariable String userName) {
        log.info("Request for database entities: " + userName);
        return githubService.getRepoByUsername(userName);
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    RepoDto saveRepo(@RequestBody RepoDto repoDto) {
        log.info("Post request for:" + repoDto);
        return githubService.saveRepo(repoDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRepoById(@PathVariable Long id) {
        log.info("Delete request for repo id: " + id);
        githubService.deleteRepoById(id);
    }

    @PatchMapping("/update/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    RepoDto patchRepo(@PathVariable Long id, @RequestBody RepoDto repoDto){
        log.info("Patch request for id: " + id);
        return githubService.patchRepoById(id, repoDto);
    }

}
