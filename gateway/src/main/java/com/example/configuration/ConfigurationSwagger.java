//package com.example.configuration;
//
////import java.util.ArrayList;
////import java.util.List;
////import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.context.annotation.Primary;
////import springfox.documentation.swagger.web.SwaggerResource;
////import springfox.documentation.swagger.web.SwaggerResourcesProvider;
////
////@Configuration
////@Primary
////public class ConfigurationSwagger implements SwaggerResourcesProvider {
////
////  public static final String API_URI = "/v3/api-docs";
////  private final RouteDefinitionLocator routeLocator;
////
////  public ConfigurationSwagger(RouteDefinitionLocator routeLocator) {
////    this.routeLocator = routeLocator;
////  }
////
////  ConfigurationSwagger
////
////  private SwaggerResource swaggerResource(String name, String location) {
////    SwaggerResource swaggerResource = new SwaggerResource();
////    swaggerResource.setName(name);
////    swaggerResource.setLocation(location);
////    swaggerResource.setSwaggerVersion("1.0.0");
////    return swaggerResource;
////  }
////
////  @Override
////  public List<SwaggerResource> get() {
////    List<SwaggerResource> resources = new ArrayList<>();
////    routeLocator.getRouteDefinitions().subscribe(
////        routeDefinition -> {
////          String resourceName = routeDefinition.getId();
////          String location =
////              routeDefinition
////                  .getPredicates()
////                  .get(0)
////                  .getArgs()
////                  .get("_genkey_0")
////                  .replace("/**", API_URI);
////          resources.add(
////              swaggerResource(resourceName, location)
////          );
////        }
////    );
////    return resources;
////  }
////
////}
//
//import java.util.ArrayList;
//import java.util.List;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//@Configuration
//public class ConfigurationSwagger {
//
//  @Bean
//  public SwaggerResourcesProvider swaggerResourcesProvider(
//      RouteDefinitionLocator routeDefinitionLocator) {
//    return () -> {
//      List<SwaggerResource> resources = new ArrayList<>();
//      routeDefinitionLocator.getRouteDefinitions().subscribe(routeDefinition -> {
//        resources.add(
//            createResource(routeDefinition.getId(), routeDefinition.getPredicates().toString()));
//      });
//      return resources;
//    };
//  }
//
//  @Bean
//  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//    return builder.routes()
//        .route("example-route", r -> r
//            .path("/example")
//            .uri("http://example.com"))
//        // Add more routes as needed
//        .build();
//  }
//
//  private SwaggerResource createResource(String name, String location) {
//    SwaggerResource swaggerResource = new SwaggerResource();
//    swaggerResource.setName(name);
//    swaggerResource.setLocation(location + "/v2/api-docs");
//    swaggerResource.setSwaggerVersion("2.0");
//    return swaggerResource;
//  }
//}
//
