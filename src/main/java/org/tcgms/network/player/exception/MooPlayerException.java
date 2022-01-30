package org.tcgms.network.player.exception;

public class MooPlayerException extends RuntimeException
{
    public MooPlayerException()
    {
        super();
    }

    public MooPlayerException(String message)
    {
        super( message );
    }

    public MooPlayerException(String message, Throwable cause)
    {
        super( message, cause );
    }

    public MooPlayerException(Throwable cause)
    {
        super( cause );
    }

    protected MooPlayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
