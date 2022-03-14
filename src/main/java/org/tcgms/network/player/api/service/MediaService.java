package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.tcgms.network.player.api.bo.MooPlayerMediaStatus;
import org.tcgms.network.player.client.MediaPlayerTask;
import org.tcgms.network.player.exception.MediaFileNotSupportedException;
import org.tcgms.network.player.exception.MooPlayerException;
import org.tcgms.network.player.exception.MooPlayerNoMediaQueuedException;
import org.tcgms.network.player.wrapper.MooPlayerAppStateWrapper;
import org.tcgms.network.player.wrapper.TimerWrapper;

import javax.validation.ValidationException;
import javax.validation.constraints.PositiveOrZero;
import java.nio.file.Path;


@Service
public class MediaService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MediaService.class );

    private static final String PLAYER_START_ERROR_MSG = "Could not process request to schedule job to start playing media {}";
    private static final String PLAYER_STOP_ERROR_MSG = "Could not process request to stop playing current media file";

    private final MooPlayerAppStateWrapper mooPlayerAppStateWrapper;
    private final TimerWrapper timerWrapper;


    public MediaService( @Autowired MooPlayerAppStateWrapper mooPlayerAppStateWrapper,
                         @Autowired TimerWrapper timerWrapper )
    {
        if( mooPlayerAppStateWrapper == null || timerWrapper == null )
        {
            throw new ValidationException();
        }

        this.mooPlayerAppStateWrapper = mooPlayerAppStateWrapper;
        this.timerWrapper = timerWrapper;
    }


    @Validated
    public void playMediaFile( @PositiveOrZero final long startTimeMilliseconds )
    {
        MediaPlayerTask mediaPlayerTask = null;
        Path mediaFilePath = null;

        try
        {
            LOGGER.debug( "Processing requested to play media file with start time of {} milliseconds ", startTimeMilliseconds );

            if( this.mooPlayerAppStateWrapper.getPlayerStatus().currentMediaPlayStatus().equals( MooPlayerMediaStatus.PLAYING_MUSIC ) )
            {
                // The player is currently playing a media file we and now have to stop the currently playing media to start playing the new requested media
                this.stopPlayingMedia();
            }

            // Get the next file from the queue to be played
            mediaFilePath = this.mooPlayerAppStateWrapper.updateStatusToPlaying();
            if( mediaFilePath == null )
            {
                LOGGER.debug( "Media service could not play play media file {}", mediaFilePath );
                throw new MooPlayerNoMediaQueuedException();
            }

            // Create a task to play the requested media file
            mediaPlayerTask = new MediaPlayerTask( mediaFilePath );

            LOGGER.debug( "About to play media file {}", mediaFilePath.getFileName().toString() );

            this.timerWrapper.scheduleMediaPlayerTask( mediaPlayerTask, startTimeMilliseconds );

            LOGGER.debug( "Media player task is scheduled to play media file {}", mediaFilePath.getFileName() );

        } catch( MediaFileNotSupportedException e )
        {
            LOGGER.error( PLAYER_START_ERROR_MSG, mediaFilePath.getFileName(), e );
            throw new MooPlayerException( PLAYER_START_ERROR_MSG, e );
        }
    }

    public void stopPlayingMedia() throws MooPlayerException
    {
        this.timerWrapper.stopCurrentMediaPlayerTask();
        this.mooPlayerAppStateWrapper.updateStatusToStopped();
    }

    public void pausePlayingMedia() throws MooPlayerException
    {
        this.timerWrapper.stopCurrentMediaPlayerTask();
        this.mooPlayerAppStateWrapper.updateStatusToPaused( 0 );
    }
}
