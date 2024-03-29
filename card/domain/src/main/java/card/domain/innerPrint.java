package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Table("InnerPrint")
public class innerPrint {

    @Id
    private Long id;//主键

    private Double X = 0.0;//X轴方向位移
    private Double Y = 0.0;//Y轴方向位移
    
    private Double scale = 100.0;//缩放

    private @NonNull String url;//图片地址

}
