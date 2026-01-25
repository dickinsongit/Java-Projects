# Design Specification: Hello MCP Server

## 1. Project Overview

### 1.1 Project Name
**SpringBoot Hello MCP Server**

### 1.2 Purpose
A Spring Boot application that implements a Model Context Protocol (MCP) server, providing a demonstration of MCP tool integration with greeting functionality and health monitoring capabilities.

### 1.3 Technology Stack
- **Framework**: Spring Boot 3.x
- **Java Version**: 17+
- **Build Tool**: Maven
- **MCP Integration**: Spring AI MCP Server (v1.1.2)
- **Transport Protocol**: Server-Sent Events (SSE)
- **Logging**: SLF4J with Logback

---

## 2. Architecture

### 2.1 Application Architecture
```
┌─────────────────────────────────────────────────────┐
│              MCP Client (e.g., Claude)              │
└────────────────────┬────────────────────────────────┘
                     │ SSE Protocol
                     ▼
┌─────────────────────────────────────────────────────┐
│          Spring Boot MCP Server (Port 8082)         │
├─────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────┐  │
│  │        Controller Layer                      │  │
│  │  - HealthcheckController (/api/health)       │  │
│  └──────────────────────────────────────────────┘  │
│                        │                            │
│  ┌──────────────────────────────────────────────┐  │
│  │        Service Layer                         │  │
│  │  - HelloMcpService (@McpTool)                │  │
│  └──────────────────────────────────────────────┘  │
│                        │                            │
│  ┌──────────────────────────────────────────────┐  │
│  │     Spring AI MCP Server Infrastructure      │  │
│  │  - Tool Registration & Discovery             │  │
│  │  - Progress Notifications                    │  │
│  │  - Logging Integration                       │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

### 2.2 Package Structure
```
com.example.mcp
├── HelloMcpServerApplication.java      # Main application entry point
├── controller
│   └── HealthcheckController.java      # REST health check endpoint
└── service
    └── HelloMcpService.java            # MCP tool implementation
```

---

## 3. Component Design

### 3.1 Main Application
**Class**: `HelloMcpServerApplication`
- **Type**: Spring Boot Application
- **Responsibility**: Bootstrap the Spring Boot application and MCP server
- **Annotations**: `@SpringBootApplication`

### 3.2 Health Check Controller
**Class**: `HealthcheckController`
- **Endpoint**: `GET /api/health`
- **Purpose**: Provides application health status monitoring
- **Return Values**:
  - `OK` - Application is healthy
  - `DEGRADED` - Application is running but with issues
  - `ERROR` - Application encountered an error
- **Logging**:
  - INFO: Health check requests and results
  - DEBUG: Detailed health verification steps
  - WARN: Degraded health status
  - ERROR: Health check failures with exception details

### 3.3 MCP Tool Service
**Class**: `HelloMcpService`

#### Tool: `hello`
- **Description**: Generate a friendly greeting message with optional personalization
- **Parameters**:
  - `name` (String, optional): The name of the person to greet
  - `progressToken` (String, optional): Token for progress tracking
- **Return**: Formatted greeting message with timestamp
- **Features**:
  - Logging notifications to MCP client
  - Progress tracking (0%, 50%, 100%)
  - Timestamp generation
  - Default greeting to "World" if no name provided

---

## 4. MCP Integration

### 4.1 Transport Protocol
- **Type**: Server-Sent Events (SSE)
- **Configuration**: `spring.ai.mcp.server.transport=sse`
- **Benefits**: 
  - Real-time communication
  - Unidirectional server-to-client streaming
  - Built-in reconnection handling

### 4.2 MCP Features Used

#### Tool Registration
- Uses `@McpTool` annotation for automatic tool discovery
- Tools are exposed to MCP clients for invocation

#### Progress Notifications
- Implements progress tracking using `ProgressNotification`
- Reports completion percentage (0.0 to 1.0)
- Includes status messages at each stage

#### Logging Integration
- Sends structured log messages to MCP client
- Uses `LoggingMessageNotification` with severity levels
- Includes metadata for context

---

## 5. Configuration

### 5.1 Application Properties
```properties
# Application Identity
spring.application.name=hello-mcp

# Server Configuration
server.port=8082

# MCP Server Configuration
spring.ai.mcp.server.transport=sse

# Logging Configuration
logging.level.root=INFO
logging.level.com.example.hello=DEBUG
logging.level.org.springframework=INFO
```

### 5.2 Logging Configuration
- **File**: `logback-spring.xml`
- **Features**:
  - Console logging with colored output
  - File-based logging to `logs/` directory
  - Rolling file appenders for log rotation
  - Structured log format with timestamps

---

## 6. API Endpoints

### 6.1 REST API

| Endpoint | Method | Description | Response Codes |
|----------|--------|-------------|----------------|
| `/api/health` | GET | Health check | 200 OK |

### 6.2 MCP Tools

| Tool | Parameters | Description |
|------|-----------|-------------|
| `hello` | `name` (optional), `progressToken` (optional) | Generates personalized greeting with timestamp |

---

## 7. Data Flow

### 7.1 Hello Tool Execution Flow
```
1. MCP Client → Invoke "hello" tool with parameters
2. HelloMcpService receives request via Spring AI MCP Server
3. Service logs invocation to MCP client (INFO level)
4. Progress: 0% - "Generating greeting"
5. Extract and validate name parameter
6. Generate timestamp
7. Progress: 50% - "Formatting message"
8. Format greeting message
9. Progress: 100% - "Greeting completed"
10. Return formatted greeting to MCP client
```

### 7.2 Health Check Flow
```
1. HTTP Client → GET /api/health
2. HealthcheckController.health() invoked
3. Log INFO: "Health check endpoint called"
4. Log DEBUG: "Performing application health verification"
5. Execute checkApplicationHealth()
6. Return status: OK/DEGRADED/ERROR
7. Log INFO/WARN/ERROR based on result
```

---

## 8. Error Handling

### 8.1 Health Check Error Handling
- **Exception Catching**: All health check exceptions are caught
- **Error Response**: Returns "ERROR" status
- **Logging**: Full exception stack trace logged at ERROR level

### 8.2 MCP Tool Error Handling
- Relies on Spring AI MCP Server framework for error management
- Exceptions propagate to MCP client with structured error responses

---

## 9. Logging Strategy

### 9.1 Log Levels
- **DEBUG**: Detailed operational information (health checks, progress details)
- **INFO**: General informational messages (tool invocations, health status)
- **WARN**: Warning conditions (degraded health)
- **ERROR**: Error events (health check failures, exceptions)

### 9.2 Log Destinations
1. **Console**: Real-time monitoring during development
2. **File**: Persistent logs in `logs/` directory for production

### 9.3 MCP Logging
- Structured notifications sent to MCP client
- Includes severity level and metadata
- Enables client-side log aggregation

---

## 10. Dependencies

### 10.1 Key Maven Dependencies
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
    <version>1.1.2</version>
</dependency>
```

### 10.2 Dependency Management
- Spring AI BOM for version management
- Parent POM: `java-projects-parent` (v0.0.1-SNAPSHOT)

---

## 11. Deployment

### 11.1 Build
```bash
mvn clean package
```

### 11.2 Run
```bash
java -jar target/hello-mcp-server-0.0.1-SNAPSHOT.jar
```

### 11.3 Port Configuration
- **Default Port**: 8082
- **Override**: Set `server.port` in application.properties or via command line

---

## 12. Testing

### 12.1 Unit Tests
- **Test Class**: `HelloMcpServerApplicationTests`
- **Framework**: JUnit 5 with Spring Boot Test
- **Scope**: Context loading and basic application bootstrap

### 12.2 Manual Testing

#### Health Check
```bash
curl http://localhost:8082/api/health
```

#### MCP Tool Testing
- Connect MCP client (e.g., Claude Desktop)
- Invoke `hello` tool via MCP protocol
- Verify greeting response and progress notifications

---

## 13. Future Enhancements

### 13.1 Potential Features
1. **Additional MCP Tools**: Implement more sophisticated tools
2. **Enhanced Health Checks**: Add database, external service checks
3. **Metrics Integration**: Spring Boot Actuator for detailed metrics
4. **Security**: Authentication and authorization for endpoints
5. **Resource Management**: Add prompts and resources to MCP server
6. **Error Recovery**: Implement retry mechanisms and circuit breakers

### 13.2 Scalability Considerations
- Stateless design allows horizontal scaling
- SSE transport supports multiple concurrent clients
- Consider load balancing for high-traffic scenarios

---

## 14. Conventions and Standards

### 14.1 Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Comprehensive JavaDoc for public APIs

### 14.2 Logging Conventions
- Start method names in log messages
- Include relevant context (parameters, state)
- Use appropriate log levels

### 14.3 API Design
- RESTful principles for HTTP endpoints
- Descriptive MCP tool names and parameters
- Clear error messages

---

## 15. Security Considerations

### 15.1 Current Security Posture
- No authentication/authorization implemented
- Suitable for development and trusted environments
- HTTP transport (not HTTPS)

### 15.2 Production Recommendations
1. Enable HTTPS/TLS
2. Implement authentication (OAuth2, API keys)
3. Rate limiting for endpoints
4. Input validation and sanitization
5. Audit logging for security events

---

## 16. Maintenance and Support

### 16.1 Logging and Monitoring
- Monitor health check endpoint
- Review application logs in `logs/` directory
- Track MCP tool invocation patterns

### 16.2 Troubleshooting
- Check application logs for errors
- Verify MCP client configuration
- Validate network connectivity on port 8082
- Review Spring AI MCP Server documentation

---

## Document Information
- **Created**: January 25, 2026
- **Version**: 1.0
- **Last Updated**: January 25, 2026
- **Author**: System Generated
