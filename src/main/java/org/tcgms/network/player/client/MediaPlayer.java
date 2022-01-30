package org.tcgms.network.player.client;

import org.tcgms.network.player.exception.MooPlayerException;

public interface MediaPlayer
{
    public void stopPLayingMedia() throws MooPlayerException;
    public int pausePlayingMedia() throws MooPlayerException;
    public void playMedia() throws MooPlayerException;
}
