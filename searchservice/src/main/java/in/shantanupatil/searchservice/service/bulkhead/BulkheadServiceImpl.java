package in.shantanupatil.searchservice.service.bulkhead;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import in.shantanupatil.searchservice.repository.SearchRepository;
import in.shantanupatil.searchservice.service.AsyncSearchService;
import in.shantanupatil.searchservice.utils.MapperUtils;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Profile("bulkhead")
public class BulkheadServiceImpl implements AsyncSearchService {

    private final SearchRepository searchRepository;

    @Bulkhead(name = "searchBulkhead", type = Bulkhead.Type.THREADPOOL)
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsync(String query) {
        List<PlacesSearchEntity> entities = searchRepository.search(query);
        return CompletableFuture.completedFuture(
                entities.stream()
                        .map(MapperUtils::toPlacesSearchDto)
                        .toList()
        );
    }

    @Bulkhead(name = "fullTextSearchBulkhead", type = Bulkhead.Type.THREADPOOL)
    @Override
    public CompletableFuture<List<PlacesSearchDto>> searchAsyncFullText(String query) {
        List<PlacesSearchEntity> entities = searchRepository.searchFullText(query);
        return CompletableFuture.completedFuture(
                entities.stream()
                        .map(MapperUtils::toPlacesSearchDto)
                        .toList()
        );
    }
}
