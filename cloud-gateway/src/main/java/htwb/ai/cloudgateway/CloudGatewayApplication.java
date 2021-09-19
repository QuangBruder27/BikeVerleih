package htwb.ai.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }

    public static String baseUrl = "http://localhost";

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, MyFilter myFilter) {
        System.out.println("Builder: "+builder.toString());
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        //.id("auth-service")
                        .uri(baseUrl+":8100/"))

                .route(r -> r.path("/bonus/**")
                        //Pre and Post Filters provided by Spring Cloud Gateway
                        .filters(f -> f.filter(myFilter.apply(new MyFilter.Config())))
                        .uri(baseUrl+":8200/"))
                        //.id("bonus-service")

                .route(r -> r.path("/rent/**")
                        .filters(f -> f.filter(myFilter.apply(new MyFilter.Config())))
                        //.id("rent-service")
                        .uri(baseUrl+":8300/"))
                .route(r -> r.path("/report/**")
                        .filters(f -> f.filter(myFilter.apply(new MyFilter.Config())))
                        // .id("report-service")
                        .uri(baseUrl+":8400/"))
                .route(r -> r.path("/locate/**")
                        .filters(f -> f.filter(myFilter.apply(new MyFilter.Config())))
                        //.id("location-service")
                        .uri(baseUrl+":8500/"))



                .build();
    }
}
