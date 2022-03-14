package org.tcgms.network.player.wrapper;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.tcgms.network.player.client.MediaPlayerTask;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.Timer;

@Component
public class TimerWrapper
{
    private static final Timer TIMER = new Timer( false );
    private MediaPlayerTask currentMediaPlayerTask;

    @Validated
    public void scheduleMediaPlayerTask( @NotNull MediaPlayerTask mediaPlayerTask, @PositiveOrZero long startTimeMilliseconds )
    {
        this.currentMediaPlayerTask = mediaPlayerTask;
        TIMER.schedule(  mediaPlayerTask, new Date( startTimeMilliseconds ) );
    }

    public void stopCurrentMediaPlayerTask()
    {
        this.currentMediaPlayerTask.cancel();
    }
}
