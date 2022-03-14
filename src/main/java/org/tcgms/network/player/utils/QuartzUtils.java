package org.tcgms.network.player.utils;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Component
public class QuartzUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger( QuartzUtils.class );

    public static final String PLAYER_JOB_NAME = "MediaPlayerJob";
    public static final String PLAYER_GROUP_NAME = "MediaPlayerGroup";
    public static final String PLAYER_TRIGGER_NAME = "MediaPlayerTrigger";
    public static final String MOO_PLAYER_TIME_SYNC_JOB_NAME = "MooPlayerTimeSyncJob";
    public static final String MOO_PLAYER_TIME_SYNC_JOB_GROUP = "MooPlayerTimeSyncGroup";
    public static final String MOO_PLAYER_TIME_SYNC_TRIGGER_NAME = "MooPlayerTimeSyncTrigger";

    private final Scheduler quartzScheduler;

    public QuartzUtils( @Autowired Scheduler quartzScheduler)
    {
        this.quartzScheduler = quartzScheduler;
    }

    public Job findActiveMediaPlayerJob( @NotNull @NotEmpty String jobName ) throws SchedulerException
    {
        LOGGER.debug( "Finding active job with name: {}", jobName );

        List<JobExecutionContext> jobExecutionContextList = this.quartzScheduler.getCurrentlyExecutingJobs();
        for( JobExecutionContext jobExecutionContext: jobExecutionContextList )
        {
            if( jobExecutionContext.getJobDetail().getDescription().equals( jobName ) )
            {
                LOGGER.debug( "Found active job with name: {}", jobName );

                return jobExecutionContext.getJobInstance();
            }
        }

        LOGGER.debug( "Was unable to find and active job: {}", jobName );

        return null;
    }

    public Job findActiveMediaPlayerJob() throws SchedulerException
    {
        return this.findActiveMediaPlayerJob( PLAYER_JOB_NAME );
    }

    public JobDetail findActiveJobDetail( @NotNull @NotEmpty String jobName ) throws SchedulerException
    {
        LOGGER.debug( "Finding active job detail with name: {}", jobName );

        List<JobExecutionContext> jobExecutionContextList = this.quartzScheduler.getCurrentlyExecutingJobs();
        for( JobExecutionContext jobExecutionContext: jobExecutionContextList )
        {
            if( jobExecutionContext.getJobDetail().getDescription().equals( jobName ) )
            {
                LOGGER.debug( "Found active job detail with name: {}", jobName );

                return jobExecutionContext.getJobDetail();
            }
        }

        LOGGER.debug( "Was unable to find and active detail job: {}", jobName );

        return null;
    }



    public Trigger createJobTrigger( @NotNull JobDetail JobDetail,
                                     @NotNull Date startDatetime,
                                     @NotNull @NotBlank String triggerName,
                                     @NotNull @NotBlank String groupName )
    {
        return TriggerBuilder.newTrigger()
                .withIdentity( triggerName, groupName )
                .startNow()
                .withSchedule( SimpleScheduleBuilder
                        .simpleSchedule()
                ).forJob( JobDetail )
                .startAt( startDatetime )
                .build();
    }

    private JobDetail createJob( @NotNull Class jobClass,
                                 @NotNull @NotEmpty String jobName,
                                 @NotNull @NotEmpty String jobGroupName )
    {
        return JobBuilder.newJob( jobClass)
                .withIdentity( jobName, jobGroupName )
                .withDescription( jobName )
                .build();
    }
}
