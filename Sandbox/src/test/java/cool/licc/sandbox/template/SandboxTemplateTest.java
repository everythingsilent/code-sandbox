package cool.licc.sandbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cool.licc.sandbox.model.ExecuteCodeRequest;
import cool.licc.sandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class SandboxTemplateTest {
    @Test
    public void javaNativeCodeSandboxTest() throws Exception {
        SandboxTemplate sandboxTemplate = new SandboxTemplate();
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(Arrays.asList("1 2", "1212 4"))
                .code(FileUtil.readString("E:\\Self\\code-sandbox\\Sandbox\\src\\main\\resources\\test-code\\Main.java", StandardCharsets.UTF_8))
                .language("java")
                .build();
        ExecuteCodeResponse executeCodeResponse = sandboxTemplate.executeCode(executeCodeRequest);
        log.debug(executeCodeResponse.toString());
    }
}
