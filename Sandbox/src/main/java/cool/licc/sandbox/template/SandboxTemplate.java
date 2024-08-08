package cool.licc.sandbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.StrUtil;
import cool.licc.sandbox.Sandbox;
import cool.licc.sandbox.model.ExecuteCodeRequest;
import cool.licc.sandbox.model.ExecuteCodeResponse;
import cool.licc.sandbox.model.ExecuteMessage;
import cool.licc.sandbox.model.JudgeInfo;
import cool.licc.sandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
public class SandboxTemplate implements Sandbox {
    private static final String CODE_SAVE_DIR = "temp-code";
    private static final String CODE_NAME = "Main";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws Exception {
        // 1. 保存用户代码文件
        String userCodePathDir = saveUserCode(executeCodeRequest.getCode());

        // 2. 编译用户代码
        ExecuteMessage compileCodeMessage = compileCode(userCodePathDir);

        if (StrUtil.isNotBlank(compileCodeMessage.getErrorMessage())) {
            // 返回错误信息
            ExecuteCodeResponse errorResponse = ExecuteCodeResponse.builder()
                    .outputList(new ArrayList<>())
                    .judgeInfo(new JudgeInfo())
                    .status(1)
                    .message(compileCodeMessage.getErrorMessage())
                    .build();
            return errorResponse;
        }

        // 3. 执行获取结果
        List<String> argsList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = runCode(userCodePathDir, argsList);
        // 执行成功返回执行UUID
        executeCodeResponse.setMessage(String.valueOf(
                PathUtil.getLastPathEle(Paths.get(userCodePathDir))
        ));

        // 4. 清除代码文件
        if (FileUtil.exist(userCodePathDir)) {
            FileUtil.del(userCodePathDir);
        }

        return executeCodeResponse;
    }


    private String saveUserCode(String userCode) throws Exception {
        // 代码存放路径
        String globalCodePath = System.getProperty("user.dir") +
                File.separator +
                CODE_SAVE_DIR;
        if (FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }

        // 用户代码保存
        String userCodePathDir = globalCodePath + File.separator + UUID.randomUUID();
        String userCodePathFile = userCodePathDir + File.separator +
                CODE_NAME + ".java";
        FileUtil.writeString(userCode, userCodePathFile, StandardCharsets.UTF_8);
        return userCodePathDir;
    }


    private ExecuteMessage compileCode(String userCodePathDir) throws Exception {
        // 对目标文件进行编译
        String executeCodeFile = userCodePathDir + File.separator +
                CODE_NAME + ".java";
        String compileCmd = String.format("javac -encoding utf-8 %s", executeCodeFile);
        Process execCompile = Runtime.getRuntime()
                .exec(compileCmd);
        ExecuteMessage execMessage = ProcessUtils.getProcessMessage(execCompile, "compile");
        return execMessage;
    }


    private ExecuteCodeResponse runCode(String userCodePathDir, List<String> argsList) throws Exception {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String args : argsList) {
            String runCodeCmd = String.format("java -cp %s %s %s",
                    userCodePathDir,
                    CODE_NAME,
                    args);
            Process execRun = Runtime.getRuntime()
                    .exec(runCodeCmd);

            ExecuteMessage execMessage = ProcessUtils.getProcessMessage(execRun, "running");
            executeMessageList.add(execMessage);
        }

        // 结果整理
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        Long maxTime = 0L;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setStatus(2);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long currentRunTime = executeMessage.getTime();
            if (executeMessage.getTime() != null) {
                maxTime = Math.max(maxTime, currentRunTime);
            }
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);

        executeCodeResponse.setOutputList(outputList);
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(0);
        } else {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}
