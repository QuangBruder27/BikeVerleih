package htwb.ai.cloudgateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static htwb.ai.cloudgateway.CloudGatewayApplication.baseUrl;

@Component
public class MyFilter extends AbstractGatewayFilterFactory<MyFilter.Config> {
    public MyFilter() {
        super(Config.class);
    }

    String currentUserId;

    private boolean isAuthorizationValid(String authorizationHeader) {
        //if (authorizationHeader.equals("default-token")) return true;
        // Logic for checking the value+
        System.out.println("Func isAuthorizationValid ");
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl+":8100/auth/check";
        String response
                = restTemplate.getForObject(url+"/"+authorizationHeader, String.class);
        if (response.equals("invaild")){
            return false;
        } else {
            currentUserId = response;
            return true;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            System.out.println("Request: "+request);

            if (!request.getHeaders().containsKey("Authorization")) {
                return this.onError(exchange, "No Authorization header, no token", HttpStatus.UNAUTHORIZED);
            };

            String token = request.getHeaders().get("Authorization").get(0);

            if (!this.isAuthorizationValid(token)) {
                return this.onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().
                    header("currentUserId", currentUserId).
                    build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}