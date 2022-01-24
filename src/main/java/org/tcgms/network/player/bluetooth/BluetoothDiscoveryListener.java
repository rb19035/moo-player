package org.tcgms.network.player.bluetooth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class BluetoothDiscoveryListener implements DiscoveryListener
{
    private static Logger LOGGER = LoggerFactory.getLogger( BluetoothDiscoveryListener.class );

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass)
    {
        LOGGER.info( "Bluetooth device discovered!!!!" );
        LOGGER.info( remoteDevice.getBluetoothAddress() );
    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords)
    {
        LOGGER.info( "Bluetooth service discovered!!!!" );
        LOGGER.info( String.valueOf( serviceRecords.length ) );
    }

    @Override
    public void serviceSearchCompleted(int i, int i1)
    {
        LOGGER.info( "Bluetooth service discovery complete !!!!" );
    }

    @Override
    public void inquiryCompleted(int i)
    {
        LOGGER.info( "Bluetooth device discovery complete !!!!" );
    }
}
