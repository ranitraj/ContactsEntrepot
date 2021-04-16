package com.android.ranit.contactsentrepot.data.response;

import androidx.annotation.IntDef;

/**
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class StateDefinition {
    @IntDef({State.LOADING, State.SUCCESS, State.ERROR})
    public @interface State {
        int LOADING = 0;
        int SUCCESS = 1;
        int ERROR = 2;
    }

    @IntDef({ErrorState.INTERNAL_ERROR, ErrorState.FILE_NOT_FOUND_ERROR,
            ErrorState.PERMISSION_ERROR, ErrorState.EXCEL_GENERATION_ERROR})
    public @interface ErrorState {
        int INTERNAL_ERROR = 100;
        int FILE_NOT_FOUND_ERROR = 404;
        int PERMISSION_ERROR = 500;
        int EXCEL_GENERATION_ERROR = 300;
    }
}
