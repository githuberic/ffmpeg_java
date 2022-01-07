package com.lgq.ffmpeg_java.examples;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author lgq
 */
public class AudioVideoE {
    private static Logger logger = Logger.getLogger(AudioVideoE.class.getName());

    public static boolean videoToAudio(String videoPath, String audioPath) {
        File fileMp4 = new File(videoPath);
        File fileMp3 = new File(audioPath);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);

        Encoder encoder = new Encoder();

        MultimediaObject mediaObject = new MultimediaObject(fileMp4);
        try {
            encoder.encode(mediaObject, fileMp3, attrs);
            logger.info("File MP4 convertito MP3");
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }
}
