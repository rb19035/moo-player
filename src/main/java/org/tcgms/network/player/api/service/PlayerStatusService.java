package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.MooPlayerAppState;
import org.tcgms.network.player.MooPlayerException;
import org.tcgms.network.player.api.dto.PlayerStatusDTO;

@Service
public class PlayerStatusService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( PlayerStatusService.class );
    private final MooPlayerAppState mooPlayerAppState;

    @Autowired
    public PlayerStatusService( MooPlayerAppState mooPlayerAppState )
    {
        this.mooPlayerAppState = mooPlayerAppState;
    }

    public PlayerStatusDTO getPlayerStatus() throws MooPlayerException
    {
        PlayerStatusDTO playerStatusDTO = new PlayerStatusDTO();
        return this.mooPlayerAppState.getPlayerStatusDTO();
    }
}
