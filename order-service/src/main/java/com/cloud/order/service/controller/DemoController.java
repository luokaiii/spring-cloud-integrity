package com.cloud.order.service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    @PreAuthorize("hasAuthority('query-demo')")
    public String queryDemo() {
        return "good";
    }
}
