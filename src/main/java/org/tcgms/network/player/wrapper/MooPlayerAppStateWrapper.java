package org.tcgms.network.player.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.tcgms.network.player.api.bo.MooPlayerConfiguration;
import org.tcgms.network.player.api.bo.MooPlayerMediaStatus;
import org.tcgms.network.player.api.bo.PlayerStatus;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

@Component
public class MooPlayerAppStateWrapper
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerAppStateWrapper.class );
    private PlayerStatus playerStatus;

    @PostConstruct
    public void initMooPlayerAppState()
    {
        this.playerStatus = new PlayerStatus( "N/A", new File("/tmp").toPath(),
                new LinkedList<Path>(), MooPlayerMediaStatus.IDLE, MooPlayerConfiguration.ISOLATED,
                "N/A");
    }

    public void updateStatusToStopped()
    {
        this.playerStatus = new PlayerStatus( "N/A", new File("/tmp").toPath(),
                new LinkedList<Path>() , MooPlayerMediaStatus.IDLE, MooPlayerConfiguration.ISOLATED,
                "N/A");
    }

    @Validated
    public Path updateStatusToPlaying()
    {
        Path mediaFilePathToBePlayed = null;

        LOGGER.debug( "Updating state to playing" );
        LOGGER.debug( "Current state is {}", this.playerStatus.currentMediaPlayStatus() );

        if( this.playerStatus.currentMediaPlayStatus().equals( MooPlayerMediaStatus.PAUSED_MUSIC ) )
        {
            mediaFilePathToBePlayed = this.playerStatus.currentMediaFilePath();

        } else if( this.playerStatus.mediaPathQueue() != null && !this.playerStatus.mediaPathQueue().isEmpty() )
        {
            mediaFilePathToBePlayed = this.playerStatus.mediaPathQueue().remove( 0 );

            this.playerStatus = new PlayerStatus(   mediaFilePathToBePlayed.getFileName().toString(), mediaFilePathToBePlayed,
                                                    this.playerStatus.mediaPathQueue(), MooPlayerMediaStatus.PLAYING_MUSIC,
                                                    MooPlayerConfiguration.ISOLATED, "N/A" );
        }

        return mediaFilePathToBePlayed;
    }

    public void updateStatusToPaused( final long mediaPlayerFileCurrentPosition )
    {
        this.playerStatus = new PlayerStatus(   this.playerStatus.currentMediaTitle(), this.playerStatus.currentMediaFilePath(),
                                                this.playerStatus.mediaPathQueue(), MooPlayerMediaStatus.PAUSED_MUSIC,
                                                MooPlayerConfiguration.ISOLATED, "N/A" );
    }

    @Validated
    public void queueMedia( @NotBlank final Path mediaFilePath )
    {
        List<Path> pathQueue= this.playerStatus.mediaPathQueue();
        pathQueue.add( mediaFilePath );

        this.playerStatus = new PlayerStatus(   this.playerStatus.currentMediaTitle(), this.playerStatus.currentMediaFilePath(),
                                                pathQueue, this.playerStatus.currentMediaPlayStatus(),
                                                this.playerStatus.playConfiguration(), "N/A" );
    }


    public PlayerStatus getPlayerStatus()
    {
        return playerStatus;
    }
}
