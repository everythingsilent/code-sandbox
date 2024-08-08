package cool.licc.sandbox.model;

import lombok.Data;

@Data
public class JudgeInfo {
    /**
     * 判题信息
     */
    private String message;

    /**
     * 使用的最大内存
     */
    private Long memory;

    /**
     * 使用时间
     */
    private Long time;
}
