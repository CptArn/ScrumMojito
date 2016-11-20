package be.studyfindr.entities;

/**
 * The ErrorResponse is a placeholder for fault information
 * used by the exception handler.
 * @version 1.0
 */
public class ErrorResponse {
    private int errorCode;
    private String message;

    /**
    * Returns the error code.
    * @return error code
    */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     * @param errorCode code to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets the error message.
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     * @param message message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

