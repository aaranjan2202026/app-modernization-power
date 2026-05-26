const { Server } = require("@modelcontextprotocol/sdk/server/index.js");
const { StdioServerTransport } = require("@modelcontextprotocol/sdk/server/stdio.js");
const { CallToolRequestSchema, ListToolsRequestSchema } = require("@modelcontextprotocol/sdk/types.js");
const axios = require("axios");

const SONARQUBE_URL = process.env.SONARQUBE_URL || "http://localhost:9000";
const SONARQUBE_TOKEN = process.env.SONARQUBE_TOKEN || "";

const sonarClient = axios.create({
  baseURL: SONARQUBE_URL,
  headers: { "Authorization": `Bearer ${SONARQUBE_TOKEN}` }
});

const server = new Server({
  name: "sonarqube-mcp-server",
  version: "1.0.0",
}, {
  capabilities: { tools: {} }
});

server.setRequestHandler(ListToolsRequestSchema, async () => {
  return {
    tools: [
      { name: "ping_system", description: "Ping SonarQube system", inputSchema: { type: "object", properties: {}, required: [] } },
      { name: "get_system_health", description: "Get system health", inputSchema: { type: "object", properties: {}, required: [] } },
      { name: "search_my_sonarqube_projects", description: "List all projects", inputSchema: { type: "object", properties: { p: { type: "number" }, ps: { type: "number" } }, required: [] } },
      { name: "search_sonar_issues_in_projects", description: "Search issues", inputSchema: { type: "object", properties: { projectKeys: { type: "string" }, severities: { type: "string" }, types: { type: "string" }, resolved: { type: "string" }, p: { type: "number" }, ps: { type: "number" } }, required: ["projectKeys"] } },
      { name: "show_rule", description: "Get rule details", inputSchema: { type: "object", properties: { key: { type: "string" } }, required: ["key"] } },
      { name: "get_component_measures", description: "Get metrics", inputSchema: { type: "object", properties: { component: { type: "string" }, metricKeys: { type: "string" } }, required: ["component", "metricKeys"] } },
      { name: "get_project_quality_gate_status", description: "Get quality gate", inputSchema: { type: "object", properties: { projectKey: { type: "string" } }, required: ["projectKey"] } },
      { name: "change_sonar_issue_status", description: "Change issue status", inputSchema: { type: "object", properties: { issue: { type: "string" }, transition: { type: "string" }, comment: { type: "string" } }, required: ["issue", "transition"] } }
    ]
  };
});

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const { name, arguments: args } = request.params;
  try {
    let response;
    switch (name) {
      case "ping_system":
        response = await sonarClient.get("/api/system/ping");
        return { content: [{ type: "text", text: JSON.stringify({ status: "ok", response: response.data }) }] };
      case "get_system_health":
        response = await sonarClient.get("/api/system/health");
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "search_my_sonarqube_projects":
        response = await sonarClient.get("/api/projects/search", { params: { p: args.p || 1, ps: args.ps || 100 } });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "search_sonar_issues_in_projects":
        const params = { projectKeys: args.projectKeys, p: args.p || 1, ps: args.ps || 500 };
        if (args.severities) params.severities = args.severities;
        if (args.types) params.types = args.types;
        if (args.resolved !== undefined) params.resolved = args.resolved;
        response = await sonarClient.get("/api/issues/search", { params });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "show_rule":
        response = await sonarClient.get("/api/rules/show", { params: { key: args.key } });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "get_component_measures":
        response = await sonarClient.get("/api/measures/component", { params: { component: args.component, metricKeys: args.metricKeys } });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "get_project_quality_gate_status":
        response = await sonarClient.get("/api/qualitygates/project_status", { params: { projectKey: args.projectKey } });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      case "change_sonar_issue_status":
        const changeParams = { issue: args.issue, transition: args.transition };
        if (args.comment) changeParams.comment = args.comment;
        response = await sonarClient.post("/api/issues/do_transition", null, { params: changeParams });
        return { content: [{ type: "text", text: JSON.stringify(response.data) }] };
      default:
        throw new Error(`Unknown tool: ${name}`);
    }
  } catch (error) {
    return { content: [{ type: "text", text: JSON.stringify({ error: error.response?.data || error.message, status: error.response?.status }) }], isError: true };
  }
});

async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error("SonarQube MCP Server running");
}

main().catch((error) => { console.error("Server error:", error); process.exit(1); });
