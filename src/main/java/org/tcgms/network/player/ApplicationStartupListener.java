package org.tcgms.network.player;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tcgms.network.player.job.BluetoothListenerJob;

@Component
public class ApplicationStartupListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ApplicationStartupListener.class );

    private static final String MOO_PLAYER_BLUETOOTH_JOB_NAME = "MooPlayerBluetoothListenerJob";
    private static final String MOO_PLAYER_BLUETOOTH_JOB_GROUP = "MooPlayerBluetoothListenerGroup";
    private static final String MOO_PLAYER_BLUETOOTH_TRIGGER_NAME = "MooPlayerBluetoothListenerTrigger";



    private final Scheduler quartzScheduler;
    private RestTemplate restTemplate;


    public ApplicationStartupListener(@Autowired Scheduler quartzScheduler, @Autowired RestTemplate restTemplate)
    {
        this.quartzScheduler = quartzScheduler;
        this.restTemplate = restTemplate;
    }

    @EventListener
    public void onApplicationEvent( final ContextRefreshedEvent event )
    {
        LOGGER.debug( "Moo Player application started" );

        //this.startBluetoothListener( event );

    }

    private void startBluetoothListener( final ContextRefreshedEvent event )
    {
        JobDetail bluetoothListenerJob = null;
        Trigger bluetoothListenerJobTrigger = null;

        try
        {
            LOGGER.debug( "Starting Bluetooth listener" );

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
                            .withIntervalInSeconds( 120 )
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