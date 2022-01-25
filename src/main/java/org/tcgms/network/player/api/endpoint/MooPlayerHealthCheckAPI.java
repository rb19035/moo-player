package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tcgms.network.player.MooPlayerException;
import org.tcgms.network.player.api.dto.HealthCheckDTO;
import org.tcgms.network.player.api.service.HealthCheckService;

@RestController
@RequestMapping( value = {"/api/healthcheck"} )
public class MooPlayerHealthCheckAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerHealthCheckAPI.class );
    private final HealthCheckService healthCheckService;

    @Autowired
    public MooPlayerHealthCheckAPI( HealthCheckService healthCheckService )
    {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping( produces = { "application/hal+json" } )
    public ResponseEntity<HealthCheckDTO> healthCheck()
    {
        HealthCheckDTO healthCheckDTO = null;

        try
        {
            LOGGER.debug( "Received call for MooPlayer health check." );

            healthCheckDTO = this.healthCheckService.executeHealthCheck();

            return ResponseEntity.ok( healthCheckDTO );

        } catch( MooPlayerException e )
        {
            LOGGER.error( "Could not execute health check API call.", e );
            return ResponseEntity.internalServerError().build();
        }
    }
}
