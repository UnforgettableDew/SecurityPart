package com.unforgettable.securitypart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "github", url = "http://localhost:8081/github")
@Component
public interface GithubFeign {
    @GetMapping("/user/{username}/repo/{repositoryName}/all-commits")
    List<Object> getAllCommits(@PathVariable String username,
                               @PathVariable String repositoryName);

    @GetMapping("/user/{username}/repo/{repositoryName}/files")
    List<Object> getFiles(@PathVariable String username,
                          @PathVariable String repositoryName);
}
