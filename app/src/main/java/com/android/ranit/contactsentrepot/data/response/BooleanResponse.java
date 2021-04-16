package com.android.ranit.contactsentrepot.data.response;

/**
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class BooleanResponse {
    @StateDefinition.State
    private int state;

    private boolean isSuccess;

    private ErrorData errorData;

    // Constructor
    public BooleanResponse(@StateDefinition.State int state, boolean isSuccess, ErrorData errorData) {
        this.state = state;
        this.isSuccess = isSuccess;
        this.errorData = errorData;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData errorData) {
        this.errorData = errorData;
    }
}
