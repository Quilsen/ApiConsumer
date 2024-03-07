package com.apiconsumer.apiconsumer.github.exception;

public class RepoForThisIdNotFound extends RuntimeException{
    public RepoForThisIdNotFound(Long id) {
        super("Repo for this id not found: " + id);
    }
}
