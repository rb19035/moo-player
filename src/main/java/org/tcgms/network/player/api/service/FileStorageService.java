package org.tcgms.network.player.api.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tcgms.network.player.FileStorageProperties;
import org.tcgms.network.player.client.MediaPlayerJob;
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
        String fileExtension = null;
        String normalizedFileName = null;

        LOGGER.debug( "Call to save file to disk with file name: {}", multipartFile.getOriginalFilename() );

        // Check to make sure we received a media file to work with
        if( multipartFile.isEmpty()
                || multipartFile.getOriginalFilename() == null
                || multipartFile.getOriginalFilename().isEmpty() )
        {
            throw new FileStorageException( "Cannot store empty file." );
        }

        // Get the file extension to make sure it is something the player supports
        fileExtension = FilenameUtils.getExtension( multipartFile.getOriginalFilename() );

        // Check the file extension to make sure it is something the player supports
        if( !ArrayUtils.contains( MediaPlayerJob.SUPPORTED_FILE_EXTENSIONS, fileExtension ) )
        {
            throw new FileStorageException( "File type not supported." );
        }

        // Normalize the file name ... which will be compared to the root dir where downloaded files are stored.
        destinationFile = this.storageRootDirectory
                .resolve
                        (
                            Paths.get( multipartFile.getOriginalFilename() )
                        )
                .normalize().toAbsolutePath();

        // Check to make sure file path is in our root directory ... If not then someone passed a pad file name
        if( !destinationFile.getParent().equals( this.storageRootDirectory.toAbsolutePath() ) )
        {
            throw new FileStorageException( "Bad filename provided." );
        }

        // File should be safe to use if the above checks were passed
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
