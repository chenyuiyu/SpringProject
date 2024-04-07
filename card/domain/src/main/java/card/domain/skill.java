package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@Document
public class skill {

    @Id
    private String id;
    
    private @NonNull String header;
    private @NonNull String description;
}
