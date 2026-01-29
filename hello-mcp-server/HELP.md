# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.2/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.2/reference/using/devtools.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


## Testing with MCP Inspector

This project implements an MCP server using Spring AI. You can test it with the MCP Inspector either over HTTP (SSE) or over stdio.

### Option A: Test over HTTP (SSE) — current setup

The current dependency (`spring-ai-starter-mcp-server-webmvc`) exposes an SSE endpoint.

- Start the server:

```bash
./mvnw -f hello-mcp-server spring-boot:run
```

- In MCP Inspector, add a new client of type `sse` with the URL:

```
http://localhost:8082/mcp
```

You should see the server tools (e.g., `hello`, `customGreeting`) available to invoke.

If your `application.properties` uses `spring.ai.mcp.server.protocol=SSE`, consider updating to the property expected by your Spring AI version (often `spring.ai.mcp.server.transport=sse`). Refer to Spring AI MCP Server docs for the matching property name for your version.

### Option B: Test over stdio — requires stdio starter

To use stdio, switch the Maven dependency to the stdio MCP server starter (refer to Spring AI docs for the exact artifact, commonly named similar to `spring-ai-starter-mcp-server-stdio`). Then enable stdio in `application.properties` (example for newer versions):

```
spring.ai.mcp.server.transport=stdio
# or
spring.ai.mcp.server.stdio=true
```

Run the app and connect MCP Inspector with a stdio client. Example Inspector client command (spawned process):

```bash
./mvnw -q -f hello-mcp-server spring-boot:run -Dspring-boot.run.arguments=--spring.ai.mcp.server.transport=stdio
```

In MCP Inspector, set client type to `stdio`, command to `./mvnw`, and args:

```
-q
-f
hello-mcp-server
spring-boot:run
-Dspring-boot.run.arguments=--spring.ai.mcp.server.transport=stdio
```

Notes:
- Ensure only one transport is enabled at a time (SSE or stdio).
- Property names can differ by Spring AI version (`protocol` vs `transport`); use the key supported by your library version.
- If Inspector fails to connect over stdio, verify the stdio starter is on the classpath and that the app writes MCP JSON-RPC over stdout.


{
  "github.copilot.chat.mcp.servers": {
    "hello-mcp": {
      "url": "http://localhost:8082/mcp/sse"
    }
  }
}

# Testing the Server

### MCP Inspector:

  npx @modelcontextprotocol/inspector

  Set Transport Type: SSE
  URL: http://localhost:8082/hello/mcp

# Add MCP Server (VS Code) 

{
    "servers": {
	  	  "hello-mcp": {
			      "url": "http://localhost:8082/hello/mcp",
			      "type": "http"
		    }
  	},
  	"inputs": []
}
