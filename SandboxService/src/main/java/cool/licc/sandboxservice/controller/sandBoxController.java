package cool.licc.sandboxservice.controller;

import cool.licc.sandbox.model.ExecuteCodeRequest;
import cool.licc.sandbox.model.ExecuteCodeResponse;
import cool.licc.sandbox.template.SandboxTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class sandBoxController {
    @PostMapping("/java-native")
    public ExecuteCodeResponse sandbox(@RequestBody ExecuteCodeRequest executeCodeRequest) throws Exception {
        SandboxTemplate sandboxTemplate = new SandboxTemplate();
        ExecuteCodeResponse executeCodeResponse = sandboxTemplate.executeCode(executeCodeRequest);
        log.warn(executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
