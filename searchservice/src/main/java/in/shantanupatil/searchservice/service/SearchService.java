package in.shantanupatil.searchservice.service;

import in.shantanupatil.searchservice.model.PlacesSearchDto;

import java.util.List;

public interface SearchService {
    List<PlacesSearchDto> search(String query);
}
