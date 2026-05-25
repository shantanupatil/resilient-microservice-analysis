package in.shantanupatil.searchservice.controller;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.service.AsyncSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Profile("retry-async")
public class AsyncSearchController {

    private final AsyncSearchService asyncSearchService;

    @GetMapping("/search/async")
    public CompletableFuture<List<PlacesSearchDto>> searchAsync(@RequestParam String query) {
        return asyncSearchService.searchAsync(query);
    }
}
