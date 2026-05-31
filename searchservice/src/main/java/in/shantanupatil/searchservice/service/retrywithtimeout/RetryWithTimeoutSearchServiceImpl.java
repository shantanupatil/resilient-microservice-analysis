package in.shantanupatil.searchservice.service.retrywithtimeout;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import in.shantanupatil.searchservice.repository.SearchRepository;
import in.shantanupatil.searchservice.service.AsyncSearchService;
import in.shantanupatil.searchservice.utils.MapperUtils;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Profile("retry-async")
public class RetryWithTimeoutSearchServiceImpl implements AsyncSearchService {

    private final SearchRepository searchRepository;
    private final Random random = new Random();
    private final Executor searchExecutor;

    @Retry(
            name = "searchRetry",
            fallbackMethod = "fallbackSearchResponse")
    @TimeLimiter(name = "searchTimeout")
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsync(String query) {
        return CompletableFuture.supplyAsync(() -> {
            makeRequestSlow();

            List<PlacesSearchEntity> placesSearchEntities =
                    searchRepository.search(query);

            return placesSearchEntities.stream()
                    .map(MapperUtils::toPlacesSearchDto)
                    .toList();
        }, searchExecutor);
    }

    @Retry(name = "searchRetry", fallbackMethod = "fallbackSearchResponse")
    @TimeLimiter(name = "searchTimeout")
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsyncFullText(String query) {
        return CompletableFuture.supplyAsync(() -> {
            makeRequestSlow();
            List<PlacesSearchEntity> placesSearchEntities = searchRepository.searchFullText(query);
            return placesSearchEntities.stream()
                    .map(MapperUtils::toPlacesSearchDto)
                    .toList();
        }, searchExecutor);
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

    private CompletableFuture<List<PlacesSearchDto>> fallbackSearchResponse(String query, Exception exception) {
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
