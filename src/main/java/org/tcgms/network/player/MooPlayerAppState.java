package org.tcgms.network.player;

import org.springframework.stereotype.Component;
import org.tcgms.network.player.api.dto.MooPlayerConfiguration;
import org.tcgms.network.player.api.dto.MooPlayerMediaStatus;
import org.tcgms.network.player.api.dto.PlayerStatusDTO;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.List;

@Component
public class MooPlayerAppState
{
    private PlayerStatusDTO playerStatusDTO;
    private long mediaPlayerFileCurrentPosition;

    @PostConstruct
    public void initMooPlayerAppState()
    {
        this.playerStatusDTO = new PlayerStatusDTO();

        this.setPlayConfiguration( MooPlayerConfiguration.ISOLATED );

        this.updateStatusToStopped();
    }

    public void updateStatusToStopped()
    {
        this.setCurrentMediaTitle( "N/A" );
        this.setCurrentMediaURI( "N/A" );
        this.setMediaUIRQueue( null );
        this.setCurrentMediaPlayStatus( MooPlayerMediaStatus.IDLE );
        this.mediaPlayerFileCurrentPosition = -1;
    }

    public void updateStatusToPlaying( Path mediaFilePath )
    {
        this.setCurrentMediaTitle( mediaFilePath.getFileName().toString() );
        this.setCurrentMediaURI( mediaFilePath.toAbsolutePath().toString() );
        this.setCurrentMediaPlayStatus( MooPlayerMediaStatus.PLAYING_MUSIC );
        this.mediaPlayerFileCurrentPosition = -1;
    }

    public void updateStatusToPaused( long mediaPlayerFileCurrentPosition )
    {
        this.setCurrentMediaPlayStatus( MooPlayerMediaStatus.PAUSED_MUSIC );
        this.mediaPlayerFileCurrentPosition = mediaPlayerFileCurrentPosition;
    }

    public String getCurrentMediaTitle()
    {
        return playerStatusDTO.getCurrentMediaTitle();
    }

    public void setCurrentMediaTitle(String currentMediaTitle)
    {
        this.playerStatusDTO.setCurrentMediaTitle( currentMediaTitle );
    }

    public String getCurrentMediaURI()
    {
        return playerStatusDTO.getCurrentMediaURI();
    }

    public void setCurrentMediaURI(String currentMediaURI)
    {
        this.playerStatusDTO.setCurrentMediaURI( currentMediaURI );
    }

    public List<String> getMediaUIRQueue()
    {
        return this.playerStatusDTO.getMediaUIRQueue();
    }

    public void setMediaUIRQueue(List<String> mediaUIRQueue)
    {
        this.playerStatusDTO.setMediaUIRQueue( mediaUIRQueue );
    }

    public MooPlayerMediaStatus getCurrentMediaPlayStatus()
    {
        return this.playerStatusDTO.getCurrentMediaPlayStatus();
    }

    public void setCurrentMediaPlayStatus( MooPlayerMediaStatus mooPlayerMediaStatus )
    {
        this.playerStatusDTO.setCurrentMediaPlayStatus( mooPlayerMediaStatus );
    }

    public MooPlayerConfiguration getPlayConfiguration()
    {
        return this.playerStatusDTO.getPlayConfiguration();
    }

    public void setPlayConfiguration( MooPlayerConfiguration playConfiguration )
    {
        this.playerStatusDTO.setPlayConfiguration( playConfiguration );
    }

    public PlayerStatusDTO getPlayerStatusDTO()
    {
        return playerStatusDTO;
    }

    public void setPlayerStatusDTO(PlayerStatusDTO playerStatusDTO)
    {
        this.playerStatusDTO = playerStatusDTO;
    }

    public long getMediaPlayerFileCurrentPosition()
    {
        return mediaPlayerFileCurrentPosition;
    }

    public void setMediaPlayerFileCurrentPosition(long mediaPlayerFileCurrentPosition)
    {
        this.mediaPlayerFileCurrentPosition = mediaPlayerFileCurrentPosition;
    }
}
