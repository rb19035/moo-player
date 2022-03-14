package org.tcgms.network.player.exception;

public class MediaFileNotSupportedException extends RuntimeException
{
    public MediaFileNotSupportedException()
    {
        super();
    }

    public MediaFileNotSupportedException(String message)
    {
        super( message );
    }

    public MediaFileNotSupportedException(String message, Throwable cause)
    {
        super( message, cause );
    }

    public MediaFileNotSupportedException(Throwable cause)
    {
        super( cause );
    }

    protected MediaFileNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
