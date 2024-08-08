package cool.licc.sandbox.utils;

import cn.hutool.core.date.StopWatch;
import cool.licc.sandbox.model.ExecuteMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessUtils {
    public static ExecuteMessage getProcessMessage(Process process, String opName) throws Exception{
        ExecuteMessage executeMessage = new ExecuteMessage();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int exitValue = process.waitFor();
        if (exitValue == 0) {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String processMessageLine;
            StringBuilder processMessage = new StringBuilder();
            while ((processMessageLine = bufferedReader.readLine()) != null){
                processMessage.append(processMessageLine);
            }

            executeMessage.setExitValue(exitValue);
            executeMessage.setMessage(
                    processMessage.equals("") ? String.format("%s success", opName) : processMessage.toString());
        }else {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String errorLine;
            StringBuilder errorMessage = new StringBuilder();
            while ((errorLine = bufferedReader.readLine()) != null){
                errorMessage.append(errorLine);
            }

            executeMessage.setExitValue(exitValue);
            executeMessage.setErrorMessage(errorMessage.toString());
        }
        stopWatch.stop();
        executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        return executeMessage;
    }
}
