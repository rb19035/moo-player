package org.tcgms.network.player.client;


import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.tcgms.network.player.exception.MediaFileNotSupportedException;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.TimerTask;

public class MediaPlayerTask extends TimerTask
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MediaPlayerTask.class );
    private final MediaPlayer mediaPlayer;


    public MediaPlayerTask( final Path mediaFilePath ) throws MediaFileNotSupportedException
    {
        this.mediaPlayer = this.createMediaPlayer( mediaFilePath );
    }

    @Override
    public void run()
    {
        this.mediaPlayer.play();
    }

    @Override
    public boolean cancel()
    {
        this.mediaPlayer.stop();

        return true;
    }

    @Validated
    public MediaPlayer createMediaPlayer( @NotNull final Path mediaFilePath ) throws MediaFileNotSupportedException
    {
        String mediaFileExtension = FilenameUtils.getExtension( mediaFilePath.getFileName().toString() );

        LOGGER.debug( "Creating media player for file extension {}", mediaFileExtension );

        if( mediaFileExtension.equalsIgnoreCase( MediaPlayer.FLAC_FILE ) )
        {
            return  new FlacPlayer( mediaFilePath );

        } else if( mediaFileExtension.equalsIgnoreCase( MediaPlayer.MP3_FILE ) )
        {
            return new MP3Player( mediaFilePath );

        } else
        {
            LOGGER.debug( "Could not create a media player for media file extension {}", mediaFileExtension );
            throw new MediaFileNotSupportedException();
        }
    }


}
