package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.api.dto.HealthCheckDTO;
import org.tcgms.network.player.api.dto.MooPlayerSystemStatus;
import org.tcgms.network.player.exception.MooPlayerException;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Service
public class HealthCheckService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( HealthCheckService.class );

    public HealthCheckDTO executeHealthCheck() throws MooPlayerException
    {
        HealthCheckDTO healthCheckDTO = new HealthCheckDTO();

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        healthCheckDTO.setSystemLoad( operatingSystemMXBean.getSystemLoadAverage() );

        healthCheckDTO.setDatabaseStatus( MooPlayerSystemStatus.OK );

        return healthCheckDTO;
    }
}
