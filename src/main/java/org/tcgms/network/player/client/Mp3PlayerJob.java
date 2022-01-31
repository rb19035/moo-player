package org.tcgms.network.player.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcgms.network.player.exception.MooPlayerException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Class encapsulates logic to play, pause, and stop a MP3 file.
 */

public class Mp3PlayerJob extends MediaPlayerJob
{
    public static final String PAUSE_JOB_DETAIL_MAP_KEY = "pauseKey";
    public static final String MEDIA_PATH_JOB_DETAIL_MAP_KEY = "mediaFileLocation";
    public static final String CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY = "mediaFilePosition";

    private static final Logger LOGGER = LoggerFactory.getLogger( Mp3PlayerJob.class );
    private Player mp3Player;


    /**
     * Method is implemented for the MooPLayer MediaPlayer interface.  When called this method stops playing the
     * currently media file and deletes the media file from storage.
     */
    @Override
    public void stopPLayingMedia()
    {
        //  If the media file is NULL then there is nothing to stop...else stop the currently playing file
        if( this.mediaFile != null )
        {
            LOGGER.debug( "Stopping media player job for file {}", this.mediaFile.getName() );

            // Close/Stop the media player
            this.mp3Player.close();

            // Delete the media file since we do not need it anymore
            this.mediaFile.delete();
        }
    }

    /**
     * Method is implemented for the MooPLayer MediaPlayer interface.  When called this method stops playing the
     * currently media file and returns the current position in the media file being played.  Note, the file is not
     * deleted from storage.
     *
     * @return int current media player position
     */
    @Override
    public int pausePlayingMedia()
    {
        int currentFilePosition = -1;

        //  If the media file is NULL then there is nothing to pause/stop...else pause/stop the currently playing file
        if( this.mediaFile != null )
        {
            LOGGER.debug( "Pausing media player job for file {}", this.mediaFile.getName() );

            // Get the current position on the media file
            currentFilePosition = this.mp3Player.getPosition();

            // Close/Stop the media player
            this.mp3Player.close();
        }

        // Return the current file position so it can be started again at the same point
        return currentFilePosition;
    }

    /**
     * Method is implemented for the MooPLayer MediaPlayer interface.  When called this method plays the media file
     * passed to the job.
     */
    @Override
    public void playMedia()
    {
        FileInputStream fis = null;
        BufferedInputStream in = null;
        byte[] songBuffer = null;
        ByteArrayInputStream byteArrayInputStream = null;
        String fileExtension = null;

        try
        {
            fileExtension = FilenameUtils.getExtension( this.mediaFile.getName() );
            if( !fileExtension.equalsIgnoreCase( MediaPlayerJob.MP3_FILE ) )
            {
                LOGGER.error( "Could not start media player job - media file {} is not .mp3", this.mediaFile.getAbsolutePath() );
                throw new MooPlayerException( "Could not start media player job - media file is not .mp3" );
            }

            //  If the media file is NULL then there is nothing to play
            if( this.mediaFile != null )
            {
                LOGGER.debug( "Starting media player job for file {}", this.mediaFile.getName() );

                songBuffer = new byte[(int) this.mediaFile.length()];
                fis = new FileInputStream( this.mediaFile );
                fis.read( songBuffer );

                byteArrayInputStream = new ByteArrayInputStream( songBuffer );
                this.mp3Player = new Player( byteArrayInputStream );

                if( this.currentLocationInPausedMedia >= 0 )
                {
                    this.mp3Player.play( this.currentLocationInPausedMedia );
                } else
                {
                    this.mp3Player.play();
                }

            } else
            {
                LOGGER.info( "Could not play media file because file was not found" );
            }

        } catch ( JavaLayerException | IOException e )
        {
            LOGGER.error( "Could not play media file {}", this.mediaFile.getName(), e );
            throw new MooPlayerException( e );

        } finally
        {
            this.mp3Player.close();
        }
    }
}