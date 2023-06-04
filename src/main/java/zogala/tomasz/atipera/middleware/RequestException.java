package zogala.tomasz.atipera.middleware;

import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException {

    public RequestException(final HttpStatus requestStatusCode, final String requestMessage) {
        this(requestStatusCode, requestMessage, null);
    }

    public RequestException(final HttpStatus requestStatusCode, final String requestMessage, final Throwable cause) {
        super(cause);
        this.requestStatus = requestStatusCode;
        this.requestMessage = requestMessage;
    }

    private HttpStatus requestStatus;
    private String requestMessage;

    public HttpStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(HttpStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}