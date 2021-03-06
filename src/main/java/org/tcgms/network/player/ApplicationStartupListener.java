package org.tcgms.network.player;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tcgms.network.player.bluetooth.BluetoothListenerJob;

@Component
public class ApplicationStartupListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ApplicationStartupListener.class );
    private static final String MOO_PLAYER_BLUETOOTH_JOB_NAME = "MooPlayerBluetoothListenerJob";
    private static final String MOO_PLAYER_BLUETOOTH_JOB_GROUP = "MooPlayerBluetoothListenerGroup";
    private static final String MOO_PLAYER_BLUETOOTH_TRIGGER_NAME = "MooPlayerBluetoothListenerTrigger";

    private final Scheduler quartzScheduler;

    @Autowired
    public ApplicationStartupListener(Scheduler quartzScheduler)
    {
        this.quartzScheduler = quartzScheduler;
    }

    @EventListener
    public void onApplicationEvent( ContextRefreshedEvent event )
    {
        JobDetail bluetoothListenerJob = null;
        Trigger bluetoothListenerJobTrigger = null;

        try
        {
            LOGGER.debug( "Moo Player application started." );

            // Create the Bluetooth listener job to scan for controller devices
            bluetoothListenerJob = JobBuilder.newJob( BluetoothListenerJob.class )
                    .withIdentity( MOO_PLAYER_BLUETOOTH_JOB_NAME, MOO_PLAYER_BLUETOOTH_JOB_GROUP )
                    .build();

            // Create the Bluetooth listener trigger to scan for controller devices
            bluetoothListenerJobTrigger = TriggerBuilder.newTrigger()
                    .withIdentity( MOO_PLAYER_BLUETOOTH_TRIGGER_NAME, MOO_PLAYER_BLUETOOTH_JOB_GROUP )
                    .startNow()
                    .withSchedule( SimpleScheduleBuilder
                            .simpleSchedule()
                            .withIntervalInSeconds( 60 )
                            .repeatForever()
                    )
                    .build();

            this.quartzScheduler.scheduleJob( bluetoothListenerJob, bluetoothListenerJobTrigger  );



        } catch( SchedulerException e )
        {
            LOGGER.error( "Could not start Bluetooth listener.", e );
        }
    }
}