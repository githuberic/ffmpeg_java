package com.lgq.ffmpeg_java.examples;

import com.alibaba.fastjson.JSON;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * @author lgq
 */
public class FfmpegFileInfoE {
    private static Logger logger = Logger.getLogger(FfmpegFileInfoE.class.getName());

    public static void main(String[] args) throws EncoderException {
        File file = new File("/Users/guoqingliu/project/learning/java/ffmpeg_java/rongyao_test.mp4");
        MultimediaObject instance = new MultimediaObject(file);
        MultimediaInfo result = instance.getInfo();

        logger.info(String.format("视频大小（Byte）:%s", file.length()));
        logger.info(String.format("视频大小（KB）:%s", new BigDecimal(file.length() + "").divide(new BigDecimal("1024"), BigDecimal.ROUND_UP).doubleValue()));
        logger.info(String.format("视频真实格式:%s", result.getFormat()));
        logger.info(String.format("视频时长（毫秒）:%s", result.getDuration()));
        logger.info(String.format("视频宽：%s，高:%s", result.getVideo().getSize().getWidth(), result.getVideo().getSize().getHeight()));
        logger.info(String.format("视频比特率（bit rate）:%s", result.getVideo().getBitRate()));
        logger.info(String.format("视频信息:%s", JSON.toJSONString(result.getMetadata())));
        logger.info(String.format("视频Video信息:%s", JSON.toJSONString(result.getVideo())));
        logger.info(String.format("视频Audio信息:%s", JSON.toJSONString(result.getAudio())));
    }
}
