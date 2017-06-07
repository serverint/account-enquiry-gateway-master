package com.wasp.gateway.enquiry.account.exceptions

/**
 * Created by aodunlami on 5/19/17.
 */
class WaspApiValidationException extends Exception{

    private String errorCode
    private String errorDescription

    WaspApiValidationException() {
    }

    WaspApiValidationException(String errorCode, String errorDescription) {
        super()
        this.errorCode = errorCode
        this.errorDescription = errorDescription
    }

    String getErrorCode() {
        return errorCode
    }

    WaspApiValidationException setErrorCode(String errorCode) {
        this.errorCode = errorCode
        return this
    }

    String getErrorDescription() {
        return errorDescription
    }

    void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription
    }
}
