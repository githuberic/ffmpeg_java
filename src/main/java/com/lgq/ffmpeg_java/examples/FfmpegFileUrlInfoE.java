package com.lgq.ffmpeg_java.examples;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

/**
 * @author lgq
 */
public class FfmpegFileUrlInfoE {
    private static Logger logger = Logger.getLogger(FfmpegFileUrlInfoE.class.getName());

    public static void main(String[] args) throws InputFormatException, EncoderException, MalformedURLException {
        URL fileURL = new URL("http://spacefly.cn/assets2/corner.mp4");
        MultimediaObject instance = new MultimediaObject(fileURL);
        MultimediaInfo result = instance.getInfo();

        logger.info(String.format("视频大小（Byte）:%s", getFileSize(fileURL)));
        logger.info(String.format("视频大小（KB）:%s", new BigDecimal(getFileSize(fileURL) + "").divide(new BigDecimal("1024"), BigDecimal.ROUND_UP).doubleValue()));
        logger.info(String.format("视频大小（MB）:%s", new BigDecimal(getFileSize(fileURL) + "").divide(new BigDecimal("1024")).divide(new BigDecimal("1024")).doubleValue()));
        logger.info(String.format("视频真实格式:%s", result.getFormat()));
        logger.info(String.format("视频时长（毫秒）:%s", result.getDuration()));
        logger.info(String.format("视频宽：%s，高:%s", result.getVideo().getSize().getWidth(),result.getVideo().getSize().getHeight()));
        logger.info(String.format("视频比特率（bit rate）:%s", result.getVideo().getBitRate()));
        logger.info(String.format("视频信息:%s", JSON.toJSONString(result.getMetadata())));
        logger.info(String.format("视频Video信息:%s", JSON.toJSONString(result.getVideo())));
        logger.info(String.format("视频Audio信息:%s", JSON.toJSONString(result.getAudio())));
    }

    /**
     * @Description:获取URL文件大小
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/11/19 14:04
     */
    private static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }
}
