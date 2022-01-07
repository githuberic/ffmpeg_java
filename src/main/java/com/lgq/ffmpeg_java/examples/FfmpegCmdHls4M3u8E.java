package com.lgq.ffmpeg_java.examples;

import com.lgq.ffmpeg_java.cmd.FfmpegCmd;
import com.lgq.ffmpeg_java.util.ThreadPoolExecutorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.lgq.ffmpeg_java.examples.Consts.CODE_FAIL;
import static com.lgq.ffmpeg_java.examples.Consts.CODE_SUCCESS;

/**
 * @author lgq
 */
public class FfmpegCmdHls4M3u8E {
    //将荣耀视频测试.mp4转换荣耀视频测试_转码.mov格式
    private static String cmd_mp4_2_ts = " -y -i /Users/guoqingliu/project/learning/java/ffmpeg_java/rongyao_test.mp4  -vcodec copy -acodec copy -vbsf h264_mp4toannexb /Users/guoqingliu/project/learning/java/ffmpeg_java/rongyao_test_ts.ts ";

    //将荣耀视频测试_转码.mov添加水印（2356899074@qq.com）荣耀视频测试_转码_水印.mov
    private static String cmd_ts_split = " -i /Users/guoqingliu/project/learning/java/ffmpeg_java/rongyao_test_ts.ts -c copy -map 0 -f segment -segment_list D:\\test-ffmpeg\\荣耀视频测试_m3u8.m3u8 -segment_time 15 D:\\test-ffmpeg\\15s_%3d.ts ";


    /**
     * 第一步：视频整体转码ts
     * 第二步：ts 文件切片
     * @param: @param args
     * @return: void
     * @throws
     */
    public static void main(String[] args) {
        // 异步执行，获取执行结果code
        CompletableFuture<Integer> completableFutureTask = CompletableFuture.supplyAsync(() -> cmdExecute(cmd_mp4_2_ts), ThreadPoolExecutorUtils.pool)
                .thenApplyAsync((Integer code)->{
                    if(CODE_SUCCESS != code) {return CODE_FAIL;}
                    System.out.println("第一步：视频整体转码ts,成功！");
                    Integer codeTmp =  cmdExecute(cmd_ts_split);
                    if(CODE_SUCCESS != codeTmp) {return CODE_FAIL;}
                    System.out.println("第二步：ts 文件切片,成功！");
                    return code;
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
     *
     * @Description: (执行ffmpeg自定义命令)
     * @param: @param cmdStr
     * @param: @return
     * @return: Integer
     * @throws
     */
    public static Integer cmdExecute(String cmdStr) {
        //code=0表示正常
        Integer code  = null;
        FfmpegCmd ffmpegCmd = new FfmpegCmd();
        /**
         * 错误流
         */
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
            while ((len=errorStream.read())!=-1){
                System.out.print((char)len);
            }

            //code=0表示正常
            code = ffmpegCmd.getProcessExitCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            ffmpegCmd.close();
        }
        //返回
        return code;
    }
}
