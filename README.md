# GitHub API Client Application

This is a simple Spring Boot application that uses the Feign client to interact with the GitHub API. The application retrieves a list of repositories and their branches for a given GitHub user.

## Setup

Clone the repository:

```
git clone https://github.com/Quilsen/ApiConsumer
```
## Usage

Build and run the application using your IDE or the following command:

```
./mvnw spring-boot:run
```

The application will start on http://localhost:8080.

## Endpoints

### List User Repositories
* Endpoint: /api/{username}
* Method: GET
* Description: Retrieve a list of repositories for the specified GitHub user.
* Example Request: GET http://localhost:8080/api/octocat
* Example Response:
```json
[
    {
        "repoName": "example-repo",
        "ownerName": "octocat",
        "branches": [
            {
                "branchName": "main",
                "sha": "1234567890abcdef"
            },
            {
                "branchName": "feature-branch",
                "sha": "abcdef1234567890"
            }
        ]
    },
    // Additional repositories...
]
```

## Exception Handling

In case of errors, the application handles exceptions thrown by the Feign client. If the GitHub API returns an error, a custom response is provided with the HTTP status code and error message.

Example error response:
```json
{
    "status": 404,
    "message": "User not found"
}
```
