package org.tcgms.network.player.api.service;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tcgms.network.player.MooPlayerAppState;
import org.tcgms.network.player.api.dto.MooPlayerMediaStatus;
import org.tcgms.network.player.client.MediaPlayer;
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
    private MooPlayerAppState mooPlayerAppState;


    public MooPlayerMediaService( @Autowired Scheduler quartzScheduler, @Autowired MooPlayerAppState mooPlayerAppState )
    {
        this.quartzScheduler = quartzScheduler;
        this.mooPlayerAppState = mooPlayerAppState;
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

            mediaPlayerJob.getJobDataMap().put( Mp3PlayerJob.MEDIA_PATH_JOB_DETAIL_MAP_KEY, mediaFilePath.toFile() );

            if( this.mooPlayerAppState.getCurrentMediaPlayStatus().equals( MooPlayerMediaStatus.PAUSED_MUSIC ) )
            {
                mediaPlayerJob.getJobDataMap().put( Mp3PlayerJob.CURRENT_MEDIA_FILE_POSITION_JOB_DETAIL_MAP_KEY,
                        this.mooPlayerAppState.getMediaPlayerFileCurrentPosition() );
            }

            mediaPlayerJobTrigger = TriggerBuilder.newTrigger()
                    .withIdentity( PLAYER_TRIGGER_NAME, PLAYER_GROUP_NAME )
                    .startNow()
                    .withSchedule( SimpleScheduleBuilder
                            .simpleSchedule()
                    ).forJob( mediaPlayerJob )
                    .startAt( new Date() )
                    .build();

            this.quartzScheduler.scheduleJob( mediaPlayerJob, mediaPlayerJobTrigger );

            this.mooPlayerAppState.updateStatusToPlaying( mediaFilePath );

            LOGGER.debug( "Moo Player job scheduled to play media file {}", mediaFilePath.getFileName() );

        } catch( SchedulerException e )
        {
            LOGGER.error( PLAYER_START_ERROR_MSG, mediaFilePath.getFileName(), e );
            throw new MooPlayerException( PLAYER_START_ERROR_MSG, e );
        }
    }

    public void stopPlayingMedia() throws MooPlayerException
    {
        Job job;

        try
        {
            LOGGER.debug( "Processing request to stop playing current media file" );

            job = this.findActiveMediaPlayerJob();
            if( job != null )
            {
                ((MediaPlayer) job).stopPLayingMedia(); ;

            } else
            {
                LOGGER.debug( "No active media player job found to stop" );
            }

            this.mooPlayerAppState.updateStatusToStopped();

        } catch( SchedulerException e )
        {
            LOGGER.error( PLAYER_STOP_ERROR_MSG );
            throw new MooPlayerException( PLAYER_STOP_ERROR_MSG, e );
        }
    }

    public void pausePlayingMedia() throws MooPlayerException
    {
        Job job;
        int mediaFilePausedPosition;

        try
        {
            LOGGER.debug( "Processing request to pause playing current media file" );

            job = this.findActiveMediaPlayerJob();
            if( job != null )
            {
                mediaFilePausedPosition = ((MediaPlayer) job).pausePlayingMedia();

                this.mooPlayerAppState.updateStatusToPaused( mediaFilePausedPosition );

            } else
            {
                LOGGER.debug( "No active media player job found to pause" );
                this.mooPlayerAppState.updateStatusToStopped();
            }

        } catch( SchedulerException e )
        {
            LOGGER.error( PLAYER_STOP_ERROR_MSG );
            throw new MooPlayerException( PLAYER_STOP_ERROR_MSG, e );
        }
    }

    private Job findActiveMediaPlayerJob() throws SchedulerException
    {
        List<JobExecutionContext> jobExecutionContextList = this.quartzScheduler.getCurrentlyExecutingJobs();
        for( JobExecutionContext jobExecutionContext: jobExecutionContextList )
        {
            if( jobExecutionContext.getJobDetail().getDescription().equals( PLAYER_JOB_NAME ) )
            {
                return jobExecutionContext.getJobInstance();
            }
        }

        return null;
    }
}
