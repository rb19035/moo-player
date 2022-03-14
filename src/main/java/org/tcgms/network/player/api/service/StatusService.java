package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.api.bo.PlayerStatus;
import org.tcgms.network.player.exception.MooPlayerException;
import org.tcgms.network.player.wrapper.MooPlayerAppStateWrapper;

/**
 * Class encapsulates logic retrieve the media player's status
 */
@Service
public class StatusService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( StatusService.class );
    private final MooPlayerAppStateWrapper mooPlayerAppStateWrapper;

    /**
     * Class contractor
     *
     * @param mooPlayerAppStateWrapper - Injected by Spring Boot
     */
    @Autowired
    public StatusService(MooPlayerAppStateWrapper mooPlayerAppStateWrapper)
    {
        this.mooPlayerAppStateWrapper = mooPlayerAppStateWrapper;
    }

    public PlayerStatus getPlayerStatus() throws MooPlayerException
    {
        // The @MooPlayerAppState class contains information about the media player's current state
        return this.mooPlayerAppStateWrapper.getPlayerStatus();
    }
}
