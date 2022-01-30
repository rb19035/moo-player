package org.tcgms.network.player.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tcgms.network.player.FileStorageProperties;
import org.tcgms.network.player.exception.FileStorageException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FileStorageService.class );
    private final Path storageRootDirectory;

    @Autowired
    public FileStorageService( FileStorageProperties fileStorageProperties )
    {
        this.storageRootDirectory = Path.of( fileStorageProperties.getDir() );
    }

    public Path storeFile( @NotNull MultipartFile multipartFile ) throws FileStorageException
    {
        Path destinationFile = null;

        LOGGER.debug( "Call to save file to disk with file name: {}", multipartFile.getOriginalFilename() );

        if( multipartFile.isEmpty()
                || multipartFile.getOriginalFilename() == null
                || multipartFile.getOriginalFilename().isEmpty() )
        {
            throw new FileStorageException( "Cannot store empty file." );
        }

        destinationFile = this.storageRootDirectory
                .resolve
                        (
                            Paths.get( multipartFile.getOriginalFilename() )
                        )
                .normalize().toAbsolutePath();

        if( !destinationFile.getParent().equals( this.storageRootDirectory.toAbsolutePath() ) )
        {
            throw new FileStorageException( "Bad filename provided." );
        }

        try ( InputStream inputStream = multipartFile.getInputStream() )
        {
            Files.copy( inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING );

            return destinationFile;

        } catch( IOException e )
        {
            throw new FileStorageException( e );
        }
    }
}
