package com.android.ranit.contactsentrepot.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.ranit.contactsentrepot.repository.contract.IMainActivityContract;

public class MainActivityViewModel extends AndroidViewModel
        implements IMainActivityContract.ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    public void initiateImport() {

    }

    @Override
    public void initiateExport() {

    }

    @Override
    public void initiateSharing() {

    }
}
