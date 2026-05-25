package in.shantanupatil.searchservice.model;

public record PlacesSearchDto (
    Long id,
    String type,
    Long entityId,
    String name
) {}
