package github.xuanyunyue.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author： zyx1128
 * @create： 2024/1/16 11:19
 * @description：TODO
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeckillSession {
    private Long id;
    private String sessionDate;
    private String sessionTime;
}
