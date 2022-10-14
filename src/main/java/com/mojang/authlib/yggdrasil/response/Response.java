package com.mojang.authlib.yggdrasil.response;

public class Response {

    private String error;

    private String errorMessage;

    private String cause;

    public String getError() {
        return this.error;
    }

    protected void setError(String error) {
        this.error = error;
    }

    public String getCause() {
        return this.cause;
    }

    protected void setCause(String cause) {
        this.cause = cause;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
