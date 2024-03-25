package card.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class innerPrint {

    @Id
    private String id;//主键

    private Double X;//X轴方向位移
    private Double Y;//Y轴方向位移
    
    private Double scale = 100.0;//缩放

    private String url;//图片地址

}
