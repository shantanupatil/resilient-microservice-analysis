package in.shantanupatil.searchservice.service.retry;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import in.shantanupatil.searchservice.repository.SearchRepository;
import in.shantanupatil.searchservice.service.SearchService;
import in.shantanupatil.searchservice.utils.MapperUtils;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/***
 * Retry mechanisms improved request completion rates under transient failure conditions.
 * However, under high concurrency and scalability pressure, retries amplified latency, increased resource utilization,
 * and contributed to system-wide degradation patterns including thread retention and throughput instability.
 */
@Service
@RequiredArgsConstructor
@Profile("retry")
public class RetrySearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;
    private final Random random = new Random();

    @Retry(
            name = "searchRetry",
            fallbackMethod = "fallbackSearchResponse"
    )
    @Override
    public List<PlacesSearchDto> search(String query) {
        throwTransientError();
        List<PlacesSearchEntity> placesSearchEntities = searchRepository.search(query);
        return placesSearchEntities.stream().map(MapperUtils::toPlacesSearchDto)
                .toList();
    }

    private void throwTransientError() {
        if (random.nextInt(100) < 50) {
            throw new RuntimeException(
                    "Simulated transient failure"
            );
        }
    }

    private List<PlacesSearchDto> fallbackSearchResponse(String query, Exception ex) {
        return List.of(
                new PlacesSearchDto(
                        -1L,
                        "Fallback Response for " + query,
                        -1L,
                        "System temporarily degraded"
                )
        );
    }
}
