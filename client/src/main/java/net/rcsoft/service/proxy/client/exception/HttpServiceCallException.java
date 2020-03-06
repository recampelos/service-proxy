package net.rcsoft.service.proxy.client.exception;

/**
 * Exception for HTTP service call error.
 * 
 * @author recampelos
 */
public class HttpServiceCallException extends Exception {

    public HttpServiceCallException(String message) {
        super(message);
    }

    public HttpServiceCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
