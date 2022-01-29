package org.tcgms.network.player.api.service;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.client.Mp3PlayerJob;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.Date;

@Service
public class MooPlayerMediaService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerMediaService.class );
    private static final String PLAYER_JOB_NAME = "MediaPlayerJob";
    private static final String PLAYER_GROUP_NAME = "MediaPlayerGroup";
    private static final String PLAYER_TRIGGER_NAME = "MediaPlayerTrigger";

    private final FileStorageService fileStorageService;
    private final Scheduler quartzScheduler;

    public MooPlayerMediaService( @Autowired FileStorageService fileStorageService, @Autowired Scheduler quartzScheduler )
    {
        this.fileStorageService = fileStorageService;
        this.quartzScheduler = quartzScheduler;
    }

    public void playMediaFile( @NotNull Path mediaFilePath )
    {
        JobDetail mediaPlayerJob = null;
        Trigger mediaPlayerJobTrigger = null;

        try
        {
            LOGGER.debug( "Moo Player requested to play media file {}", mediaFilePath.getFileName() );

            mediaPlayerJob = JobBuilder.newJob( Mp3PlayerJob.class )
                    .withIdentity( PLAYER_JOB_NAME, PLAYER_GROUP_NAME )
                    .build();

            mediaPlayerJob.getJobDataMap().put( "mediaLocation", mediaFilePath.toFile() );

            mediaPlayerJobTrigger = TriggerBuilder.newTrigger()
                    .withIdentity( PLAYER_TRIGGER_NAME, PLAYER_GROUP_NAME )
                    .startNow()
                    .withSchedule( SimpleScheduleBuilder
                            .simpleSchedule()
                    ).forJob( mediaPlayerJob )
                    .startAt( new Date() )
                    .build();

            //mediaPlayerJobScheduler.start();
            Date jobscheduledDate = this.quartzScheduler.scheduleJob( mediaPlayerJob, mediaPlayerJobTrigger );

            LOGGER.debug( "Moo Player job scheduled to play media file {}", mediaFilePath.getFileName() );

            LOGGER.debug( "Moo Player job scheduled to play media file at {}", jobscheduledDate.toString() );

        } catch( Throwable e )
        {
            LOGGER.error( "Could not start media player.", e );
        }
    }

    public void stopPlayingMedia()
    {
        //this.quartzScheduler.interrupt(  )
    }

    public void pausePlayingMedia()
    {

    }
}
