package in.shantanupatil.searchservice.repository;

import in.shantanupatil.searchservice.model.PlacesSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<PlacesSearchEntity, Long> {

    @Query(value = """
            SELECT * FROM places_search WHERE name ILIKE CONCAT(:query, '%') LIMIT 1000
        """, nativeQuery = true)
    List<PlacesSearchEntity> search(String query);

    @Query(value = """

            SELECT * FROM places_search WHERE name_vector @@ plainto_tsquery('simple', :query) LIMIT 1000;
        """, nativeQuery = true)
    List<PlacesSearchEntity> searchFullText(String query);
}
