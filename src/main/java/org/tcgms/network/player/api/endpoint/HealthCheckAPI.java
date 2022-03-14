package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tcgms.network.player.api.bo.HealthCheckData;
import org.tcgms.network.player.api.service.HealthCheckService;

/**
 * Class encapsulates the REST API logic for a simple heath check for the application
 */
@RestController
@RequestMapping( value = {"/api/healthcheck"} )
public class HealthCheckAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( HealthCheckAPI.class );
    private final HealthCheckService healthCheckService;

    /**
     * Class Constructor
     * @param healthCheckService - Injected by Spring Boot
     */
    @Autowired
    public HealthCheckAPI(HealthCheckService healthCheckService )
    {
        this.healthCheckService = healthCheckService;
    }

    /**
     * Method implements the REST API's health check endpoint.
     *
     * @return healthCheckDTO - DTO object containing application health data
     */
    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<HealthCheckData> healthCheck()
    {
        LOGGER.debug( "Received call for MooPlayer health check." );

        HealthCheckData healthCheckData = this.healthCheckService.executeHealthCheck();

        return ResponseEntity.ok( healthCheckData );
    }
}
