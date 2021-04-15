package com.android.ranit.contactsentrepot.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.ranit.contactsentrepot.repository.Repository;
import com.android.ranit.contactsentrepot.repository.contract.IMainActivityContract;

public class MainActivityViewModel extends AndroidViewModel
        implements IMainActivityContract.ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private final Repository mRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance();
    }

    @Override
    public void initiateImport() {
        Log.e(TAG, "initiateImport: ");
        mRepository.readDataFromExcel();
    }

    @Override
    public void initiateExport() {
        Log.e(TAG, "initiateExport: ");
        mRepository.generateExcel();
    }

    @Override
    public void initiateSharing() {

    }
}
