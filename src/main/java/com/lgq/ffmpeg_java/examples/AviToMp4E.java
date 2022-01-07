package com.lgq.ffmpeg_java.examples;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

public class AviToMp4E {
    private static Logger logger = Logger.getLogger(AviToMp4E.class.getName());

    public  void AviToMp4(String oldPath,String newPath,UploadRecord uploadRecord,String type) {
//		File source = new File("C:\\shiping\\1.avi");
//	    File target = new File("C:\\shiping\\2019-06-27333333测试.mp4");
        File source = new File(oldPath);
        File target = new File(newPath);
        logger.info("转换前的路径:"+oldPath);
        logger.info("转换后的路径:"+newPath);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame"); //音频编码格式
        audio.setBitRate(new Integer(800000));
        audio.setChannels(new Integer(1));
        //audio.setSamplingRate(new Integer(22050));

        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");//视频编码格式
        video.setBitRate(new Integer(3200000));
        video.setFrameRate(new Integer(15));//数字设置小了，视频会卡顿

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setInputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        Encoder encoder = new Encoder();
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
            logger.info("avi转MP4 --- 转换开始:"+new Date());
            encoder.encode(multimediaObject, target, attrs);
            logger.info("avi转MP4 --- 转换结束:"+new Date());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InputFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (EncoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
