# Design Specification - Demo SpringBoot Application

## Project Overview
A lightweight Spring Boot web application that provides a RESTful API and an interactive home page built with HTML, CSS, and JavaScript.

**Framework:** Spring Boot 4.0.1  
**Java Version:** 25.0.2  
**Server:** Apache Tomcat 11.0.15  
**Build Tool:** Maven  

---

## Architecture

### 1. High-Level Architecture
```
┌─────────────────────────────────────────┐
│         Client Browser                  │
│  (index.html + JavaScript)              │
└──────────────┬──────────────────────────┘
               │ HTTP Requests
               ▼
┌─────────────────────────────────────────┐
│     Spring Boot Application             │
│  Port 8081 (Tomcat Server)              │
├─────────────────────────────────────────┤
│  Controllers                            │
│  ├── HealthcheckController              │
│  └── (Static Resource Handler)          │
│                                         │
│  Static Resources                       │
│  └── src/main/resources/static/         │
│      └── index.html                     │
└─────────────────────────────────────────┘
```

### 2. Project Structure
```
demo-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java          (Main Spring Boot Class)
│   │   │   └── controller/
│   │   │       └── HealthcheckController.java (REST API)
│   │   └── resources/
│   │       ├── application.properties         (Configuration)
│   │       └── static/
│   │           └── index.html                 (Home Page)
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java
├── pom.xml                                    (Maven Dependencies)
└── DESIGN_SPECIFICATION.md                    (This Document)
```

---

## Components

### 1. Backend - Spring Boot Application

#### DemoApplication.java
- **Purpose:** Entry point for the Spring Boot application
- **Responsibilities:**
  - Initialize Spring Boot context
  - Configure embedded Tomcat server
  - Load application configuration
  
#### HealthcheckController.java
- **Purpose:** Provides REST API endpoints for health monitoring
- **Endpoint:** `GET /api/health`
- **Response:** `OK` (String)
- **Status Code:** 200 (OK)
- **Use Case:** Allows frontend to verify backend availability

### 2. Frontend - Static Web Page

#### index.html
- **Purpose:** Single-page home interface
- **Location:** `src/main/resources/static/index.html`
- **Served At:** `http://localhost:8081/`
- **Features:**
  - Welcome message and branding
  - Interactive buttons for user actions
  - Real-time API communication via JavaScript
  - Responsive design with CSS styling

---

## Features

### 1. User Interface
- **Welcome Section**
  - Displays greeting message
  - Application title and subtitle
  - Brief description

- **Interactive Buttons**
  - **Say Hello Button:** Displays a welcome message
  - **Check Health Button:** Calls backend `/api/health` endpoint and displays status

- **Visual Feedback**
  - Status messages displayed dynamically
  - Color-coded responses (green for success, red for errors)
  - Smooth transitions and hover effects

### 2. Styling
- **Color Scheme:**
  - Primary: #667eea (Purple-blue)
  - Secondary: #764ba2 (Deep purple)
  - Background: Linear gradient (135deg)
  
- **Layout:**
  - Centered container design
  - Maximum width: 700px
  - Responsive with proper padding and spacing
  - Box shadow for depth

- **Typography:**
  - Font Family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif
  - Heading: 2.5em, bold
  - Body Text: 1.05em, readable line-height (1.8)

### 3. JavaScript Interactions
- **handleClick():** 
  - Displays hello message when button clicked
  - Hides health status if visible
  
- **checkHealth():**
  - Makes fetch request to `/api/health` endpoint
  - Displays server status dynamically
  - Handles errors gracefully with error messages
  - Shows loading-like experience

---

## API Specification

### Health Check Endpoint
```
GET /api/health
```

**Request:**
```
GET /api/health HTTP/1.1
Host: localhost:8081
```

**Response (Success):**
```
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

OK
```

**Usage:**
```javascript
fetch('/api/health')
  .then(response => response.text())
  .then(data => console.log(data))  // Output: OK
  .catch(error => console.error(error));
```

---

## Deployment Configuration

### Server Settings
- **Port:** 8081 (configurable via `--server.port=8081`)
- **Context Path:** `/` (root)
- **Protocol:** HTTP

### Application Properties
**File:** `src/main/resources/application.properties`

Key configurations:
- Server port (default: 8080, override to 8081)
- Application name: demo-springboot
- Version: 0.0.1-SNAPSHOT

---

## Build & Deployment

### Build Process
```bash
# Clean and build with tests
./mvnw clean install

# Clean and build without tests
./mvnw clean package -DskipTests
```

**Output:** `target/demo-springboot-0.0.1-SNAPSHOT.jar`

### Running the Application
```bash
# Run with default port (8080)
java -jar target/demo-springboot-0.0.1-SNAPSHOT.jar

# Run on custom port (8081)
java -jar target/demo-springboot-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Dependencies
- **Spring Boot Starter Web** - Web framework and Tomcat
- **Spring Boot Starter Test** - Testing framework
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework

---

## User Workflows

### 1. Accessing the Application
1. User navigates to `http://localhost:8081/`
2. Spring Boot serves `index.html` as welcome page
3. Page loads with welcome message and buttons

### 2. Say Hello Workflow
1. User clicks "Say Hello" button
2. JavaScript function `handleClick()` executes
3. Message area displays: "Hello! Welcome to the Spring Boot application."
4. Health status hidden (if previously shown)

### 3. Check Health Workflow
1. User clicks "Check Health" button
2. JavaScript sends `fetch()` request to `/api/health`
3. Backend responds with "OK"
4. Success message displays: "✓ Application Status: OK"
5. Message area hides (if previously shown)

### 4. Error Handling
1. If health check fails (network error, server down)
2. Error message displays in red
3. Shows: "✗ Error checking health: [error details]"

---

## Error Handling & Edge Cases

| Scenario | Behavior |
|----------|----------|
| Port 8080 already in use | Use `--server.port=8081` to run on alternate port |
| Frontend cannot reach API | Error message displayed with fetch error details |
| Controller not found | 404 response returned |
| Invalid request method | 405 Method Not Allowed |

---

## Performance Considerations

- **Static Content:** Served directly without processing
- **API Response:** Minimal payload (single string "OK")
- **Load Time:** Sub-second response times expected
- **Scalability:** Lightweight design suitable for development

---

## Security Notes

- **CORS:** Not explicitly configured (same-origin requests only)
- **Authentication:** Not implemented (public endpoints)
- **HTTPS:** Not configured (HTTP only for development)
- **Input Validation:** Not required for current simple endpoints

---

## Testing

### Unit Tests
**File:** `DemoApplicationTests.java`
- Loads Spring Boot context
- Verifies application startup
- Tests are run during Maven build (can skip with `-DskipTests`)

### Manual Testing
```bash
# Test home page
curl http://localhost:8081/

# Test health endpoint
curl http://localhost:8081/api/health
# Response: OK
```

---

## Future Enhancements

1. **Authentication:** Add Spring Security for user login
2. **Database:** Integrate Spring Data JPA with persistence layer
3. **API Versioning:** Implement versioned endpoints (`/api/v1/...`)
4. **Logging:** Add log aggregation and monitoring
5. **HTTPS:** Configure SSL/TLS certificates
6. **Caching:** Add Redis for performance optimization
7. **Frontend Framework:** Migrate to React or Vue.js
8. **Containerization:** Add Docker support

---

## Troubleshooting

### Home page not loading
- Verify application is running on correct port
- Check browser console for JavaScript errors
- Clear browser cache (Cmd+Shift+Delete on macOS)
- Verify `index.html` exists in `src/main/resources/static/`

### API endpoint returns 404
- Ensure controller is in correct package
- Verify `@RestController` and `@GetMapping` annotations present
- Rebuild with `./mvnw clean package`

### Port already in use
- Find process using port: `lsof -i :8080` (macOS/Linux)
- Kill process or use different port with `--server.port=XXXX`

---

## Contact & Support

**Project:** Demo SpringBoot Application  
**Version:** 0.0.1-SNAPSHOT  
**Last Updated:** January 22, 2026  
**Framework:** Spring Boot 4.0.1
