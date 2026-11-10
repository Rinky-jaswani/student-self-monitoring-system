package com.studyspark.controller;

import com.studyspark.dto.DashboardResponse;
import com.studyspark.service.DashboardService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/me")
    public DashboardResponse getDashboard(Principal principal) {
        return dashboardService.getDashboard(
                principal.getName()
        );
    }
}