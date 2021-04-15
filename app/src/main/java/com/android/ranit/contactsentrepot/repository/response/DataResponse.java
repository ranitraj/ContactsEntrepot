package com.android.ranit.contactsentrepot.repository.response;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class DataResponse {
    @StateDefinition.State
    private int state;

    private T data;

    private ErrorData errorData;

    // Constructor
    public DataResponse(@StateDefinition.State int state, T data, ErrorData errorData) {
        this.state = state;
        this.data = data;
        this.errorData = errorData;
    }

    @StateDefinition.State
    public int getState() {
        return state;
    }

    public void setState(@StateDefinition.State int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData errorData) {
        this.errorData = errorData;
    }
}
