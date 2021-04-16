package com.android.ranit.contactsentrepot.data.response;

import java.util.List;

/**
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class DataResponse<T> {
    @StateDefinition.State
    private int state;

    private List<T> data;

    private ErrorData errorData;

    // Constructor
    public DataResponse(@StateDefinition.State int state, List<T> data, ErrorData errorData) {
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData errorData) {
        this.errorData = errorData;
    }
}
