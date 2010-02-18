package com.manheim.vim.services.exception;

public class ConfigurationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and 
     * may subsequently be initialized by a call to Throwable.initCause(java.lang.Throwable).
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public ConfigurationException(String message)
    {
        super(message);
    }
    
    /**
     * Constructs a new exception with the specified detail message and cause. 
     * Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param throwable the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown) 
     */
    public ConfigurationException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class 
     * and detail message of cause). This constructor is useful for exceptions
     * that are little more than wrappers for other throwables 
     * (for example, PrivilegedActionException). 
     * @param throwable the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown)
     */
    public ConfigurationException(Throwable throwable)
    {
        super(throwable);
    }
}
