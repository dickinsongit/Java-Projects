# Testing the Server

MCP Inspector:

  npx @modelcontextprotocol/inspector

  Set Transport Type: Streamable HTTP
  URL: http://localhost:8080/mcp



### Add MCP Server (VS Code) 

{
	"servers": {
		"hello-mcp": {
			"url": "http://localhost:8082/hello/mcp",
			"type": "http"
		},
		
		"arch-mcp-server": {
			"url": "http://localhost:8083/arch/mcp",
			"type": "http"
		}
	},
	"inputs": []
}