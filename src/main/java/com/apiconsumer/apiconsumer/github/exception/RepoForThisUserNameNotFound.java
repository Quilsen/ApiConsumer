package com.apiconsumer.apiconsumer.github.exception;

public class RepoForThisUserNameNotFound extends RuntimeException {
    public RepoForThisUserNameNotFound(String message) {
        super("Repo for this username not found: " + message);
    }
}
