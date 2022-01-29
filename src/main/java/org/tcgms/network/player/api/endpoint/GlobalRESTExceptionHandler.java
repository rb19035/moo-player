package org.tcgms.network.player.api.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.tcgms.network.player.exception.FileStorageException;
import org.tcgms.network.player.exception.MooPlayerException;

import javax.validation.ValidationException;

@ControllerAdvice
public class GlobalRESTExceptionHandler extends ResponseEntityExceptionHandler
{
    private static Logger LOGGER = LoggerFactory.getLogger( GlobalRESTExceptionHandler.class );
    private static final String FILE_STORAGE_EXCEPTION = "Could not save or access media.";
    private static final String VALIDATION_EXCEPTION = "Missing a required param..";

    @ExceptionHandler( value = FileStorageException.class )
    public ResponseEntity<?> fileStorageException( FileStorageException fileStorageException)
    {
        LOGGER.error( "Error while doing File IO.", fileStorageException );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( FILE_STORAGE_EXCEPTION );
    }

    @ExceptionHandler( value = MooPlayerException.class )
    public ResponseEntity<?> mooPlayerException( MooPlayerException mooPlayerException )
    {
        LOGGER.error( "Error while doing File IO.", mooPlayerException );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
    }

    @ExceptionHandler( value = ValidationException.class )
    public ResponseEntity<?> validationException( ValidationException validationException )
    {
        LOGGER.error( "Error while doing File IO.", validationException );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( VALIDATION_EXCEPTION );
    }
}
