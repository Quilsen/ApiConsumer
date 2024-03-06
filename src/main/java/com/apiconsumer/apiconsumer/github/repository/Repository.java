package com.apiconsumer.apiconsumer.github.repository;

public record Repository(String name, RepositoryOwner owner, boolean fork) {
}
