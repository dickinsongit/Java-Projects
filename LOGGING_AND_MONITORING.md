# Logging and Monitoring Configuration Guide

## Overview
This guide explains the comprehensive logging and monitoring system implemented in the Demo SpringBoot Application.

---

## 1. Logging Architecture

### Components

#### SLF4J (Simple Logging Facade for Java)
- **Purpose:** Abstraction layer for logging frameworks
- **Benefits:** Allows switching logging implementations without changing code
- **Usage:** Used throughout the application via `LoggerFactory`

#### Logback
- **Purpose:** Production-ready logging framework
- **Benefits:** Better performance, better configuration options, asynchronous logging
- **Configuration:** `logback-spring.xml`

#### JSON Logging
- **Purpose:** Structure logs for automated parsing and aggregation
- **Tools:** Logback JSON encoder with Jackson
- **Output:** `application-json.log` files

#### Spring Cloud Sleuth
- **Purpose:** Distributed tracing across microservices
- **Benefits:** Correlate logs across service calls with trace IDs
- **Configuration:** Trace ID generation and sampling

#### Micrometer & Prometheus
- **Purpose:** Metrics collection and monitoring
- **Benefits:** Real-time application metrics and performance monitoring
- **Output:** Prometheus-compatible format at `/actuator/prometheus`

---

## 2. Log Files

### File Locations
```
logs/
├── application.log              # Main application logs (text format)
├── application-json.log         # JSON format for log aggregation
├── application-error.log        # Error logs only
├── dev.log                       # Development profile specific
└── [archive files]              # Rotated logs with date stamps
```

### Log Levels

| Level | Description | Used For |
|-------|-------------|----------|
| DEBUG | Detailed debugging information | Development, troubleshooting |
| INFO | General informational messages | Important events, state changes |
| WARN | Warning messages for potential issues | Degraded states, unusual conditions |
| ERROR | Error conditions | Exceptions, failures |

### Configuration by Profile

#### Default Profile
```
console: INFO level
file: INFO level (application.log)
json: Full structured logging
error: Separate error file
max file size: 10MB
retention: 30 days
```

#### Development Profile (`dev`)
```
console: DEBUG level
file: DEBUG level (dev.log)
max file size: 5MB
retention: 7 days
verbose output
```

#### Production Profile (`prod`)
```
console: WARN level
file: INFO level
json: Full structured logging
async writing for performance
max file size: 50MB
retention: 90 days
total size cap: 10GB
```

---

## 3. Running the Application

### Start with Default Profile
```bash
./mvnw clean package -DskipTests
java -jar target/demo-springboot-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Start with Development Profile
```bash
java -jar target/demo-springboot-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  --server.port=8081
```

### Start with Production Profile
```bash
java -jar target/demo-springboot-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8081 \
  --logging.file.name=/var/log/demo-springboot/application.log
```

---

## 4. Log File Analysis

### View Application Logs
```bash
# Real-time log streaming
tail -f logs/application.log

# Last 50 lines
tail -50 logs/application.log

# Search for errors
grep ERROR logs/application.log

# Search with context
grep -B 2 -A 2 ERROR logs/application.log
```

### View JSON Logs
```bash
# Pretty print JSON logs
cat logs/application-json.log | python3 -m json.tool

# Parse with jq (if installed)
cat logs/application-json.log | jq '.'

# Filter by level
cat logs/application-json.log | jq 'select(.level=="ERROR")'
```

### Error Log Analysis
```bash
# View only errors
tail -f logs/application-error.log

# Count errors by type
grep "Exception" logs/application-error.log | sort | uniq -c
```

---

## 5. Monitoring Endpoints

### Health Endpoint
```
GET /actuator/health

Response:
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {...}
    }
  }
}
```

### Info Endpoint
```
GET /actuator/info

Response:
{
  "app": {
    "name": "Demo SpringBoot Application",
    "description": "A lightweight Spring Boot application with logging and monitoring",
    "version": "0.0.1-SNAPSHOT"
  }
}
```

### Metrics Endpoint
```
GET /actuator/metrics

Response:
{
  "names": [
    "jvm.memory.used",
    "jvm.gc.pause",
    "http.server.requests",
    "process.uptime",
    ...
  ]
}
```

### Prometheus Metrics
```
GET /actuator/prometheus

Response (plain text format):
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap"} 123456789
...
```

### Loggers Endpoint
```
GET /actuator/loggers

List all active loggers:
{
  "loggers": [
    {
      "name": "com.example.demo",
      "configuredLevel": "DEBUG",
      "effectiveLevel": "DEBUG"
    },
    ...
  ]
}
```

### Change Log Level at Runtime
```bash
POST /actuator/loggers/com.example.demo
Content-Type: application/json

{
  "configuredLevel": "INFO"
}
```

---

## 6. Distributed Tracing Setup

### What is Trace ID?
A unique identifier that follows a request through multiple services/methods.

### Example Trace ID in Logs
```
2026-01-22 22:27:35.031 [main] INFO ... [demo] ... 
```

The trace ID is generated by Spring Cloud Sleuth and included in all logs for a single request.

### Using Trace IDs
```bash
# Find all logs for a specific trace
grep "TRACE_ID_HERE" logs/application.log

# Correlate across multiple services
grep "trace-id=12345" logs/application.log
```

---

## 7. Integration with External Monitoring

### Prometheus Integration

**Setup Prometheus to scrape metrics:**
```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'demo-springboot'
    static_configs:
      - targets: ['localhost:8081']
    metrics_path: '/actuator/prometheus'
```

### ELK Stack (Elasticsearch, Logstash, Kibana)

**Logstash Configuration to Parse JSON Logs:**
```
input {
  file {
    path => "/path/to/logs/application-json.log"
    codec => json
    start_position => "beginning"
  }
}

output {
  elasticsearch {
    hosts => ["localhost:9200"]
    index => "demo-springboot-%{+YYYY.MM.dd}"
  }
}
```

### CloudWatch Integration (AWS)

**Add Logback CloudWatch Appender:**
```xml
<appender name="CLOUDWATCH" class="com.github.jcustenborder.logback.aws.CloudWatchAppender">
  <logGroup>demo-springboot-logs</logGroup>
  <logStream>${HOSTNAME}-application</logStream>
  <encoder>
    <pattern>${LOG_PATTERN}</pattern>
  </encoder>
</appender>
```

---

## 8. Performance Optimization

### Async Logging
- Uses `AsyncAppender` for file operations
- Non-blocking writes improve throughput
- Queue size: 512 (default), 2048 (production)

### Rolling File Policy
- Rotates logs when file reaches max size or daily
- Automatic cleanup based on retention policy
- Compresses old logs to save disk space

### JSON Logging Benefits
- Enables automated parsing
- Integrates with log aggregation tools
- Structured queries possible
- Machine-readable format

---

## 9. Best Practices

### Logging Guidelines

✅ **DO:**
- Log important business events
- Include context (user ID, session, operation)
- Use appropriate log levels
- Include exception stacktraces with ERROR logs
- Use structured logging (JSON) for production

❌ **DON'T:**
- Log sensitive data (passwords, tokens, PII)
- Log at DEBUG level in production
- Use string concatenation for logs (use parameterized logging)
- Create excessive logs that impact performance

### Parameterized Logging
```java
// Good - parameters are not evaluated if DEBUG is off
logger.debug("Processing request for user: {}", userId);

// Bad - string concatenation always happens
logger.debug("Processing request for user: " + userId);
```

### Exception Logging
```java
// Good - includes full stacktrace
logger.error("Failed to process payment", exception);

// Bad - loses stacktrace
logger.error("Failed to process payment: " + exception.getMessage());
```

---

## 10. Troubleshooting

### Issue: No log files created
**Solution:**
1. Ensure `logs/` directory exists: `mkdir -p logs`
2. Check file permissions: `chmod 755 logs`
3. Verify `logback-spring.xml` configuration

### Issue: Logs growing too large
**Solution:**
1. Reduce log level in production profile
2. Adjust `maxFileSize` in logback configuration
3. Reduce `maxHistory` retention period
4. Implement log rotation externally

### Issue: Application startup slow
**Solution:**
1. Async appenders are enabled (should not impact startup)
2. Check if disk is slow (reduce logging in dev)
3. Review log sampling rate for Sleuth

### Issue: JSON logs not formatted correctly
**Solution:**
1. Verify `logback-json-classic` dependency is installed
2. Check Jackson dependency is present
3. Ensure encoder class is `ch.qos.logback.contrib.json.classic.JsonEncoder`

---

## 11. Quick Reference Commands

### View Application Status
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8081/actuator/info
curl http://localhost:8081/actuator/metrics
```

### Watch Logs in Real-time
```bash
tail -f logs/application.log

# With grep filtering
tail -f logs/application.log | grep ERROR
```

### Search Logs
```bash
# Find all DEBUG logs
grep "DEBUG" logs/application.log

# Find logs from specific class
grep "HealthcheckController" logs/application.log

# Find logs in time range (example)
grep "2026-01-22 22:27" logs/application.log
```

### Manage Log Rotation
```bash
# Manually compress old logs
gzip logs/application.*.log

# Delete logs older than 30 days
find logs -name "*.log" -mtime +30 -delete
```

---

## 12. Next Steps

1. **Monitor Application:** Access `/actuator/health` regularly
2. **Setup Alerts:** Configure Prometheus rules for critical metrics
3. **Log Aggregation:** Set up ELK stack for centralized log analysis
4. **Performance Tuning:** Adjust async queue sizes based on traffic
5. **Security:** Mask sensitive data in logs
6. **Documentation:** Document custom metrics specific to your business

