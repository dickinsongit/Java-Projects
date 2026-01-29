# Testing the Server

MCP Inspector:

  npx @modelcontextprotocol/inspector

  Set Transport Type: Streamable HTTP
  URL: http://localhost:8080/mcp

  {
	"servers": {
		"hello-mcp": {
			"url": "http://localhost:8082/hello/mcp",
			"type": "http"
		}
	},
	"inputs": []
}