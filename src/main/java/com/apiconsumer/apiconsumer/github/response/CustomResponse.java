package com.apiconsumer.apiconsumer.github.response;

public record CustomResponse(int status, String message) {
    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
