package in.shantanupatil.searchservice.service;

import in.shantanupatil.searchservice.model.PlacesSearchDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncSearchService {
    CompletableFuture<List<PlacesSearchDto>> searchAsync(String query);
}
