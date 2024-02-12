package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.CustomResponse;
import com.apiconsumer.apiconsumer.github.GithubService;
import com.apiconsumer.apiconsumer.github.Response;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
class GithubController {
    private final GithubService githubService;

    @GetMapping("/{username}")
    ResponseEntity<?> listUserRepositories(@PathVariable String username){
        try {
            ArrayList<Response> userRepos = githubService.getUserRepos(username);
            return ResponseEntity.ok(userRepos);
        } catch (FeignException e){
            CustomResponse customResponse = new CustomResponse(e.status(), e.getMessage());
            return ResponseEntity.status(e.status()).body(customResponse);
        }
    }

}
