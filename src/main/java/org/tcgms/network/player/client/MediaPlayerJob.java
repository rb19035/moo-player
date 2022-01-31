package org.tcgms.network.player.client;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcgms.network.player.exception.MooPlayerException;

import java.io.File;

public abstract class MediaPlayerJob implements Job
{
    public static final String MEDIA_PATH_JOB_DETAIL_MAP_KEY = "mediaFileLocation";
    public static final String CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY = "mediaFilePosition";
    public static final String MP3_FILE = "mp3";
    public static final String FLAC_FILE = "flac";
    public static final String WAV_FILE = "wav";
    public static final String[] SUPPORTED_FILE_EXTENSIONS = { MP3_FILE, FLAC_FILE, WAV_FILE };

    protected File mediaFile;
    protected int currentLocationInPausedMedia = -1;

    private static final Logger LOGGER = LoggerFactory.getLogger( MediaPlayerJob.class );

    abstract public void stopPLayingMedia() throws MooPlayerException;
    abstract public int pausePlayingMedia() throws MooPlayerException;
    abstract public void playMedia() throws MooPlayerException;

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
            if( !this.mediaFile.exists() || !this.mediaFile.isFile() || !this.mediaFile.canRead() )
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
}
