package org.tcgms.network.player.client;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcgms.network.player.exception.MooPlayerException;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class FlacPlayerJob extends MediaPlayerJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FlacPlayerJob.class );
    private FlacPlayer flacPlayer;


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
            this.flacPlayer.stop();

            // Delete the media file since we do not need it anymore
            this.mediaFile.delete();
        }
    }

    @Override
    public int pausePlayingMedia() throws MooPlayerException
    {
        return 0;
    }

    @Override
    public void playMedia() throws MooPlayerException
    {
        String fileExtension;

        try
        {
            // Check to make sure the file is Flac
            fileExtension = FilenameUtils.getExtension( this.mediaFile.getName() );
            if( !fileExtension.equalsIgnoreCase( MediaPlayerJob.FLAC_FILE ) )
            {
                LOGGER.error( "Could not start media player job - media file {} is not .flac", this.mediaFile.getAbsolutePath() );
                throw new MooPlayerException( "Could not start media player job - media file is not .flac" );
            }

            //  If the media file is NULL then there is nothing to play
            if( this.mediaFile != null )
            {
                LOGGER.debug( "Starting media player job for file {}", this.mediaFile.getName() );

                this.flacPlayer = new FlacPlayer();
                this.flacPlayer.decode( this.mediaFile.getAbsolutePath() );
//
//                if( this.currentLocationInPausedMedia >= 0 )
//                {
//                    this.mp3Player.play( this.currentLocationInPausedMedia );
//                } else
//                {
//                    this.mp3Player.play();
//                }

            } else
            {
                LOGGER.info( "Could not play media file because file was not found" );
            }

        } catch ( IOException | LineUnavailableException e )
        {
            LOGGER.error( "Could not play media file {}", this.mediaFile.getName(), e );
            throw new MooPlayerException( e );
        }
    }
}
