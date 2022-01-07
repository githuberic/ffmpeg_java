package com.lgq.ffmpeg_java.examples;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author lgq
 */
public class VideoE {
    private static Logger logger = Logger.getLogger(VideoE.class.getName());

    public static boolean videoToVideo(String videoSource, String videoTarget) {
        long start = System.currentTimeMillis();

        File source = new File(videoSource);
        File target = new File(videoTarget);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(236000 / 2);
        audio.setChannels(2);
        audio.setSamplingRate(8000);

        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setBitRate(1000000);
        video.setFrameRate(25);
        video.setQuality(4);
//        video.setSize(new VideoSize(720, 480));

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);


        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(encoder.getUnhandledMessages());
            return false;
        } finally {
//            time = new Date();
//            System.out.println(simpleDateFormat.format(time));
            long end = System.currentTimeMillis();
            System.out.println("总耗时：" + (end - start) + "ms");
        }
    }
}
