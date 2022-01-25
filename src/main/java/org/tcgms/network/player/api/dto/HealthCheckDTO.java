package org.tcgms.network.player.api.dto;

public class HealthCheckDTO
{
    private double systemLoad;
    private MooPlayerSystemStatus databaseStatus;


    public double getSystemLoad()
    {
        return systemLoad;
    }

    public void setSystemLoad(double systemLoad)
    {
        this.systemLoad = systemLoad;
    }

    public MooPlayerSystemStatus getDatabaseStatus()
    {
        return this.databaseStatus;
    }

    public void setDatabaseStatus(MooPlayerSystemStatus dbStatus)
    {
        this.databaseStatus = dbStatus;
    }
}
