package com.coursehub.course_service.client;

import com.coursehub.course_service.config.FeignConfig;
import com.coursehub.course_service.dto.response.UserSelfResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "identity-service",
        path = "/v1/user",
        configuration = FeignConfig.class

)
public interface IdentityServiceClient {

    Logger log = LoggerFactory.getLogger(IdentityServiceClient.class);

    @GetMapping("/self")
    @CircuitBreaker(name = "getUserInfoByIdCircuitBreaker", fallbackMethod = "getSelfFallBack")
    ResponseEntity<UserSelfResponse> getSelf();

    default ResponseEntity<UserSelfResponse> getSelfFallBack(Throwable throwable) {

        log.warn("Fallback triggered for getSelf, Reason: {}", throwable.getMessage());

        return ResponseEntity.ok(new UserSelfResponse(
                "Author info is currently unreachable.",
                null));
    }

}
