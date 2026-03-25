package goltsman.btbookingservice.service.impl;

import goltsman.btbookingservice.exception.business.ExternalServiceUnavailableException;
import goltsman.btbookingservice.model.external.RestaurantResponse;
import goltsman.btbookingservice.model.external.TableResponse;
import goltsman.btbookingservice.service.RestaurantServiceClient;
import goltsman.btbookingservice.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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
        log.debug("Checking restaurant existence: {}", restaurantId);
        performGet("/api/v2/restaurants/{id}", restaurantId, RestaurantResponse.class);
        log.debug("Restaurant {} exists", restaurantId);
    }

    public TableResponse getTable(Long tableId) {
        log.debug("Fetching table info: {}", tableId);
        return performGet("/api/v2/tables/{id}", tableId, TableResponse.class);
    }

    private <T> T performGet(String uriTemplate, Long id, Class<T> responseType) {
        String token = SecurityUtils.getCurrentBearerToken();
        if (token == null) {
            throw new ExternalServiceUnavailableException("Отсутствует JWT для вызова Restaurant Service");
        }
        try {
            return webClient.get()
                    .uri(restaurantServiceBaseUrl + uriTemplate, id)
                    .headers(h -> h.setBearerAuth(token))
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            resp -> Mono.error(new EntityNotFoundException(
                                    "Ресурс не найден в Restaurant Service: " + uriTemplate.replace("{id}", String.valueOf(id)))))
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> Mono.error(new ExternalServiceUnavailableException(
                                    "Ошибка клиента при вызове Restaurant Service: " + resp.statusCode())))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(new ExternalServiceUnavailableException(
                                    "Ошибка сервера Restaurant Service")))
                    .bodyToMono(responseType)
                    .timeout(Duration.ofSeconds(3))
                    .block();
        } catch (WebClientRequestException ex) {
            throw new ExternalServiceUnavailableException("Не удалось подключиться к Restaurant Service");
        }
    }
}
