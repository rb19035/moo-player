package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.MooPlayerAppState;
import org.tcgms.network.player.api.dto.PlayerStatusDTO;
import org.tcgms.network.player.exception.MooPlayerException;

/**
 * Class encapsulates logic retrieve the media player's status
 */
@Service
public class PlayerStatusService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( PlayerStatusService.class );
    private final MooPlayerAppState mooPlayerAppState;

    /**
     * Class contractor
     *
     * @param mooPlayerAppState - Injected by Spring Boot
     */
    @Autowired
    public PlayerStatusService( MooPlayerAppState mooPlayerAppState )
    {
        this.mooPlayerAppState = mooPlayerAppState;
    }

    public PlayerStatusDTO getPlayerStatus() throws MooPlayerException
    {
        // The @MooPlayerAppState class contains information about the media player's current state
        return this.mooPlayerAppState.getPlayerStatusDTO();
    }
}
