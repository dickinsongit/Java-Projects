package com.arch.doc.mcp.service;

// @Component
public class HealthResourceProvider {

    // private final ServiceRegistryRepository repository;

    // public HealthResourceProvider(ServiceRegistryRepository repository) {
    //     this.repository = repository;
    // }

    // @McpResource(
    //     uri = "health://{serviceName}", 
    //     name = "Service Health Status", 
    //     description = "Provides the real-time health and error logs for a specific microservice"
    // )
    // public Mono<ReadResourceResult> getServiceHealth(String serviceName) {
    //     return repository.findByName(serviceName)
    //         .map(service -> {
    //             String statusData = """
    //                 Service: %s
    //                 Status: %s
    //                 Version: %s
    //                 Last Heartbeat: %s
    //                 Recent Errors: %s
    //                 """.formatted(
    //                     service.getName(),
    //                     service.getStatus(),
    //                     service.getVersion(),
    //                     service.getLastHeartbeat(),
    //                     service.getRecentErrorLog()
    //                 );

    //             return new ReadResourceResult(List.of(
    //                 new TextResourceContents("health://" + serviceName, "text/plain", statusData)
    //             ));
    //         })
    //         .switchIfEmpty(Mono.error(new RuntimeException("Service not found: " + serviceName)));
    // }
}
