package org.tcgms.network.player.client;

import org.tcgms.network.player.exception.MediaPlayerException;

public interface MediaPlayer
{
    public static final String MEDIA_PATH_JOB_DETAIL_MAP_KEY = "mediaFileLocation";
    public static final String CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY = "mediaFilePosition";
    public static final String MP3_FILE = "mp3";
    public static final String FLAC_FILE = "flac";
    public static final String WAV_FILE = "wav";
    public static final String[] SUPPORTED_FILE_EXTENSIONS = { MP3_FILE, FLAC_FILE, WAV_FILE };

    public void play() throws MediaPlayerException;
    public void stop() throws MediaPlayerException;
    public void pause() throws MediaPlayerException;
}
