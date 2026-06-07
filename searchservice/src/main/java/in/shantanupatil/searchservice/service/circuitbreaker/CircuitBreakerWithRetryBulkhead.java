package in.shantanupatil.searchservice.service.circuitbreaker;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import in.shantanupatil.searchservice.repository.SearchRepository;
import in.shantanupatil.searchservice.service.AsyncSearchService;
import in.shantanupatil.searchservice.utils.MapperUtils;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Profile("circuit-breaker")
public class CircuitBreakerWithRetryBulkhead implements AsyncSearchService {

    private final SearchRepository searchRepository;
    private final Random random = new Random();

    @CircuitBreaker(name = "searchCircuitBreaker")
    @Retry(name = "searchRetry", fallbackMethod = "fallbackSearchResponse")
    @Bulkhead(name = "searchBulkhead", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "searchTimeout")
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsync(String query) {
        makeRequestSlow();
        List<PlacesSearchEntity> placesSearchEntities = searchRepository.search(query);
        return CompletableFuture.completedFuture(placesSearchEntities.stream()
                .map(MapperUtils::toPlacesSearchDto).toList());
    }

    @CircuitBreaker(name = "searchCircuitBreaker")
    @Retry(name = "searchRetry", fallbackMethod = "fallbackSearchResponse")
    @Bulkhead(name = "fullTextSearchBulkhead", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "searchTimeout")
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsyncFullText(String query) {
        makeRequestSlow();
        List<PlacesSearchEntity> placesSearchEntities = searchRepository.searchFullText(query);
        return CompletableFuture.completedFuture(placesSearchEntities.stream()
                .map(MapperUtils::toPlacesSearchDto).toList());
    }

    private void makeRequestSlow() {
        if (random.nextInt(100) < 50) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private CompletableFuture<List<PlacesSearchDto>> fallbackSearchResponse(String query, Throwable exception) {
        return CompletableFuture.completedFuture(List.of(
                new PlacesSearchDto(
                        -1L,
                        "Fallback Response for " + query,
                        -1L,
                        "System temporarily degraded"
                )
        ));
    }
}
