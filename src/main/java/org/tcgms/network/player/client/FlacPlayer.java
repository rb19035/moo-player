package org.tcgms.network.player.client;

import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import javax.validation.ValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

/* libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

public final class FlacPlayer implements PCMProcessor, MediaPlayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FlacPlayer.class );
    private final Path flacMediaFilePath;

    /**
     * Play a FLAC file application.
     * @author kc7bfi
     */
        private AudioFormat fmt;
        private DataLine.Info info;
        private SourceDataLine line;
        private Vector listeners = new Vector();
        private FileInputStream is = null;
        private FLACDecoder decoder = null;


        public FlacPlayer( final Path flacMediaFilePath )
        {
            this.flacMediaFilePath = flacMediaFilePath;

            if ( flacMediaFilePath == null || !flacMediaFilePath.toFile().exists() )
            {
                LOGGER.error( "Flac media file path is null" );
                throw new ValidationException();
            }

            File flacFile = flacMediaFilePath.toFile();
            if( !flacFile.exists() || !flacFile.isFile() || !flacFile.canRead() )
            {
                LOGGER.debug( "Flac media file is not accessible" );
                throw new ValidationException();
            }

            try
            {
                this.is = new FileInputStream( this.flacMediaFilePath.toFile() );
                this.decoder = new FLACDecoder(is);
                this.decoder.addPCMProcessor(this);

                // ---> decoder.decode();

            } catch( IOException e )
            {
                // Do nothing?
            } finally
            {
                this.stop();
            }
        }

        public void addListener (LineListener listener)
        {
            listeners.add(listener);
        }
        /**
         * Decode and play an input FLAC file.
         * @param inFileName    The input FLAC file name
         * @throws IOException  Thrown if error reading file
         * @throws LineUnavailableException Thrown if error playing file
         */
        public void decode(String inFileName) throws IOException, LineUnavailableException
        {
            try
            {
                this.is = new FileInputStream(inFileName);
                this.decoder = new FLACDecoder(is);
                this.decoder.addPCMProcessor(this);

                // ---> decoder.decode();

            } catch( IOException e )
            {
                // Do nothing?
            } finally
            {
                this.stop();
            }
        }

    @Override
    public void play()
    {
        try
        {
            LOGGER.debug( "Playing FLAC media file" );

            this.decoder.decode();

            LOGGER.debug( "Completed playing FLAC media file" );

        } catch( IOException e )
        {
            e.printStackTrace();
        } finally
        {
            this.stop();
        }
    }

    @Override
    public void stop()
        {
            if( this.line != null )
            {
                this.line.stop();
                this.line.close();
            }

            if( this.listeners != null )
            {
                this.listeners.clear();
            }

            try
            {
                if( this.is != null )
                {
                    this.is.close();
                    this.is = null;
                }
            } catch( IOException e )
            {
                // Eat it...
            }

            if( this.decoder != null )
            {
                this.decoder = null;
            }

        }

    @Override
    public void pause()
    {

    }

    public long getCurrentMediaPosition()
        {
            long currentPosition = -1;

            if( this.line != null )
            {
                this.line.getMicrosecondPosition();
            }

            return currentPosition;

        }

        /**
         * Process the StreamInfo block.
         * @param streamInfo the StreamInfo block
         * @see org.jflac.PCMProcessor#processStreamInfo(org.jflac.metadata.StreamInfo)
         */
        public void processStreamInfo(StreamInfo streamInfo) {
            try {
                fmt = streamInfo.getAudioFormat();
                info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
                line = (SourceDataLine) AudioSystem.getLine(info);

                //  Add the listeners to the line at this point, it's the only
                //  way to get the events triggered.
                int size = listeners.size();
                for (int index = 0; index < size; index++)
                    line.addLineListener((LineListener) listeners.get(index));

                line.open(fmt, AudioSystem.NOT_SPECIFIED);
                line.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        /**
         * Process the decoded PCM bytes.
         * @param pcm The decoded PCM data
         *
         */
        public void processPCM(ByteData pcm) {
            line.write(pcm.getData(), 0, pcm.getLen());
        }


        public void removeListener (LineListener listener)
        {
            listeners.removeElement(listener);
        }
}
