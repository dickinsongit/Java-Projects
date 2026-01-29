package com.arch.doc.mcp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import org.springframework.stereotype.Service;

/**
 * MCP Service for Microservices Architecture Documentation
 * Provides tools for teams to document, discover, and manage microservices
 * Designed for team-wide usage across multiple microservices environments
 * Integrates with AI editors for context-aware development assistance
 */
@Service
public class ArchitectureDocumentProvider {

	private static final Logger logger = LoggerFactory.getLogger(ArchitectureDocumentProvider.class);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Get microservice registry with all team services
	 */
	@McpTool(description = "Get complete registry of all team microservices with endpoints and responsibilities")
	public String getMicroserviceRegistry() {
		logger.info("Microservice registry requested");
		
		StringBuilder registry = new StringBuilder();
		registry.append("=== Team Microservices Registry ===\n");
		registry.append("Last Updated: ").append(LocalDateTime.now().format(DATE_FORMATTER)).append("\n\n");
		
		registry.append("SERVICE DISCOVERY:\n");
		registry.append("Each microservice should register itself with the following information:\n");
		registry.append("  • Service Name (e.g., user-service, order-service)\n");
		registry.append("  • Service Version (semantic versioning)\n");
		registry.append("  • Base URL/Endpoint\n");
		registry.append("  • Primary Port\n");
		registry.append("  • Health Check Endpoint\n");
		registry.append("  • Owner/Team\n\n");
		
		registry.append("EXAMPLE SERVICES:\n");
		registry.append("┌─ USER-SERVICE\n");
		registry.append("│  Version: 2.1.0\n");
		registry.append("│  Endpoint: http://user-service:8081\n");
		registry.append("│  Health: GET /actuator/health\n");
		registry.append("│  Owner: Platform Team\n");
		registry.append("│\n");
		registry.append("├─ ORDER-SERVICE\n");
		registry.append("│  Version: 1.5.2\n");
		registry.append("│  Endpoint: http://order-service:8082\n");
		registry.append("│  Health: GET /actuator/health\n");
		registry.append("│  Owner: Commerce Team\n");
		registry.append("│\n");
		registry.append("├─ PAYMENT-SERVICE\n");
		registry.append("│  Version: 3.0.1\n");
		registry.append("│  Endpoint: http://payment-service:8083\n");
		registry.append("│  Health: GET /actuator/health\n");
		registry.append("│  Owner: Payments Team\n");
		registry.append("│\n");
		registry.append("└─ NOTIFICATION-SERVICE\n");
		registry.append("   Version: 1.2.0\n");
		registry.append("   Endpoint: http://notification-service:8084\n");
		registry.append("   Health: GET /actuator/health\n");
		registry.append("   Owner: Platform Team\n");
		
		return registry.toString();
	}

	/**
	 * Document service dependencies and communication patterns
	 */
	@McpTool(description = "Get service dependency graph and communication patterns between microservices")
	public String getServiceDependencies(
			@McpToolParam(description = "Service name or 'all' for complete graph") String serviceName) {
		logger.info("Service dependencies requested for: {}", serviceName);
		
		StringBuilder deps = new StringBuilder();
		deps.append("=== Microservice Dependencies ===\n\n");
		
		if ("all".equalsIgnoreCase(serviceName)) {
			deps.append("COMPLETE DEPENDENCY GRAPH:\n\n");
			deps.append("order-service → payment-service\n");
			deps.append("             → notification-service\n");
			deps.append("             → user-service\n\n");
			
			deps.append("payment-service → notification-service\n");
			deps.append("               → user-service\n\n");
			
			deps.append("notification-service (no downstream dependencies)\n\n");
			
			deps.append("user-service (no downstream dependencies)\n\n");
		} else {
			deps.append("SERVICE: ").append(serviceName).append("\n\n");
			deps.append("DEPENDS ON:\n");
			deps.append("  • Communication Type: REST/gRPC/Message Queue\n");
			deps.append("  • Retry Policy: Exponential backoff\n");
			deps.append("  • Timeout: 5000ms\n");
			deps.append("  • Circuit Breaker: Enabled\n\n");
		}
		
		deps.append("COMMUNICATION PATTERNS:\n");
		deps.append("1. SYNCHRONOUS (REST/gRPC)\n");
		deps.append("   Used for: Real-time queries, immediate responses\n");
		deps.append("   Example: order-service calls user-service for validation\n\n");
		
		deps.append("2. ASYNCHRONOUS (Message Queue)\n");
		deps.append("   Used for: Event notifications, decoupling services\n");
		deps.append("   Example: payment-service publishes PaymentProcessed event\n\n");
		
		deps.append("3. EVENTUAL CONSISTENCY\n");
		deps.append("   Used for: Distributed transactions\n");
		deps.append("   Example: Saga pattern for order fulfillment\n");
		
		return deps.toString();
	}

	/**
	 * Get API contract documentation template
	 */
	@McpTool(description = "Get API contract documentation and request/response schemas")
	public String getApiContracts(
			@McpToolParam(description = "Service name to get API contracts for") String serviceName) {
		logger.info("API contracts requested for service: {}", serviceName);
		
		StringBuilder contracts = new StringBuilder();
		contracts.append("=== API Contracts for ").append(serviceName).append(" ===\n\n");
		
		contracts.append("ENDPOINT TEMPLATE:\n");
		contracts.append("METHOD: [GET|POST|PUT|DELETE|PATCH]\n");
		contracts.append("PATH: /api/v1/{resource}/{id}\n");
		contracts.append("BASE_URL: http://").append(serviceName).append(":PORT\n\n");
		
		contracts.append("REQUEST FORMAT:\n");
		contracts.append("{\n");
		contracts.append("  \"headers\": {\n");
		contracts.append("    \"Content-Type\": \"application/json\",\n");
		contracts.append("    \"Authorization\": \"Bearer {token}\",\n");
		contracts.append("    \"X-Request-ID\": \"uuid\"\n");
		contracts.append("  },\n");
		contracts.append("  \"body\": { /* payload */ }\n");
		contracts.append("}\n\n");
		
		contracts.append("RESPONSE FORMAT:\n");
		contracts.append("{\n");
		contracts.append("  \"status\": \"success|error\",\n");
		contracts.append("  \"data\": { /* response data */ },\n");
		contracts.append("  \"errors\": [ /* error details */ ],\n");
		contracts.append("  \"timestamp\": \"ISO-8601 date\"\n");
		contracts.append("}\n\n");
		
		contracts.append("COMMON HTTP STATUS CODES:\n");
		contracts.append("200: OK\n");
		contracts.append("201: Created\n");
		contracts.append("400: Bad Request\n");
		contracts.append("401: Unauthorized\n");
		contracts.append("403: Forbidden\n");
		contracts.append("404: Not Found\n");
		contracts.append("409: Conflict\n");
		contracts.append("500: Internal Server Error\n");
		contracts.append("503: Service Unavailable\n");
		
		return contracts.toString();
	}

	/**
	 * Get microservice deployment patterns and configurations
	 */
	@McpTool(description = "Get deployment patterns, infrastructure setup, and environment configurations for microservices")
	public String getDeploymentPatterns() {
		logger.info("Deployment patterns requested");
		
		StringBuilder patterns = new StringBuilder();
		patterns.append("=== Microservice Deployment Patterns ===\n\n");
		
		patterns.append("CONTAINERIZATION:\n");
		patterns.append("✓ Use Docker for all services\n");
		patterns.append("✓ Dockerfile location: src/main/docker/Dockerfile\n");
		patterns.append("✓ Base image: openjdk:17-slim\n");
		patterns.append("✓ Multi-stage builds for optimization\n");
		patterns.append("✓ Security: Scan images with Trivy\n\n");
		
		patterns.append("ORCHESTRATION:\n");
		patterns.append("✓ Kubernetes for production\n");
		patterns.append("✓ Docker Compose for local development\n");
		patterns.append("✓ Helm charts for deployment\n");
		patterns.append("✓ Service mesh: Istio (optional)\n\n");
		
		patterns.append("ENVIRONMENT CONFIGURATION:\n");
		patterns.append("├── Development: Local environment with Docker Compose\n");
		patterns.append("├── Staging: Kubernetes cluster\n");
		patterns.append("├── Production: Kubernetes with HA setup\n");
		patterns.append("└── Disaster Recovery: Multi-region setup\n\n");
		
		patterns.append("CI/CD PIPELINE:\n");
		patterns.append("1. Build: Maven/Gradle compile and test\n");
		patterns.append("2. Package: Docker image creation\n");
		patterns.append("3. Push: Registry (ECR/Docker Hub/Harbor)\n");
		patterns.append("4. Deploy: Kubernetes apply/helm upgrade\n");
		patterns.append("5. Verify: Smoke tests and health checks\n\n");
		
		patterns.append("SCALING:\n");
		patterns.append("├── Horizontal: Replicas via Kubernetes\n");
		patterns.append("├── Vertical: Resource requests/limits\n");
		patterns.append("├── Auto-scaling: HPA based on CPU/Memory\n");
		patterns.append("└── Load Balancing: Service mesh or Ingress\n");
		
		return patterns.toString();
	}

	/**
	 * Get data storage and database patterns
	 */
	@McpTool(description = "Get database patterns, schemas, and data management strategies for microservices")
	public String getDatabasePatterns() {
		logger.info("Database patterns requested");
		
		StringBuilder db = new StringBuilder();
		db.append("=== Database Patterns for Microservices ===\n\n");
		
		db.append("DATABASE PER SERVICE PATTERN:\n");
		db.append("✓ Each service owns its database\n");
		db.append("✓ No shared databases between services\n");
		db.append("✓ Service-to-service: API calls or events\n\n");
		
		db.append("DATABASE CHOICES:\n");
		db.append("1. RELATIONAL (PostgreSQL, MySQL)\n");
		db.append("   Use For: ACID transactions, complex queries\n");
		db.append("   Services: user-service, order-service\n\n");
		
		db.append("2. NoSQL (MongoDB, DynamoDB)\n");
		db.append("   Use For: Flexible schema, high throughput\n");
		db.append("   Services: notification-service, logs\n\n");
		
		db.append("3. CACHE (Redis)\n");
		db.append("   Use For: Session data, caching, real-time features\n");
		db.append("   Services: All services (shared)\n\n");
		
		db.append("4. MESSAGE QUEUE (RabbitMQ, Kafka)\n");
		db.append("   Use For: Async communication, event streaming\n");
		db.append("   Services: All services (shared)\n\n");
		
		db.append("MIGRATION STRATEGY:\n");
		db.append("├── Version control: Flyway or Liquibase\n");
		db.append("├── Zero-downtime: Blue-green deployments\n");
		db.append("├── Backwards compatibility: New code supports old data\n");
		db.append("└── Rollback plan: Always prepare for reversal\n\n");
		
		db.append("DATA CONSISTENCY:\n");
		db.append("├── Strong consistency: Single service transactions\n");
		db.append("├── Eventual consistency: Distributed transactions (Saga pattern)\n");
		db.append("├── Event sourcing: Immutable event log\n");
		db.append("└── CQRS: Separate read/write models\n");
		
		return db.toString();
	}

	/**
	 * Get security and authentication patterns
	 */
	@McpTool(description = "Get security best practices, authentication, and authorization patterns")
	public String getSecurityPatterns() {
		logger.info("Security patterns requested");
		
		StringBuilder security = new StringBuilder();
		security.append("=== Security Patterns for Microservices ===\n\n");
		
		security.append("AUTHENTICATION:\n");
		security.append("✓ OAuth 2.0 / OpenID Connect\n");
		security.append("✓ JWT (JSON Web Tokens)\n");
		security.append("✓ API Keys for service-to-service\n");
		security.append("✓ mTLS (mutual TLS) for inter-service communication\n\n");
		
		security.append("AUTHORIZATION:\n");
		security.append("✓ Role-Based Access Control (RBAC)\n");
		security.append("✓ Attribute-Based Access Control (ABAC)\n");
		security.append("✓ Token validation at API Gateway\n");
		security.append("✓ Fine-grained permissions per endpoint\n\n");
		
		security.append("API GATEWAY:\n");
		security.append("├── Authentication enforcement\n");
		security.append("├── Rate limiting\n");
		security.append("├── Request logging\n");
		security.append("├── CORS management\n");
		security.append("└── SSL/TLS termination\n\n");
		
		security.append("SECRETS MANAGEMENT:\n");
		security.append("├── Vault (HashiCorp Vault)\n");
		security.append("├── Cloud KMS (AWS/Azure/GCP)\n");
		security.append("├── Environment variables (non-sensitive)\n");
		security.append("└── Never commit secrets to Git\n\n");
		
		security.append("DATA PROTECTION:\n");
		security.append("├── Encryption at rest\n");
		security.append("├── Encryption in transit (TLS 1.3+)\n");
		security.append("├── PII masking in logs\n");
		security.append("└── Data retention policies\n\n");
		
		security.append("SECURITY SCANNING:\n");
		security.append("├── SAST: SonarQube, Checkmarx\n");
		security.append("├── DAST: OWASP ZAP, Burp Suite\n");
		security.append("├── Dependency scanning: Snyk, NIST NVD\n");
		security.append("└── Container scanning: Trivy, Clair\n");
		
		return security.toString();
	}

	/**
	 * Get monitoring, logging, and observability patterns
	 */
	@McpTool(description = "Get monitoring, logging, and observability strategy for microservices")
	public String getObservabilityPatterns() {
		logger.info("Observability patterns requested");
		
		StringBuilder obs = new StringBuilder();
		obs.append("=== Observability Patterns ===\n\n");
		
		obs.append("LOGGING (Centralized):\n");
		obs.append("├── Stack: ELK (Elasticsearch, Logstash, Kibana)\n");
		obs.append("├── Alternative: Splunk, Datadog\n");
		obs.append("├── Log Format: JSON for structured logging\n");
		obs.append("├── Correlation ID: Trace requests across services\n");
		obs.append("└── Retention: 7 days hot, 30 days cold\n\n");
		
		obs.append("METRICS:\n");
		obs.append("├── Collection: Prometheus scrape endpoints\n");
		obs.append("├── Visualization: Grafana dashboards\n");
		obs.append("├── Key Metrics:\n");
		obs.append("│   • Response time (p50, p95, p99)\n");
		obs.append("│   • Request rate (RPS)\n");
		obs.append("│   • Error rate\n");
		obs.append("│   • CPU, Memory, Disk usage\n");
		obs.append("│   • Database query time\n");
		obs.append("└── Retention: 30 days\n\n");
		
		obs.append("TRACING (Distributed):\n");
		obs.append("├── Technology: Jaeger or Zipkin\n");
		obs.append("├── Instrumentation: OpenTelemetry\n");
		obs.append("├── Sampling: 10% in production\n");
		obs.append("└── Shows: Latency breakdown across services\n\n");
		
		obs.append("ALERTING:\n");
		obs.append("├── Alert Manager: Prometheus AlertManager\n");
		obs.append("├── Notification: PagerDuty, Slack, Email\n");
		obs.append("├── Severity Levels: Critical, High, Medium, Low\n");
		obs.append("└── Escalation: Auto-escalate after 15 mins\n\n");
		
		obs.append("HEALTH CHECKS:\n");
		obs.append("├── Liveness: Service is running\n");
		obs.append("├── Readiness: Service ready for traffic\n");
		obs.append("├── Startup: Service initialization complete\n");
		obs.append("└── Endpoint: /actuator/health\n");
		
		return obs.toString();
	}

	/**
	 * Get common microservice design patterns and anti-patterns
	 */
	@McpTool(description = "Get design patterns, architectures, and anti-patterns for microservices")
	public String getDesignPatterns() {
		logger.info("Design patterns requested");
		
		StringBuilder patterns = new StringBuilder();
		patterns.append("=== Microservice Design Patterns ===\n\n");
		
		patterns.append("COMMUNICATION PATTERNS:\n");
		patterns.append("1. REQUEST-REPLY\n");
		patterns.append("   Sync REST/gRPC calls\n");
		patterns.append("   Use: When immediate response needed\n\n");
		
		patterns.append("2. PUBLISH-SUBSCRIBE\n");
		patterns.append("   Event-driven via message broker\n");
		patterns.append("   Use: Decoupled notifications\n\n");
		
		patterns.append("3. REQUEST-CALLBACK\n");
		patterns.append("   Async with callback endpoint\n");
		patterns.append("   Use: Long-running operations\n\n");
		
		patterns.append("TRANSACTION PATTERNS:\n");
		patterns.append("1. SAGA PATTERN\n");
		patterns.append("   Orchestrated: Central coordinator\n");
		patterns.append("   Choreography: Services react to events\n");
		patterns.append("   Use: Distributed transactions\n\n");
		
		patterns.append("2. TWO-PHASE COMMIT\n");
		patterns.append("   Prepare → Commit/Abort\n");
		patterns.append("   Use: Critical operations (not recommended)\n\n");
		
		patterns.append("3. EVENTUAL CONSISTENCY\n");
		patterns.append("   Accept temporary inconsistency\n");
		patterns.append("   Use: High availability scenarios\n\n");
		
		patterns.append("RESILIENCE PATTERNS:\n");
		patterns.append("✓ Circuit Breaker: Prevent cascading failures\n");
		patterns.append("✓ Retry with Exponential Backoff\n");
		patterns.append("✓ Timeout: Fail fast\n");
		patterns.append("✓ Bulkhead: Isolate resources\n");
		patterns.append("✓ Rate Limiting: Protect downstream services\n\n");
		
		patterns.append("ANTI-PATTERNS TO AVOID:\n");
		patterns.append("✗ Shared database between services\n");
		patterns.append("✗ Direct database-to-database access\n");
		patterns.append("✗ Chatty services (too many calls)\n");
		patterns.append("✗ Synchronous call chains\n");
		patterns.append("✗ Lack of monitoring/logging\n");
		patterns.append("✗ Monolith disguised as microservices\n");
		
		return patterns.toString();
	}

	/**
	 * Get testing strategy for microservices
	 */
	@McpTool(description = "Get testing strategies and test patterns for microservices")
	public String getTestingStrategy() {
		logger.info("Testing strategy requested");
		
		StringBuilder testing = new StringBuilder();
		testing.append("=== Testing Strategy for Microservices ===\n\n");
		
		testing.append("TEST PYRAMID:\n");
		testing.append("        /\\\n");
		testing.append("       /  \\ E2E Tests (10%)\n");
		testing.append("      /    \\ Slow, Brittle\n");
		testing.append("     /______\\\n");
		testing.append("    /        \\\n");
		testing.append("   /  Integration Tests (30%)\n");
		testing.append("  /   With real services\n");
		testing.append(" /____________\\\n");
		testing.append("/              \\\n");
		testing.append("Unit Tests (60%)\n");
		testing.append("Fast, Reliable\n");
		testing.append("\\______________/\n\n");
		
		testing.append("UNIT TESTS:\n");
		testing.append("├── Framework: JUnit 5, Mockito\n");
		testing.append("├── Coverage: Aim for 70%+ coverage\n");
		testing.append("├── Mocking: Mock external dependencies\n");
		testing.append("└── Tools: SonarQube for analysis\n\n");
		
		testing.append("INTEGRATION TESTS:\n");
		testing.append("├── Test with TestContainers\n");
		testing.append("├── Database: Use embedded H2 or TestContainers\n");
		testing.append("├── Mock external APIs\n");
		testing.append("└── Verify service boundaries\n\n");
		
		testing.append("CONTRACT TESTS:\n");
		testing.append("├── Consumer-driven contracts\n");
		testing.append("├── Framework: Pact\n");
		testing.append("├── Verify: API compatibility\n");
		testing.append("└── Prevent breaking changes\n\n");
		
		testing.append("LOAD TESTING:\n");
		testing.append("├── Tool: JMeter, Gatling\n");
		testing.append("├── Target: 1000+ req/sec\n");
		testing.append("├── Metrics: P99 < 200ms\n");
		testing.append("└── Frequency: Before releases\n\n");
		
		testing.append("E2E TESTS:\n");
		testing.append("├── Minimal, critical flows only\n");
		testing.append("├── Framework: Cypress, Selenium\n");
		testing.append("├── Environment: Staging replica\n");
		testing.append("└── Run: Post-deployment smoke tests\n");
		
		return testing.toString();
	}

	/**
	 * Get troubleshooting and debugging guidelines
	 */
	@McpTool(description = "Get troubleshooting, debugging, and incident response guidelines")
	public String getTroubleshootingGuide() {
		logger.info("Troubleshooting guide requested");
		
		StringBuilder guide = new StringBuilder();
		guide.append("=== Troubleshooting & Debugging Guide ===\n\n");
		
		guide.append("COMMON ISSUES:\n\n");
		
		guide.append("1. SERVICE TIMEOUT\n");
		guide.append("   Check:\n");
		guide.append("   ├── Downstream service health: /actuator/health\n");
		guide.append("   ├── Network connectivity: ping service DNS\n");
		guide.append("   ├── Logs for errors: Check centralized logging\n");
		guide.append("   ├── Database query time: Slow queries?\n");
		guide.append("   └── Load: Is service under load?\n\n");
		
		guide.append("2. HIGH MEMORY USAGE\n");
		guide.append("   Check:\n");
		guide.append("   ├── Heap dumps: jmap -dump:live,format=b\n");
		guide.append("   ├── Connection pools: Configured correctly?\n");
		guide.append("   ├── Cache invalidation: Cache growing unbounded?\n");
		guide.append("   ├── Goroutine/thread leaks\n");
		guide.append("   └── GC logs: Is GC running frequently?\n\n");
		
		guide.append("3. CASCADING FAILURES\n");
		guide.append("   Check:\n");
		guide.append("   ├── Circuit breaker status\n");
		guide.append("   ├── Rate limiting working?\n");
		guide.append("   ├── Retry logic: Exponential backoff?\n");
		guide.append("   ├── Timeout values\n");
		guide.append("   └── Deploy circuit breaker immediately\n\n");
		
		guide.append("4. DATABASE CONNECTION FAILURES\n");
		guide.append("   Check:\n");
		guide.append("   ├── DB reachability: telnet host port\n");
		guide.append("   ├── Connection pool exhausted: Check metrics\n");
		guide.append("   ├── Credentials correct: Check secrets\n");
		guide.append("   ├── SSL/TLS certificates: expired?\n");
		guide.append("   └── Max connections: Increase if needed\n\n");
		
		guide.append("DEBUGGING TOOLS:\n");
		guide.append("├── Distributed Tracing: Jaeger UI\n");
		guide.append("├── Centralized Logging: Kibana\n");
		guide.append("├── Metrics: Grafana dashboards\n");
		guide.append("├── CLI: kubectl logs, kubectl exec\n");
		guide.append("└── Profilers: Java Flight Recorder\n\n");
		
		guide.append("INCIDENT RESPONSE:\n");
		guide.append("1. Assess: Severity, impact, scope\n");
		guide.append("2. Notify: Alert on-call engineer\n");
		guide.append("3. Mitigate: Circuit breaker, scale up\n");
		guide.append("4. Investigate: Check logs, metrics, traces\n");
		guide.append("5. Fix: Deploy patch or rollback\n");
		guide.append("6. Verify: Monitor for regression\n");
		guide.append("7. Document: Post-mortem analysis\n");
		
		return guide.toString();
	}

	/**
	 * Get team best practices and standards
	 */
	@McpTool(description = "Get team best practices, coding standards, and guidelines for microservices development")
	public String getTeamStandards() {
		logger.info("Team standards requested");
		
		StringBuilder standards = new StringBuilder();
		standards.append("=== Team Best Practices & Standards ===\n\n");
		
		standards.append("CODE QUALITY:\n");
		standards.append("✓ Language: Java 17+\n");
		standards.append("✓ Framework: Spring Boot 4.0+\n");
		standards.append("✓ SonarQube: Minimum grade A\n");
		standards.append("✓ Coverage: Minimum 70% unit test coverage\n");
		standards.append("✓ Linting: Checkstyle, SpotBugs\n\n");
		
		standards.append("VERSION CONTROL:\n");
		standards.append("✓ Git workflow: Git Flow or Trunk-Based\n");
		standards.append("✓ Commits: Atomic, descriptive messages\n");
		standards.append("✓ PR reviews: Minimum 2 approvers\n");
		standards.append("✓ Branch protection: No direct push to main\n\n");
		
		standards.append("NAMING CONVENTIONS:\n");
		standards.append("├── Packages: com.company.service.domain\n");
		standards.append("├── Classes: PascalCase (UserService)\n");
		standards.append("├── Methods: camelCase (getUserById)\n");
		standards.append("├── Constants: UPPER_SNAKE_CASE\n");
		standards.append("└── Services: {domain}-service\n\n");
		
		standards.append("COMMIT CONVENTIONS:\n");
		standards.append("feat: New feature\n");
		standards.append("fix: Bug fix\n");
		standards.append("refactor: Code restructuring\n");
		standards.append("test: Test updates\n");
		standards.append("docs: Documentation\n");
		standards.append("chore: Dependency updates\n\n");
		
		standards.append("API DESIGN:\n");
		standards.append("✓ REST: Follow RFC 7231\n");
		standards.append("✓ Versioning: URL path /api/v1/\n");
		standards.append("✓ Pagination: ?page=1&size=20\n");
		standards.append("✓ Filtering: ?status=active\n");
		standards.append("✓ Sorting: ?sort=createdAt:desc\n\n");
		
		standards.append("DOCUMENTATION:\n");
		standards.append("✓ README.md: Setup and run instructions\n");
		standards.append("✓ API docs: OpenAPI/Swagger\n");
		standards.append("✓ ADRs: Architecture Decision Records\n");
		standards.append("✓ Inline comments: Complex logic only\n");
		standards.append("✓ Runbooks: Deployment procedures\n");
		
		return standards.toString();
	}

	/**
	 * List all available MCP tools for microservices
	 */
	@McpTool(description = "List all available MCP tools provided by the Microservices Documentation service")
	public String listAvailableTools() {
		logger.info("Available tools list requested");
		
		StringBuilder tools = new StringBuilder();
		tools.append("=== Available MCP Tools for Team Microservices ===\n\n");
		
		tools.append("1. getMicroserviceRegistry()\n");
		tools.append("   Get complete registry of all team microservices\n\n");
		
		tools.append("2. getServiceDependencies(serviceName)\n");
		tools.append("   Get dependencies and communication patterns\n");
		tools.append("   Parameter: service name or 'all'\n\n");
		
		tools.append("3. getApiContracts(serviceName)\n");
		tools.append("   Get API contract templates and schemas\n");
		tools.append("   Parameter: service name\n\n");
		
		tools.append("4. getDeploymentPatterns()\n");
		tools.append("   Get deployment strategies and configurations\n\n");
		
		tools.append("5. getDatabasePatterns()\n");
		tools.append("   Get database patterns and data management\n\n");
		
		tools.append("6. getSecurityPatterns()\n");
		tools.append("   Get security and auth patterns\n\n");
		
		tools.append("7. getObservabilityPatterns()\n");
		tools.append("   Get monitoring, logging, and observability\n\n");
		
		tools.append("8. getDesignPatterns()\n");
		tools.append("   Get microservice design patterns\n\n");
		
		tools.append("9. getTestingStrategy()\n");
		tools.append("   Get testing strategies and test patterns\n\n");
		
		tools.append("10. getTroubleshootingGuide()\n");
		tools.append("    Get debugging and incident response\n\n");
		
		tools.append("11. getTeamStandards()\n");
		tools.append("    Get team best practices and standards\n\n");
		
		tools.append("12. listAvailableTools()\n");
		tools.append("    List all available tools (this command)\n");
		
		return tools.toString();
	}

}

