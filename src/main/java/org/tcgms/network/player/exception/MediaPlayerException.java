package org.tcgms.network.player.exception;

public class MediaPlayerException extends RuntimeException
{
    public MediaPlayerException()
    {
        super();
    }

    public MediaPlayerException(String message)
    {
        super( message );
    }

    public MediaPlayerException(String message, Throwable cause)
    {
        super( message, cause );
    }

    public MediaPlayerException(Throwable cause)
    {
        super( cause );
    }

    protected MediaPlayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
