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
public class ExecuteCodeResponse {
    /**
     * 执行结果列表
     */
    private List<String> outputList;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 状态码 0正常 1数量不匹配异常 2异常
     */
    private Integer status;

    /**
     * 判题执行信息
     */
    private JudgeInfo judgeInfo;
}
