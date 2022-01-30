package org.tcgms.network.player.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcgms.network.player.exception.MooPlayerException;

import java.io.*;


/**
 * Class encapsulates logic to play, pause, and stop a MP3 file.
 */

public class Mp3PlayerJob implements Job, MediaPlayer
{
    public static final String PAUSE_JOB_DETAIL_MAP_KEY = "pauseKey";
    public static final String MEDIA_PATH_JOB_DETAIL_MAP_KEY = "mediaFileLocation";
    public static final String CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY = "mediaFilePosition";


    private static final Logger LOGGER = LoggerFactory.getLogger( Mp3PlayerJob.class );

    private Player mp3Player;
    private File mediaFile;
    private int currentLocationInPausedMedia = -1;

    /**
     * Method implemented for the Quartz Job interface.  When called by the Quartz Scheduler this method will
     * play or pause a media file.  Note, the playMedia() method blocks until the media is done playing.
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        try
        {
            // Get the media file's Path obj from the Quartz JopDataMap
            this.mediaFile = (File) jobExecutionContext.getJobDetail().getJobDataMap().get( MEDIA_PATH_JOB_DETAIL_MAP_KEY );

            // Make sure we were provided a media file Path obj to work with
            if( this.mediaFile == null )
            {
                LOGGER.error( "Could not start media player job - Media file reference is NULL" );
                throw new JobExecutionException();
            }

            // Check to make sure the media file's Path obj points to something we can work with
            if( !this.mediaFile.exists() || this.mediaFile.isFile() || !this.mediaFile.canRead() )
            {
                LOGGER.error( "Could not start media player job - Cannot access media file {}", this.mediaFile.getAbsolutePath() );
                throw new JobExecutionException();
            }

            // Check to see if we are being asked to restart a paused media file by checking to see if a start position was provided
            Object temp = jobExecutionContext.getJobDetail().getJobDataMap().get( CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY );
            if(  temp != null )
            {
                this.currentLocationInPausedMedia = (Integer) temp;
            }

            //  Play the media file
            this.playMedia();

        } catch( MooPlayerException e )
        {
            LOGGER.error( "Could not start media player job", e );
            throw new JobExecutionException( e );
        }

    }

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
        byte[] songBuffer;
        ByteArrayInputStream byteArrayInputStream = null;

        try
        {
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