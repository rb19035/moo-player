package org.tcgms.network.player.api.service;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.client.Mp3PlayerJob;
import org.tcgms.network.player.exception.MooPlayerException;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Service
public class MooPlayerMediaService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerMediaService.class );
    private static final String PLAYER_JOB_NAME = "MediaPlayerJob";
    private static final String PLAYER_GROUP_NAME = "MediaPlayerGroup";
    private static final String PLAYER_TRIGGER_NAME = "MediaPlayerTrigger";
    private static final String PLAYER_START_ERROR_MSG = "Could not process request to schedule job to start playing media {}";
    private static final String PLAYER_STOP_ERROR_MSG = "Could not process request to stop playing current media file";

    private final Scheduler quartzScheduler;

    @Autowired
    public MooPlayerMediaService( Scheduler quartzScheduler )
    {
        this.quartzScheduler = quartzScheduler;
    }

    public void playMediaFile( @NotNull Path mediaFilePath ) throws MooPlayerException
    {
        JobDetail mediaPlayerJob = null;
        Trigger mediaPlayerJobTrigger = null;

        try
        {
            LOGGER.debug( "Processing requested to play media file {}", mediaFilePath.getFileName() );

            mediaPlayerJob = JobBuilder.newJob( Mp3PlayerJob.class )
                    .withIdentity( PLAYER_JOB_NAME, PLAYER_GROUP_NAME )
                    .withDescription( PLAYER_JOB_NAME )
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

            this.quartzScheduler.scheduleJob( mediaPlayerJob, mediaPlayerJobTrigger );

            LOGGER.debug( "Moo Player job scheduled to play media file {}", mediaFilePath.getFileName() );

        } catch( SchedulerException e )
        {
            LOGGER.error( PLAYER_START_ERROR_MSG, mediaFilePath.getFileName(), e );
            throw new MooPlayerException( PLAYER_START_ERROR_MSG, e );
        }
    }

    public void stopPlayingMedia() throws MooPlayerException
    {
        JobKey jobKey;

        try
        {
            LOGGER.debug( "Processing request to stop playing current media file" );

            jobKey = this.findActiveMediaPlayerJob();
            if( jobKey != null )
            {
                this.quartzScheduler.interrupt( jobKey );

            } else
            {
                LOGGER.debug( "No active media player job found to stop" );
            }

        } catch( SchedulerException e )
        {
            LOGGER.error( PLAYER_STOP_ERROR_MSG );
            throw new MooPlayerException( PLAYER_STOP_ERROR_MSG, e );
        }
    }

    public void pausePlayingMedia()
    {

    }

    private JobKey findActiveMediaPlayerJob() throws SchedulerException
    {
        List<JobExecutionContext> jobExecutionContextList = this.quartzScheduler.getCurrentlyExecutingJobs();
        for( JobExecutionContext jobExecutionContext: jobExecutionContextList )
        {
            if( jobExecutionContext.getJobDetail().getDescription().equals( PLAYER_JOB_NAME ) )
            {
                return jobExecutionContext.getJobDetail().getKey();
            }
        }

        return null;
    }
}
