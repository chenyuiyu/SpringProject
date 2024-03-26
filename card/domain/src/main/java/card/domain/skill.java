package card.domain;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@RequiredArgsConstructor
public class skill {

    @Id
    private Long id;
    
    private @NonNull String header;
    private @NonNull String description;
}
