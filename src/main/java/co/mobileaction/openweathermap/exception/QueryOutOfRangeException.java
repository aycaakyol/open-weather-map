package co.mobileaction.openweathermap.exception;

public class QueryOutOfRangeException extends RuntimeException
{
    public QueryOutOfRangeException(String message)
    {
        super(message);
    }
}
