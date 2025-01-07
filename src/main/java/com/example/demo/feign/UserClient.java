package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/user")
public interface UserClient {

    // Example: record that a user has returned a book
    @PostMapping("/api/users/{userId}/history")
    void recordReturn(
            @PathVariable("userId") Long userId,
            @RequestParam("loanId") Long loanId
    );

    // If admin checks happen here:
    @GetMapping("/api/users/{userId}/isAdmin")
    boolean checkAdmin(@PathVariable("userId") Long userId);

    @GetMapping("/Check/User/{id}")
    boolean checkUserByID(@PathVariable("id") long id);
}
