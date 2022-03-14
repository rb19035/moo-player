package org.tcgms.network.player.exception;

public class MooPlayerNoMediaQueuedException extends RuntimeException
{
    public MooPlayerNoMediaQueuedException()
    {
        super();
    }

    public MooPlayerNoMediaQueuedException(String message)
    {
        super( message );
    }

    public MooPlayerNoMediaQueuedException(String message, Throwable cause)
    {
        super( message, cause );
    }

    public MooPlayerNoMediaQueuedException(Throwable cause)
    {
        super( cause );
    }

    protected MooPlayerNoMediaQueuedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
