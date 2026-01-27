# Architecture Documentation MCP Service - Design Specification

## Overview

The **Architecture Documentation MCP Service** is a Model Context Protocol (MCP) server designed to provide comprehensive microservices architecture guidance and documentation tools that can be leveraged by development teams across their organization.

This service exposes 12 MCP tools that enable teams to:
- Document and discover microservices
- Access best practices and design patterns
- Implement deployment and security strategies
- Troubleshoot issues and debug problems
- Maintain team standards and consistency

**Primary Use Case:** Integration with AI editors (Claude, GitHub Copilot, etc.) to provide context-aware architecture guidance during development.

---

## Architecture & Design

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    AI Editor (Integrated)                   │
│              (Claude, GitHub Copilot, etc.)                 │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  │ MCP Protocol
                  │
┌─────────────────▼───────────────────────────────────────────┐
│          ArchitectureDocService (MCP Server)                │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Core Documentation Tools (12 tools)                 │  │
│  │  • getMicroserviceRegistry()                        │  │
│  │  • getServiceDependencies()                         │  │
│  │  • getApiContracts()                                │  │
│  │  • getDeploymentPatterns()                          │  │
│  │  • getDatabasePatterns()                            │  │
│  │  • getSecurityPatterns()                            │  │
│  │  • getObservabilityPatterns()                       │  │
│  │  • getDesignPatterns()                              │  │
│  │  • getTestingStrategy()                             │  │
│  │  • getTroubleshootingGuide()                        │  │
│  │  • getTeamStandards()                               │  │
│  │  • listAvailableTools()                             │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  Framework: Spring Boot 4.0.2                               │
│  Protocol: MCP Server (WebMVC)                              │
│  Language: Java 17+                                         │
└──────────────────────────────────────────────────────────────┘
```

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 4.0.2 |
| **AI Integration** | Spring AI | 1.1.2 |
| **Protocol** | MCP Server | WebMVC variant |
| **Language** | Java | 17+ (LTS) |
| **Build Tool** | Maven | 3.x |
| **Logging** | SLF4J + Logback | Latest |
| **Web Server** | Embedded Tomcat | Latest from Spring Boot |

### Design Principles

1. **Stateless Documentation**: All tools return formatted documentation without maintaining internal state
2. **Team-Centric**: Designed for organization-wide adoption across all microservices
3. **Consistent Format**: All tools return structured, readable text documentation
4. **Logging & Monitoring**: Every tool invocation is logged for audit and debugging
5. **Extensibility**: Easy to add new tools or update existing documentation
6. **No External Dependencies**: Works independently without requiring external databases or APIs

---

## MCP Tools Specification

### 1. getMicroserviceRegistry()

**Purpose**: Get complete registry of all team microservices with endpoints and metadata

**Return Format**: Structured text with service information

**Example Output**:
```
=== Team Microservices Registry ===
Last Updated: 2025-01-26 14:32:15

SERVICE DISCOVERY:
Each microservice should register itself with:
  • Service Name
  • Service Version
  • Base URL/Endpoint
  • Primary Port
  • Health Check Endpoint
  • Owner/Team

EXAMPLE SERVICES:
┌─ USER-SERVICE
│  Version: 2.1.0
│  Endpoint: http://user-service:8081
│  Health: GET /actuator/health
│  Owner: Platform Team
...
```

**Use Case**: When developers need to find available services and their contact information

---

### 2. getServiceDependencies(serviceName: String)

**Purpose**: Get service dependency graph and communication patterns

**Parameters**:
- `serviceName`: Service name (or "all" for complete graph)

**Return Format**: Dependency graph with communication patterns

**Supports Patterns**:
- Request-Reply (Synchronous REST/gRPC)
- Publish-Subscribe (Asynchronous messaging)
- Event-Driven architecture

**Use Case**: Understanding how microservices interact and their coupling

---

### 3. getApiContracts(serviceName: String)

**Purpose**: Get API contract templates and request/response schemas

**Parameters**:
- `serviceName`: Service name to get contracts for

**Returns**:
- Endpoint naming conventions
- Request/response format templates
- HTTP status codes and meanings
- Header requirements
- Authentication patterns

**Use Case**: Implementing consistent APIs across the organization

---

### 4. getDeploymentPatterns()

**Purpose**: Get deployment strategies, infrastructure, and CI/CD patterns

**Covers**:
- **Containerization**: Docker best practices
- **Orchestration**: Kubernetes and Helm
- **Environment Configuration**: Dev, Staging, Production setups
- **CI/CD Pipeline**: Build, package, push, deploy, verify stages
- **Scaling**: Horizontal, vertical, auto-scaling strategies
- **Load Balancing**: Service mesh and Ingress patterns

**Use Case**: Setting up consistent deployment pipelines across services

---

### 5. getDatabasePatterns()

**Purpose**: Get database design patterns and data management strategies

**Includes**:
- **Database-Per-Service Pattern**: Ownership and isolation
- **Database Choices**:
  - Relational (PostgreSQL, MySQL)
  - NoSQL (MongoDB, DynamoDB)
  - Cache (Redis)
  - Message Queue (RabbitMQ, Kafka)
- **Migration Strategy**: Flyway, Liquibase, zero-downtime deployments
- **Data Consistency**: Strong vs Eventual consistency
- **Event Sourcing & CQRS**: Alternative architectures

**Use Case**: Making database technology decisions and ensuring data consistency

---

### 6. getSecurityPatterns()

**Purpose**: Get security best practices and authentication/authorization patterns

**Covers**:
- **Authentication**: OAuth 2.0, OpenID Connect, JWT, API Keys, mTLS
- **Authorization**: RBAC, ABAC, fine-grained permissions
- **API Gateway**: Central security enforcement
- **Secrets Management**: Vault, Cloud KMS, environment variables
- **Data Protection**: Encryption at rest/transit, PII masking
- **Security Scanning**: SAST, DAST, dependency scanning, container scanning

**Use Case**: Implementing secure microservices architecture

---

### 7. getObservabilityPatterns()

**Purpose**: Get monitoring, logging, and observability strategy

**Components**:
- **Logging**: ELK stack, centralized logging, JSON format, correlation IDs
- **Metrics**: Prometheus, Grafana, key performance indicators
- **Tracing**: Jaeger/Zipkin, OpenTelemetry, distributed tracing
- **Alerting**: Alert thresholds, notification channels, escalation
- **Health Checks**: Liveness, readiness, startup probes

**Use Case**: Implementing full observability across microservices

---

### 8. getDesignPatterns()

**Purpose**: Get microservice design patterns and anti-patterns

**Communication Patterns**:
- Request-Reply (sync)
- Publish-Subscribe (async)
- Request-Callback

**Transaction Patterns**:
- Saga Pattern (orchestrated and choreography)
- Two-Phase Commit
- Eventual Consistency

**Resilience Patterns**:
- Circuit Breaker
- Retry with Exponential Backoff
- Timeout
- Bulkhead isolation
- Rate Limiting

**Anti-Patterns to Avoid**:
- Shared databases
- Chatty services
- Synchronous call chains
- Lack of monitoring

**Use Case**: Making architectural decisions and avoiding common pitfalls

---

### 9. getTestingStrategy()

**Purpose**: Get comprehensive testing strategies and test patterns

**Test Pyramid**:
```
        /\
       /  \ E2E Tests (10%)
      /    \ Slow, Brittle
     /______\
    /        \
   /  Integration Tests (30%)
  /   With real services
 /____________\
/              \
Unit Tests (60%)
Fast, Reliable
\______________/
```

**Test Types**:
- **Unit Tests**: JUnit 5, Mockito, 70%+ coverage target
- **Integration Tests**: TestContainers, embedded databases
- **Contract Tests**: Pact, consumer-driven contracts
- **Load Testing**: JMeter, Gatling, P99 latency targets
- **E2E Tests**: Cypress/Selenium, critical flows only

**Use Case**: Establishing testing standards and coverage goals

---

### 10. getTroubleshootingGuide()

**Purpose**: Get debugging and incident response guidelines

**Common Issues Covered**:
- Service timeouts and debugging steps
- High memory usage and heap analysis
- Cascading failures and circuit breakers
- Database connection failures
- Debugging tools (Jaeger, Kibana, Grafana)

**Incident Response Process**:
1. Assess severity and scope
2. Notify on-call engineer
3. Mitigate (scale, circuit break)
4. Investigate (logs, metrics, traces)
5. Fix (deploy patch or rollback)
6. Verify (monitor for regression)
7. Document (post-mortem)

**Use Case**: Quick reference during incidents and troubleshooting

---

### 11. getTeamStandards()

**Purpose**: Get team best practices, coding standards, and guidelines

**Standards Include**:
- **Code Quality**: Java 17+, Spring Boot 4.0+, SonarQube grade A, 70%+ coverage
- **Version Control**: Git workflow, atomic commits, PR reviews
- **Naming Conventions**: Package, class, method, constant naming
- **Commit Conventions**: feat, fix, refactor, test, docs, chore
- **API Design**: REST principles, versioning, pagination, filtering
- **Documentation**: README, OpenAPI, ADRs, inline comments, runbooks

**Use Case**: Ensuring consistency across all team microservices

---

### 12. listAvailableTools()

**Purpose**: List all available MCP tools with descriptions

**Returns**: Quick reference of all 12 tools and their purposes

**Use Case**: Onboarding new team members and tool discovery

---

## Integration Guide

### Prerequisites

- Java 17 or higher
- Spring Boot 4.0.2
- Maven 3.x
- Spring AI 1.1.2 or compatible

### Setup Instructions

1. **Clone/Add MCP Server to your project**:
   ```bash
   git clone <repo-url> arch-mcp-server
   cd arch-mcp-server
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Start the server**:
   ```bash
   mvn spring-boot:run
   ```
   
   Server starts on: `http://localhost:8080`

### Integration with AI Editor

#### Claude in VS Code (Claude Extension)

1. Install the Claude VS Code extension
2. Configure MCP server in settings:
   ```json
   {
     "mcpServers": {
       "arch-doc": {
         "command": "java",
         "args": ["-jar", "/path/to/arch-mcp-server.jar"],
         "env": {
           "SPRING_PROFILES_ACTIVE": "prod"
         }
       }
     }
   }
   ```

3. Restart VS Code and test in the chat

#### GitHub Copilot with MCP Support

1. Configure Copilot settings to include MCP servers
2. Point to running instance: `http://localhost:8080`
3. Tools will be available in Copilot chat

### Example Usage in AI Editor

**User Query**:
```
I'm starting a new microservice for payment processing. 
What should I consider for deployment, security, and monitoring?
```

**MCP Tools Called**:
- `getDeploymentPatterns()` - Deployment strategy
- `getSecurityPatterns()` - Security implementation
- `getObservabilityPatterns()` - Monitoring setup
- `getTeamStandards()` - Team conventions

---

## Implementation Details

### Core Components

#### ArchitectureDocService (Main Service)

**Location**: `src/main/java/com/arch/doc/mcp/service/ArchitectureDocService.java`

**Key Features**:
- 12 MCP tools with @McpTool annotations
- Structured text formatting for readability
- SLF4J logging for all tool invocations
- Zero external dependencies

**Tool Anatomy**:
```java
@McpTool(description = "Tool description for AI editors")
public String getToolName(
    @McpToolParam(description = "Parameter description") String param) {
    logger.info("Tool invoked with param: {}", param);
    
    StringBuilder output = new StringBuilder();
    output.append("=== Section Title ===\n");
    // Structured output
    return output.toString();
}
```

#### HealthcheckController

**Location**: `src/main/java/com/arch/doc/mcp/controller/HealthcheckController.java`

**Endpoints**:
- `GET /api/health` - Health status check

### Logging Configuration

**File**: `src/main/resources/logback-spring.xml`

**Features**:
- SLF4J with Logback
- Console and file output
- Structured logging ready

**Key Logging Points**:
- Tool invocation start
- Tool completion
- Error conditions

---

## Extension Points

### Adding New Tools

1. Add new method to `ArchitectureDocService`:
   ```java
   @McpTool(description = "Your tool description")
   public String getNewTool(
       @McpToolParam(description = "param desc") String param) {
       logger.info("New tool called");
       
       StringBuilder output = new StringBuilder();
       // Build output
       return output.toString();
   }
   ```

2. Update `listAvailableTools()` to include new tool

3. Test and deploy

### Updating Documentation

Tools return static documentation. To update:

1. Edit the relevant tool method
2. Update the StringBuilder output
3. Rebuild and deploy

To make documentation dynamic (e.g., from a database or API):

1. Inject a `ConfigService` or `DocumentationRepository`
2. Fetch documentation at runtime
3. Format and return

---

## Performance Considerations

### Tool Response Time

- **Average Response Time**: < 100ms (all tools are CPU-bound)
- **Memory Usage**: Minimal (all data is computed, not cached)
- **Concurrent Users**: Designed for stateless, horizontal scaling

### Optimization Opportunities

1. **Caching**: Add @Cacheable for static content (if database added)
2. **Pagination**: Break large responses into chunks
3. **Lazy Loading**: Load documentation on-demand from files

---

## Security Considerations

### Authentication

- **Current**: No authentication (internal MCP protocol handles it)
- **Future**: Add Spring Security for API endpoints if exposed

### Authorization

- **Current**: All tools available to all users
- **Future**: Add role-based tool access via @PreAuthorize

### Data Protection

- **Sensitive Data**: No PII handled (documentation only)
- **Transport**: MCP protocol handles encryption
- **Logging**: Safe to log tool invocations (no secrets exposed)

---

## Testing Strategy

### Unit Tests

**Location**: `src/test/java/com/arch/doc/mcp/ArchMcpServerApplicationTests.java`

**Coverage Goals**: 70%+

**Test Cases**:
- Tool method returns non-null, non-empty strings
- Logging is called appropriately
- Parameter handling (null, empty, invalid)

### Integration Tests

- Service layer integration with Spring context
- MCP protocol message handling

### Manual Testing

```bash
# Start server
mvn spring-boot:run

# Test via curl (if HTTP endpoint exposed)
curl http://localhost:8080/api/health

# Test via MCP client
# Use AI editor or MCP testing client
```

---

## Deployment

### Local Development

```bash
mvn spring-boot:run
```

### Docker Deployment

```dockerfile
FROM openjdk:17-slim
COPY target/arch-mcp-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t arch-mcp-server .
docker run -p 8080:8080 arch-mcp-server
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: arch-mcp-server
spec:
  replicas: 2
  selector:
    matchLabels:
      app: arch-mcp-server
  template:
    metadata:
      labels:
        app: arch-mcp-server
    spec:
      containers:
      - name: arch-mcp-server
        image: arch-mcp-server:latest
        ports:
        - containerPort: 8080
        livenessProbe:
          httpGet:
            path: /api/health
            port: 8080
          initialDelaySeconds: 30
```

---

## Maintenance & Support

### Version Management

- **Current Version**: 0.0.1-SNAPSHOT
- **Release Cycle**: Semantic versioning (Major.Minor.Patch)

### Updates & Changes

To update documentation:

1. **Modify** the relevant tool method
2. **Test** changes locally
3. **Rebuild**: `mvn clean install`
4. **Deploy** to production
5. **Notify** team of changes

### Troubleshooting

**Issue**: Tool not appearing in AI editor
- Check: MCP server running, configuration correct, network connectivity

**Issue**: Stale documentation
- Solution: Rebuild and redeploy service

**Issue**: Performance degradation
- Check: Memory usage, concurrent connections, CPU usage

---

## Future Enhancements

1. **Database-backed Configuration**: Store microservice registry in database
2. **Dynamic Documentation**: Load from Markdown files or API
3. **Real-time Metrics**: Query actual service health and status
4. **Customization**: Per-team documentation overrides
5. **Multi-language Support**: Translate documentation for global teams
6. **Interactive Validation**: Validate service configurations against patterns
7. **Integration with CI/CD**: Pull real deployment info
8. **GraphQL Support**: Alternative query interface

---

## Conclusion

The Architecture Documentation MCP Service provides a comprehensive, team-wide resource for microservices development guidance. By integrating with AI editors, it brings architecture knowledge directly into developers' workflows, improving consistency, reducing decision time, and enabling better architectural decisions across the organization.

For questions or contributions, contact the Platform Architecture team.
