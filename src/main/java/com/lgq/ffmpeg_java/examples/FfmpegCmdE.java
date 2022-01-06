package com.lgq.ffmpeg_java.examples;

import com.lgq.ffmpeg_java.cmd.FfmpegCmd;
import com.lgq.ffmpeg_java.util.ThreadPoolExecutorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author lgq
 */
public class FfmpegCmdE {

    //执行成功0,失败1
    private static int CODE_SUCCESS = 0;
    private static int CODE_FAIL = 1;

    private static final String BASE_PATH = "/Users/guoqingliu/project/learning/java/ffmpeg_java/";

    private static final String MP4_PATH = BASE_PATH + "rongyao_test.mp4";

    private static final String MOV_PATH = BASE_PATH + "rongyao_test.mov";

    private static final String MOV_WATER_PATH = BASE_PATH + "rongyao_test_water.mov";

    // 将rongyao_test.mp4转换rongyao_test.mov格式
    private static String CMD_MP4_2_MOV = " -i " + MP4_PATH + " -c copy " + MOV_PATH;

    // 将rongyao_test.mov添加水印（28192174@qq.comrongyao_test_water.mov
    private static String cmd_mov_water = " -i " + MOV_PATH + " -vf \"drawtext=fontfile=Arial.ttf: text='28192174@qq.com': y=h-line_h-20:x=(w-text_w)/2: fontsize=34: fontcolor=yellow: shadowy=2\" -b:v 3000k " + MOV_WATER_PATH;

    public static void main(String[] args) {
        //异步执行，获取执行结果code
        CompletableFuture<Integer> completableFutureTask = CompletableFuture.supplyAsync(() ->{
                    return cmdExecute(CMD_MP4_2_MOV);
                }, ThreadPoolExecutorUtils.pool)
                .thenApplyAsync((Integer code)->{
                    if(CODE_SUCCESS != code) {return CODE_FAIL;}
                    System.out.println("第一步：mp4转mov,成功！");

                    Integer codeTmp =  cmdExecute(cmd_mov_water);
                    if(CODE_SUCCESS != codeTmp) {return CODE_FAIL;}
                    System.out.println("第二步：mov添加水印,成功！");
                    return codeTmp;
                }, ThreadPoolExecutorUtils.pool);

        //获取执行结果
        //code=0表示正常
        try {
            System.out.println(String.format("获取最终执行结果:%s", completableFutureTask.get() == CODE_SUCCESS ? "成功！" : "失败！"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws
     * @Description: (执行ffmpeg自定义命令)
     * @param: @param cmdStr
     * @param: @return
     * @return: Integer
     */
    public static Integer cmdExecute(String cmdStr) {
        // code=0表示正常
        Integer code = null;

        FfmpegCmd ffmpegCmd = new FfmpegCmd();

        InputStream errorStream = null;
        try {
            //destroyOnRuntimeShutdown表示是否立即关闭Runtime
            //如果ffmpeg命令需要长时间执行，destroyOnRuntimeShutdown = false

            //openIOStreams表示是不是需要打开输入输出流:
            //	       inputStream = processWrapper.getInputStream();
            //	       outputStream = processWrapper.getOutputStream();
            //	       errorStream = processWrapper.getErrorStream();
            ffmpegCmd.execute(false, true, cmdStr);
            errorStream = ffmpegCmd.getErrorStream();

            //打印过程
            int len = 0;
            while ((len = errorStream.read()) != -1) {
                System.out.print((char) len);
            }

            //code=0表示正常
            code = ffmpegCmd.getProcessExitCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ffmpegCmd.close();
        }
        //返回
        return code;
    }
}
