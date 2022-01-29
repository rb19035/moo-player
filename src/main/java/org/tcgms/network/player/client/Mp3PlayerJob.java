package org.tcgms.network.player.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Mp3PlayerJob implements InterruptableJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger( Mp3PlayerJob.class );

    private static Player MP3_PLAYER;

    @Override
    public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        File mp3File = null;
        FileInputStream fis = null;
        BufferedInputStream in = null;
        byte[] songBuffer;
        ByteArrayInputStream byteArrayInputStream = null;

        try
        {
            // If the player is currently processing media close it
            if( MP3_PLAYER != null )
            {
                LOGGER.debug( "Media player already playing media, stopping current media" );

                MP3_PLAYER.close();
            }

            mp3File = (File) jobExecutionContext.getJobDetail().getJobDataMap().get( "mediaLocation" );


            if( mp3File != null )
            {
                LOGGER.debug( "Starting media player job for file {}", mp3File.getName() );

                songBuffer = new byte[(int) mp3File.length()];
                fis = new FileInputStream( mp3File );
                fis.read( songBuffer );

                byteArrayInputStream = new ByteArrayInputStream( songBuffer );
                MP3_PLAYER = new Player( byteArrayInputStream );
                MP3_PLAYER.play();

            } else
            {
                LOGGER.info( "Could not play media file because file was not found" );
            }

        } catch ( JavaLayerException | IOException e )
        {
            LOGGER.error( "Could not play media file {}", mp3File.getName(), e );
            throw new JobExecutionException( e );

        } finally
        {
            if( mp3File!= null )
            {
                mp3File.delete();
            }
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException
    {
        LOGGER.debug( "Stopping media player" );

        if( MP3_PLAYER != null )
        {
            MP3_PLAYER.close();
            LOGGER.debug( "Stopped media player" );
        }
    }
}