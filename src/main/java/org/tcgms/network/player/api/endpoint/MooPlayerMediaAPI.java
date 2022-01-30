package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tcgms.network.player.MooPlayerAppState;
import org.tcgms.network.player.api.dto.MooPlayerMediaStatus;
import org.tcgms.network.player.api.service.FileStorageService;
import org.tcgms.network.player.api.service.MooPlayerMediaService;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.nio.file.Path;

@RestController
@RequestMapping( value = {"/api/media"} )
public class MooPlayerMediaAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MooPlayerMediaAPI.class );

    private final FileStorageService fileStorageService;
    private final MooPlayerMediaService mooPlayerMediaService;
    private MooPlayerAppState mooPlayerAppState;


    public MooPlayerMediaAPI( @Autowired FileStorageService fileStorageService,
                              @Autowired MooPlayerMediaService mooPlayerMediaService,
                              @Autowired MooPlayerAppState mooPlayerAppState )
    {
        this.fileStorageService = fileStorageService;
        this.mooPlayerMediaService = mooPlayerMediaService;
        this.mooPlayerAppState = mooPlayerAppState;
    }

    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.TEXT_HTML_VALUE } )
    @ResponseStatus( HttpStatus.OK )
    public void playMedia( @Nullable @RequestParam( "file" ) MultipartFile file )
    {
        Path mediaFilePath = null;

        LOGGER.debug( "Received request to play media file" );

        if( file == null )
        {
            if(  this.mooPlayerAppState.getCurrentMediaPlayStatus().equals( MooPlayerMediaStatus.PAUSED_MUSIC ) )
            {
                mediaFilePath = new File( this.mooPlayerAppState.getCurrentMediaURI() ).toPath();
            } else
            {
                throw new ValidationException();
            }

        } else
        {
            mediaFilePath = this.fileStorageService.storeFile( file );
        }

        this.mooPlayerMediaService.playMediaFile( mediaFilePath );
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
        LOGGER.debug( "Received request to pause playing media file" );

        this.mooPlayerMediaService.pausePlayingMedia();
    }
}
