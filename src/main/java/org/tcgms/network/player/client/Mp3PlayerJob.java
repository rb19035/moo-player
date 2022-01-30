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


public class Mp3PlayerJob implements Job, MediaPlayer
{
    public static final String PAUSE_JOB_DETAIL_MAP_KEY = "pauseKey";
    public static final String MEDIA_PATH_JOB_DETAIL_MAP_KEY = "mediaFileLocation";
    public static final String CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY = "mediaFilePosition";


    private static final Logger LOGGER = LoggerFactory.getLogger( Mp3PlayerJob.class );

    private Player mp3Player;
    private File mediaFile;
    private int currentLocationInPausedMedia = -1;

    @Override
    public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        try
        {
            this.mediaFile = (File) jobExecutionContext.getJobDetail().getJobDataMap().get( MEDIA_PATH_JOB_DETAIL_MAP_KEY );

            Object temp = jobExecutionContext.getJobDetail().getJobDataMap().get( CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY );
            if(  temp != null )
            {
                this.currentLocationInPausedMedia = (Integer) temp;
            }

            this.playMedia();

        } catch( MooPlayerException e )
        {
            LOGGER.error( "Could not start media player job", e );
            throw new JobExecutionException( e );
        }

    }

    @Override
    public void stopPLayingMedia()
    {
        if( this.mediaFile != null )
        {
            LOGGER.debug( "Stopping media player job for file {}", this.mediaFile.getName() );

            this.mp3Player.close();
            this.mediaFile.delete();
        }
    }

    @Override
    public int pausePlayingMedia()
    {
        int currentFilePosition = -1;

        if( this.mediaFile != null )
        {
            LOGGER.debug( "Pausing media player job for file {}", this.mediaFile.getName() );
            currentFilePosition = this.mp3Player.getPosition();

            this.mp3Player.close();
        }

        return currentFilePosition;
    }

    @Override
    public void playMedia()
    {
        FileInputStream fis = null;
        BufferedInputStream in = null;
        byte[] songBuffer;
        ByteArrayInputStream byteArrayInputStream = null;

        try
        {
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