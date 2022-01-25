package org.tcgms.network.player;

import org.springframework.stereotype.Component;
import org.tcgms.network.player.api.dto.MooPlayerConfiguration;
import org.tcgms.network.player.api.dto.MooPlayerMediaStatus;
import org.tcgms.network.player.api.dto.PlayerStatusDTO;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class MooPlayerAppState
{
    private PlayerStatusDTO playerStatusDTO;

    @PostConstruct
    public void initMooPlayerAppState()
    {
        this.playerStatusDTO = new PlayerStatusDTO();
        
        this.setCurrentMediaTitle( "N/A" );
        this.setCurrentMediaURI( "N/A" );
        this.setMediaUIRQueue( null );
        this.setCurrentMediaPlayStatus( MooPlayerMediaStatus.IDLE );
        this.setPlayConfiguration( MooPlayerConfiguration.ISOLATED );
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
}
