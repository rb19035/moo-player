package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tcgms.network.player.api.service.FileStorageService;
import org.tcgms.network.player.api.service.MediaService;
import org.tcgms.network.player.exception.MooPlayerNoMediaQueuedException;
import org.tcgms.network.player.wrapper.MooPlayerAppStateWrapper;

import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping( value = {"/api/media"} )
public class MediaAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MediaAPI.class );

    private final FileStorageService fileStorageService;
    private final MediaService mediaService;
    private final MooPlayerAppStateWrapper mooPlayerAppStateWrapper;

    public MediaAPI(@Autowired FileStorageService fileStorageService,
                    @Autowired MediaService mediaService,
                    @Autowired MooPlayerAppStateWrapper mooPlayerAppStateWrapper )
    {
        this.fileStorageService = fileStorageService;
        this.mediaService = mediaService;
        this.mooPlayerAppStateWrapper = mooPlayerAppStateWrapper;
    }

    @PostMapping( value = "/{startTimestampMilliseconds}", consumes = { MediaType.TEXT_HTML_VALUE } )
    @ResponseStatus( HttpStatus.OK )
    @Validated
    public void playMedia( @PathVariable( required = true ) @PositiveOrZero final long startTimestampMilliseconds )
    {

        LOGGER.debug( "Received request to play media file with start time of {} ms", startTimestampMilliseconds );

        if( this.mooPlayerAppStateWrapper.getPlayerStatus().mediaPathQueue().isEmpty() )
        {
            throw new MooPlayerNoMediaQueuedException();
        }

        this.mediaService.playMediaFile( startTimestampMilliseconds );
    }

    @DeleteMapping
    @ResponseStatus( HttpStatus.OK )
    public void stopPlayingMedia()
    {
        LOGGER.debug( "Received request to stop playing media file" );

        this.mediaService.stopPlayingMedia();
    }

    @PatchMapping
    @ResponseStatus( HttpStatus.OK )
    public void pausePlayingMedia()
    {
        LOGGER.debug( "Received request to pause playing media file" );

        this.mediaService.pausePlayingMedia();
    }
}
