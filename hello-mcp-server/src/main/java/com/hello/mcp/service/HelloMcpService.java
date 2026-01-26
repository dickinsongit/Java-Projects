package com.hello.mcp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ProgressNotification;
import org.springaicommunity.mcp.annotation.McpProgressToken;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import org.springframework.stereotype.Service;

@Service
public class HelloMcpService {

	@McpTool(description = "Generate a friendly greeting message with optional personalization")
	public String hello(McpSyncServerExchange exchange,
					   @McpToolParam(description = "The name of the person to greet (optional)") String name,
					   @McpProgressToken String progressToken) 
	{

		exchange.loggingNotification(LoggingMessageNotification.builder()
			.level(LoggingLevel.INFO)
			.data("Called hello tool with name: " + (name != null ? name : "World"))
			.meta(Map.of())
			.build());

		// 0% progress
		if (progressToken != null) {
			exchange.progressNotification(new ProgressNotification(progressToken, 0.0, 1.0, "Generating greeting"));
		}

		String greetingName = (name != null && !name.trim().isEmpty()) ? name : "World";
		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// 50% progress
		if (progressToken != null) {
			exchange.progressNotification(new ProgressNotification(progressToken, 0.5, 1.0, "Formatting message"));
		}

		String greeting = String.format("Hello, %s! Welcome to the MCP Hello Server.\nTimestamp: %s", greetingName, timestamp);

		// 100% progress
		if (progressToken != null) {
			exchange.progressNotification(new ProgressNotification(progressToken, 1.0, 1.0, "Greeting completed"));
		}

		exchange.loggingNotification(LoggingMessageNotification.builder()
			.level(LoggingLevel.DEBUG)
			.data("Successfully generated greeting for: " + greetingName)
			.meta(Map.of())
			.build());

		return greeting;
	}

	@McpTool(description = "Get a custom greeting with a specific greeting type (hello, hi, hey, greetings)")
	public String customGreeting(McpSyncServerExchange exchange,
								@McpToolParam(description = "The name of the person to greet") String name,
								@McpToolParam(description = "The type of greeting (hello, hi, hey, greetings)") String greetingType) 
	{

		exchange.loggingNotification(LoggingMessageNotification.builder()
			.level(LoggingLevel.INFO)
			.data("Called customGreeting tool with name: " + name + " and greetingType: " + greetingType)
			.meta(Map.of())
			.build());

		String greeting = switch (greetingType != null ? greetingType.toLowerCase() : "hello") {
			case "hi" -> "Hi there, " + name + "!";
			case "hey" -> "Hey " + name + "! What's up?";
			case "greetings" -> "Greetings, " + name + "! How do you do?";
			default -> "Hello, " + name + "!";
		};

		return greeting;
	}

}

