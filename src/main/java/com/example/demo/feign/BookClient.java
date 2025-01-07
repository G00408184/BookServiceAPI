package com.example.demo.feign;

import com.example.demo.entity.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "loan-service", url = "http://localhost:8083/api/loans")
public interface BookClient {

    @GetMapping("/findByTitleAndAuthor")
    List<Loan> getLoanByTitleAndAuthor(
            @RequestParam("title") String title,
            @RequestParam("author") String author);
}
