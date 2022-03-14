package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.api.bo.HealthCheckData;
import org.tcgms.network.player.api.bo.MooPlayerSystemStatus;
import org.tcgms.network.player.exception.MooPlayerException;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Class encapsulates logic for a simple heath check for the application
 */
@Service
public class HealthCheckService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( HealthCheckService.class );

    /**
     * Method executes a simple application health check and returns a DTO object
     *
     * @return HealthCheckDTO
     * @throws MooPlayerException
     */
    public HealthCheckData executeHealthCheck() throws MooPlayerException
    {
        // Get a reference to the underlying OS
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        HealthCheckData healthCheckData = new HealthCheckData( operatingSystemMXBean.getSystemLoadAverage(), MooPlayerSystemStatus.OK );

        return healthCheckData;
    }
}
