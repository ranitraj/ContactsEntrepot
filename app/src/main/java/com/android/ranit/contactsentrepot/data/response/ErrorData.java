package com.android.ranit.contactsentrepot.data.response;

/**
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class ErrorData {
    @StateDefinition.ErrorState
    private int mErrorStatus;

    private String mErrorMessage;

    // Constructor
    public ErrorData(@StateDefinition.ErrorState int status, String message) {
        this.mErrorStatus = status;
        this.mErrorMessage = message;
    }

    @StateDefinition.ErrorState
    public int getErrorStatus() {
        return mErrorStatus;
    }

    public void setErrorStatus(@StateDefinition.ErrorState int mErrorStatus) {
        this.mErrorStatus = mErrorStatus;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }
}
