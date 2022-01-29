package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tcgms.network.player.api.service.FileStorageService;
import org.tcgms.network.player.api.service.MooPlayerMediaService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping( value = {"/api/media"} )
public class MooPlayerMediaAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerMediaAPI.class );

    private final FileStorageService fileStorageService;
    private final MooPlayerMediaService mooPlayerMediaService;


    public MooPlayerMediaAPI( @Autowired FileStorageService fileStorageService, @Autowired MooPlayerMediaService mooPlayerMediaService )
    {
        this.fileStorageService = fileStorageService;
        this.mooPlayerMediaService = mooPlayerMediaService;
    }

    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    @ResponseStatus( HttpStatus.OK )
    public void playMedia( @NotNull @RequestParam( "file" ) MultipartFile file )
    {
        LOGGER.debug( "Received request to play media file" );
        this.mooPlayerMediaService.playMediaFile( this.fileStorageService.storeFile( file ) );
    }

    @PutMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    @ResponseStatus( HttpStatus.OK )
    public void queueMedia( @NotNull @RequestParam( "file" ) MultipartFile file )
    {
        this.mooPlayerMediaService.playMediaFile( this.fileStorageService.storeFile( file ) );
    }

    @DeleteMapping
    @ResponseStatus( HttpStatus.OK )
    public void stopPlayingMedia()
    {
        LOGGER.debug( "Received request to stop playing media file" );

        this.mooPlayerMediaService.stopPlayingMedia();
    }

    @PatchMapping
    @ResponseStatus( HttpStatus.OK )
    public void pausePlayingMedia()
    {
        this.mooPlayerMediaService.pausePlayingMedia();
    }
}
