package org.tcgms.network.player.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

public class BluetoothListenerJob implements Job
{
    private static final Logger LOGGER = LoggerFactory.getLogger( BluetoothListenerJob.class );

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        LocalDevice localDevice = null;
        DiscoveryAgent discoveryAgent = null;
        RemoteDevice[] remoteDevices = null;

        try
        {
            localDevice = LocalDevice.getLocalDevice();

            LOGGER.debug( "Initializing Bluetooth listener" );

            LOGGER.debug( "Bluetooth local device power is: {}", String.valueOf( localDevice.isPowerOn() ) );
            LOGGER.debug( "Bluetooth local device name is: {}", String.valueOf( localDevice.getFriendlyName() ) );
            LOGGER.debug( "Bluetooth local device address is: {}", String.valueOf( localDevice.getBluetoothAddress() ) );
            LOGGER.debug( "Bluetooth local device discoverable is: {}", String.valueOf( localDevice.getDiscoverable() ) );

            discoveryAgent = localDevice.getDiscoveryAgent();

            boolean flag = discoveryAgent.startInquiry( DiscoveryAgent.GIAC, new BluetoothDiscoveryListener() );

            LOGGER.debug( "Bluetooth local device discovery agent started: {}", String.valueOf( flag ) );

//            for( RemoteDevice remoteDevice: discoveryAgent.retrieveDevices( DiscoveryAgent.GIAC ) )
//            {
//                LOGGER.info( remoteDevice.getBluetoothAddress() );
//            }

        } catch( BluetoothStateException e )
        {
            e.printStackTrace();
        }
    }
}