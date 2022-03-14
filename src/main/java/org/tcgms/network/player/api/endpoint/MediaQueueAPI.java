package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tcgms.network.player.api.service.FileStorageService;
import org.tcgms.network.player.wrapper.MooPlayerAppStateWrapper;

import java.nio.file.Path;

@RestController
@RequestMapping( value = {"/api/media/queue"} )
public class MediaQueueAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MediaQueueAPI.class );
    private final FileStorageService fileStorageService;
    private MooPlayerAppStateWrapper mooPlayerAppStateWrapper;


    public MediaQueueAPI( @Autowired FileStorageService fileStorageService, @Autowired MooPlayerAppStateWrapper mooPlayerAppStateWrapper )
    {
        this.fileStorageService = fileStorageService;
        this.mooPlayerAppStateWrapper = mooPlayerAppStateWrapper;
    }

    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    @ResponseStatus( HttpStatus.OK )
    public void playMedia( @RequestParam( value = "file", required = true ) final MultipartFile file )
    {
        LOGGER.debug( "Received request to queue media file" );

        // A file was passed in with the request and the player needs to store it to disk and queue it to be played
        Path mediaFilePath = this.fileStorageService.storeFile( file );
        this.mooPlayerAppStateWrapper.queueMedia( mediaFilePath );

        LOGGER.debug( "Media file stored to disk and queued to play: {}", mediaFilePath.toAbsolutePath() );
        LOGGER.debug( "Player queue size: {}", this.mooPlayerAppStateWrapper.getPlayerStatus().mediaPathQueue().size() );
    }
}
