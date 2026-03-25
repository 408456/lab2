package goltsman.btreviewservice.service.impl;

import goltsman.btreviewservice.exception.ExternalServiceUnavailableException;
import goltsman.btreviewservice.service.RestaurantServiceClient;
import goltsman.btreviewservice.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceClientImpl implements RestaurantServiceClient {

    private final WebClient webClient;

    @Value("${clients.restaurant-service.base-url}")
    private String restaurantServiceBaseUrl;

    public void checkRestaurantExists(Long restaurantId) {
        String token = SecurityUtils.getCurrentBearerToken();
        if (token == null) {
            throw new ExternalServiceUnavailableException("Отсутствует JWT для вызова Restaurant Service");
        }
        try {
            webClient.get()
                    .uri(restaurantServiceBaseUrl + "/api/v2/restaurants/{id}", restaurantId)
                    .headers(h -> h.setBearerAuth(token))
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            resp -> Mono.error(
                                    new EntityNotFoundException("Ресторан с id=" + restaurantId + " не найден")
                            ))
                    .onStatus(status -> status.value() == 401,
                            resp -> Mono.error(
                                    new BadCredentialsException("Ошибка авторизации между сервисами")
                            ))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(
                                    new Exception("Ошибка Restaurant Service")
                            ))
                    .bodyToMono(Void.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();
        } catch (WebClientRequestException ex) {
            throw new RuntimeException("Не удалось подключиться к Restaurant Service", ex);
        }
    }
}