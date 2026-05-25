package in.shantanupatil.searchservice.service.retrywithtimeout;

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

@Service
@RequiredArgsConstructor
@Profile("retry-timeout")
public class RetryWithTimeoutSearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;
    private final Random random = new Random();

    @Retry(name = "searchRetry")
    @Override
    public List<PlacesSearchDto> search(String query) {
        makeRequestSlow();
        List<PlacesSearchEntity> placesSearchEntities = searchRepository.search(query);
        return placesSearchEntities.stream().map(MapperUtils::toPlacesSearchDto)
                .toList();
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
