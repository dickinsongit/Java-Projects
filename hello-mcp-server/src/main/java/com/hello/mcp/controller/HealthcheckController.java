package com.hello.mcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthcheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthcheckController.class);

    @GetMapping("/health")
    public String health() {
        logger.info("Health check endpoint called");
        logger.debug("Performing application health verification");
        
        try {
            // Simulate health check logic
            boolean isHealthy = checkApplicationHealth();
            
            if (isHealthy) {
                logger.info("Application health check passed");
                return "OK";
            } else {
                logger.warn("Application health check failed");
                return "DEGRADED";
            }
        } catch (Exception e) {
            logger.error("Health check failed with exception", e);
            return "ERROR";
        }
    }

    private boolean checkApplicationHealth() {
        logger.debug("Checking application health status");
        // Add your health check logic here
        return true;
    }
}
