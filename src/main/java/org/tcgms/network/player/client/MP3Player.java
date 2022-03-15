package org.tcgms.network.player.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcgms.network.player.exception.MediaPlayerException;

import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MP3Player implements MediaPlayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MP3Player.class );
    private Player player;
    private Path mp3FilePath;

    private MP3Player() {}

    public MP3Player( final Path mp3MediaFilePath )
    {
        this.mp3FilePath = mp3MediaFilePath;

        if ( mp3MediaFilePath == null || !mp3MediaFilePath.toFile().exists() )
        {
            LOGGER.error( "Flac media file path is null" );
            throw new ValidationException();
        }

        File mp3File = mp3MediaFilePath.toFile();
        if( !mp3File.exists() || !mp3File.isFile() || !mp3File.canRead() )
        {
            LOGGER.debug( "MP3 media file is not accessible" );
            throw new ValidationException();
        }

        try
        {
            this.player = new Player( Files.newInputStream( this.mp3FilePath ) );

        } catch( JavaLayerException | IOException e )
        {
            LOGGER.error( "Could not initialize MP3 Player instance" );
            throw new MediaPlayerException( "Could not create MP3 Player instance" );
        }
    }

    @Override
    public void play() throws MediaPlayerException
    {
        try
        {
            LOGGER.debug( "Playing MP3 file {} at {}ms", this.mp3FilePath.toString(), System.currentTimeMillis() );

            this.player.play();

            LOGGER.debug( "Completed playing MP3 file {}", this.mp3FilePath.toString() );

        } catch( JavaLayerException e )
        {
            LOGGER.error( "Could not play MP3 media media file: {}", this.mp3FilePath.toAbsolutePath() );
            throw new MediaPlayerException();
        }
    }

    @Override
    public void stop()
    {
        this.player.close();
    }

    @Override
    public void pause()
    {
        this.player.close();
    }
}
