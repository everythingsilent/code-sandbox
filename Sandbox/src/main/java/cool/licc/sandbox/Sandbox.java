package cool.licc.sandbox;

import cool.licc.sandbox.model.ExecuteCodeRequest;
import cool.licc.sandbox.model.ExecuteCodeResponse;

public interface Sandbox {
    /**
     * 执行代码程序
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws Exception;
}
