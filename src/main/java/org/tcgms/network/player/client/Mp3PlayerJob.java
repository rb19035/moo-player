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

    private Player mp3player;

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
            mp3File = (File) jobExecutionContext.getJobDetail().getJobDataMap().get( "mediaLocation" );

            if( mp3File != null )
            {
                LOGGER.debug( "Starting media player job for file {}", mp3File.getName() );

                songBuffer = new byte[(int) mp3File.length()];
                fis = new FileInputStream( mp3File );
                fis.read( songBuffer );

                byteArrayInputStream = new ByteArrayInputStream( songBuffer );
                this.mp3player = new Player( byteArrayInputStream );
                this.mp3player.play();

            } else
            {
                LOGGER.info( "Could not play media file because file was not found." );
            }

        } catch ( JavaLayerException | IOException e )
        {
            LOGGER.error( "Could not play media file {}", mp3File.getName(), e );
            throw new JobExecutionException( e );

        } finally
        {
            if( mp3File!= null )
            {
                LOGGER.debug( "Stopped media player job for file {}", mp3File.getName() );
                mp3File.delete();
            }
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException
    {
        if( this.mp3player != null )
        {
            LOGGER.debug( "Stopping media player" );
            this.mp3player.close();
        }
    }
}