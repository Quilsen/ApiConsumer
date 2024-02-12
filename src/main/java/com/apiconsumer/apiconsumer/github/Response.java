package com.apiconsumer.apiconsumer.github;

import java.util.List;

public record Response(String repoName, String repoOwnerLogin, List<ResponseBranch> infoBranches) {
}
