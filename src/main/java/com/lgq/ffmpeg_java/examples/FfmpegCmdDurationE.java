package com.lgq.ffmpeg_java.examples;

import com.lgq.ffmpeg_java.cmd.FfmpegCmd;
import com.lgq.ffmpeg_java.util.ThreadPoolExecutorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lgq
 */
public class FfmpegCmdDurationE {
    private final static String DURATION_START = "Duration:";

    private final static String KEY_FOR_HOUR = "hour";
    private final static String KEY_FOR_MINUTE = "minute";
    private final static String KEY_FOR_SECONED = "seconed";
    private final static String KEY_FOR_MILLSECONED = "millseconed";

    // 小时 * 60 = 分
    // 分 * 60 = 秒
    private final static BigDecimal GAP_60 = new BigDecimal("60");
    //秒* 1000 = 毫秒
    private final static BigDecimal GAP_1000 = new BigDecimal("1000");

    /**
     * TYPE_0:小时
     * TYPE_1:分钟
     * TYPE_2:秒钟
     * TYPE_3:毫秒
     */
    public final static int TYPE_0 = 0;
    public final static int TYPE_1 = 1;
    public final static int TYPE_2 = 2;
    public final static int TYPE_3 = 3;

    //ffmpeg执行命令
    private final static String cmd_4_info = "-i /Users/guoqingliu/project/learning/java/ffmpeg_java/rongyao_test.mp4";

    public static void main(String[] args) {
        try {
            System.out.println(String.format("获取时长：%s 小时", duration(cmd_4_info, TYPE_0).doubleValue()));
            System.out.println(String.format("获取时长：%s 分钟", duration(cmd_4_info, TYPE_1).doubleValue()));
            System.out.println(String.format("获取时长：%s 秒钟", duration(cmd_4_info, TYPE_2).doubleValue()));
            System.out.println(String.format("获取时长：%s 毫秒", duration(cmd_4_info, TYPE_3).doubleValue()));
            System.out.println("------------------------------------------------------------");
            System.out.println(String.format("获取时长（格式化）：%s", durationFormat(cmd_4_info)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws
     * @Description: (获取格式化的时间duration ： such as : 00 : 01 : 03.32)
     * @param: @param cmd_4_info
     * @param: @return
     * @param: @throws Exception
     * @return: BigDecimal
     */
    public static String durationFormat(final String cmd_4_info) {
        String duration = null;
        try {
            CompletableFuture<String> completableFutureTask = CompletableFuture.supplyAsync(() -> {
                String durationStr = null;
                //执行ffmpeg命令
                StringBuffer sText = getErrorStreamText(cmd_4_info);
                if (null != sText && sText.indexOf(DURATION_START) > -1) {
                    String stextOriginal = sText.toString();
                    //正则解析时间
                    Matcher matcher = patternDuration().matcher(stextOriginal);
                    //正则提取字符串
                    while (matcher.find()) {
                        //such as:00:01:03.32
                        durationStr = matcher.group(1);
                        break;
                    }
                }
                return durationStr;
            }, ThreadPoolExecutorUtils.pool);

            duration = completableFutureTask.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return duration;
    }

    /**
     * @throws Exception
     * @throws
     * @Description: (获取时长)
     * @param: @param cmd_4_info ffmpeg命令，如：-i I:\\rongyao_test.mp4
     * @param: @param type 类型：
     * TYPE_0:小时
     * TYPE_1:分钟
     * TYPE_2:秒钟
     * TYPE_3:毫秒
     * @param: @return
     * @return: BigDecimal
     */
    public static BigDecimal duration(final String cmd_4_info, int type) throws Exception {
        BigDecimal duration = new BigDecimal("00");
        Map<String, String> map = null;
        try {
            CompletableFuture<Map<String, String>> completableFutureTask = CompletableFuture.supplyAsync(() -> {
                Map<String, String> mapTmp = null;

                //执行ffmpeg命令
                StringBuffer sText = getErrorStreamText(cmd_4_info);
                if (null != sText && sText.indexOf(DURATION_START) > -1) {
                    String stextOriginal = sText.toString();

                    //正则解析时间
                    Matcher matcher = patternDuration().matcher(stextOriginal);

                    //正则提取字符串
                    while (matcher.find()) {
                        //such as:00:01:03.32
                        String durationStr = matcher.group(1);
                        mapTmp = getHourMinuteSeconedMillseconed(durationStr);
                        break;
                    }
                }
                return mapTmp;
            }, ThreadPoolExecutorUtils.pool);

            map = completableFutureTask.get();
            if (null != map && map.size() > 0) {
                switch (type) {
                    case TYPE_0:
                        //小时
                        duration = duration.add(new BigDecimal(map.get(KEY_FOR_HOUR)));
                        break;
                    case TYPE_1:
                        //分钟
                        duration = duration.add(new BigDecimal(map.get(KEY_FOR_HOUR)).multiply(GAP_60))
                                .add(new BigDecimal(map.get(KEY_FOR_MINUTE)));
                        break;
                    case TYPE_2:
                        //秒
                        duration = duration.add(new BigDecimal(map.get(KEY_FOR_HOUR)).multiply(GAP_60).multiply(GAP_60))
                                .add(new BigDecimal(map.get(KEY_FOR_MINUTE)).multiply(GAP_60))
                                .add(new BigDecimal(map.get(KEY_FOR_SECONED)));
                        break;
                    case TYPE_3:
                        //毫秒
                        duration = duration.add(new BigDecimal(map.get(KEY_FOR_HOUR)).multiply(GAP_60).multiply(GAP_60).multiply(GAP_1000))
                                .add(new BigDecimal(map.get(KEY_FOR_MINUTE)).multiply(GAP_60).multiply(GAP_1000))
                                .add(new BigDecimal(map.get(KEY_FOR_SECONED)).multiply(GAP_1000))
                                .add(new BigDecimal(map.get(KEY_FOR_MILLSECONED)));
                        break;
                    default:
                        throw new Exception("未知的类型！");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return duration;
    }

    //模式
    public static Pattern patternDuration() {
        //"(?i)duration:\\s*([0-9\\:\\.]+)"-->匹配到时分秒即可，毫秒不需要
        return Pattern.compile("(?i)duration:\\s*([0-9\\:\\.]+)");
    }

    /**
     * @throws
     * @Description: (获取错误流)
     * @param: @param cmd_4_info
     * @param: @return
     * @return: StringBuffer
     */
    private static StringBuffer getErrorStreamText(String cmdStr) {
        //返回的text
        StringBuffer sText = new StringBuffer();
        FfmpegCmd ffmpegCmd = new FfmpegCmd();
        try {
            ;
            //错误流
            InputStream errorStream = null;
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
                char t = (char) len;
                System.out.print(t);
                sText.append(t);
            }

            //code=0表示正常
            ffmpegCmd.getProcessExitCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            ffmpegCmd.close();
        }
        //返回
        return sText;
    }

    /**
     * @throws
     * @Description: (获取duration的时分秒毫秒)
     * @param: durationStr  such as:00:01:03.32
     * @return: Map
     */
    private static Map<String, String> getHourMinuteSeconedMillseconed(String durationStr) {
        HashMap<String, String> map = new HashMap<>(4);
        if (null != durationStr && durationStr.length() > 0) {
            String[] durationStrArr = durationStr.split("\\:");
            String hour = durationStrArr[0];
            String minute = durationStrArr[1];

            //特殊
            String seconed = "00";
            String millseconed = "00";
            String seconedTmp = durationStrArr[2];
            if (seconedTmp.contains("\\.")) {
                String[] seconedTmpArr = seconedTmp.split("\\.");
                seconed = seconedTmpArr[0];
                millseconed = seconedTmpArr[1];
            } else {
                seconed = seconedTmp;
            }
            map.put(KEY_FOR_HOUR, hour);
            map.put(KEY_FOR_MINUTE, minute);
            map.put(KEY_FOR_SECONED, seconed);
            map.put(KEY_FOR_MILLSECONED, millseconed);
        }
        return map;
    }
}
