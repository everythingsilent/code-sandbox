package cool.licc.sandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {
    /**
     * 参数输入
     */
    private List<String> inputList;

    /**
     * 用户提交代码
     */
    private String code;

    /**
     * 代码语言
     */
    private String language;
}
