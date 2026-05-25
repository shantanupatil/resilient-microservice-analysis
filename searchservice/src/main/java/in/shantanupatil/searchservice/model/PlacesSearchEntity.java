package in.shantanupatil.searchservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "places_search")
@Data
@NoArgsConstructor
public class PlacesSearchEntity {
    @Id
    private Long id;
    private String type;
    @Column(name = "entity_id")
    private Long entityId;
    private String name;
}
