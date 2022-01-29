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

@RestController
@RequestMapping( value = {"/api/status"} )
public class MooPlayerStatusAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerStatusAPI.class );
    private final PlayerStatusService playerStatusService;

    @Autowired
    public MooPlayerStatusAPI( PlayerStatusService playerStatusService )
    {
        this.playerStatusService = playerStatusService;
    }

    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<PlayerStatusDTO> healthCheck()
    {
        LOGGER.debug( "Received call to GET player status API." );

        // Call the service and return the current status
        PlayerStatusDTO playerStatusDTO = this.playerStatusService.getPlayerStatus();

        return ResponseEntity.ok( playerStatusDTO );
    }
}
