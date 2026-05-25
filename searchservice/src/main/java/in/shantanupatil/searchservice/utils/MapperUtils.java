package in.shantanupatil.searchservice.utils;

import in.shantanupatil.searchservice.model.PlacesSearchDto;
import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    public static PlacesSearchDto toPlacesSearchDto(PlacesSearchEntity placesSearchEntity) {
        return new PlacesSearchDto(
                placesSearchEntity.getId(),
                placesSearchEntity.getType(),
                placesSearchEntity.getEntityId(),
                placesSearchEntity.getName()
        );
    }

}
