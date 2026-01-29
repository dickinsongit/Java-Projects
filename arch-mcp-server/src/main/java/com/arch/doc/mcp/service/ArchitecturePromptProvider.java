package com.arch.doc.mcp.service;

import java.util.List;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;

@Component
public class ArchitecturePromptProvider {

    @McpPrompt(
        name = "design-spring-microservice", 
        description = "Guides the AI to design a Spring Boot microservice following internal standards."
    )
    public GetPromptResult designMicroservice(
        @McpArg(name = "serviceName", description = "The name of the service (e.g., order-service)") String serviceName,
        @McpArg(name = "features", description = "List of features needed") String features) 
    {
        String template = """
            You are a Senior Spring Boot Architect. 
            Design a microservice named: %s.
            
            Core Requirements:
            - Features to include: %s
            - Use Spring Cloud Stream for messaging.
            - Use R2DBC for reactive persistence.
            - Ensure all controllers return ProblemDetail for errors.
            
            Please provide:
            1. A high-level architecture diagram (Mermaid).
            2. The Maven pom.xml dependencies.
            3. A sample Reactive Controller implementation.
            """.formatted(serviceName, features);

        return new GetPromptResult(
            "Guided Microservice Design",
            List.of(new PromptMessage(Role.USER, new TextContent(template)))
        );
    }

    /**
     * Autocomplete handler for the 'techStack' argument of the 'new-service-blueprint' prompt.
     */
    @McpComplete(prompt = "design-spring-microservice")
    public List<String> completeTechStack(@McpToolParam(description  = "techStack") String prefix) {
        List<String> approvedStacks = List.of("Spring-WebFlux-R2DBC", "Spring-MVC-JPA", "Kotlin-Micronaut");
        
        return approvedStacks.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }
}