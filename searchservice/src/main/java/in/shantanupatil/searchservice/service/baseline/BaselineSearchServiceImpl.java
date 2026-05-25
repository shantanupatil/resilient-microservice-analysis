package in.shantanupatil.searchservice.service.baseline;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import in.shantanupatil.searchservice.repository.SearchRepository;
import in.shantanupatil.searchservice.service.SearchService;
import in.shantanupatil.searchservice.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("baseline")
@RequiredArgsConstructor
public class BaselineSearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public List<PlacesSearchDto> search(String query) {
        List<PlacesSearchEntity> placesSearchEntities = searchRepository.search(query);
        return placesSearchEntities.stream().map(MapperUtils::toPlacesSearchDto)
                .toList();
    }
}
