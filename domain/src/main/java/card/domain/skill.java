package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@Table("Skill")
public class skill {

    @Id
    private Long id;
    
    private @NonNull String header;
    private @NonNull String description;
}
