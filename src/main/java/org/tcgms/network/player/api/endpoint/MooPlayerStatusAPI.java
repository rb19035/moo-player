package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tcgms.network.player.api.dto.PlayerStatusDTO;
import org.tcgms.network.player.api.service.PlayerStatusService;

/**
 * Class encapsulates the REST API logic check the application's current state
 */
@RestController
@RequestMapping( value = {"/api/status"} )
public class MooPlayerStatusAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerStatusAPI.class );
    private final PlayerStatusService playerStatusService;

    /**
     * Class constructor
     *
     * @param playerStatusService - Status service impl injected by Spring Boot
     */
    @Autowired
    public MooPlayerStatusAPI( PlayerStatusService playerStatusService )
    {
        this.playerStatusService = playerStatusService;
    }

    /**
     * Method implements the REST API's (GET) player status endpoint.
     *
     * @return playerStatusDTO - DTO object containing information on the players current status
     */
    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<PlayerStatusDTO> playerStatus()
    {
        LOGGER.debug( "Received call to GET player status API." );

        // Call the service and return the current status
        PlayerStatusDTO playerStatusDTO = this.playerStatusService.getPlayerStatus();

        return ResponseEntity.ok( playerStatusDTO );
    }
}
