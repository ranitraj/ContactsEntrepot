package com.android.ranit.contactsentrepot.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.ranit.contactsentrepot.R;
import com.android.ranit.contactsentrepot.repository.contract.IMainActivityContract;

/**
 * Created By: Ranit Raj Ganguly on 15/04/2021
 */
public class MainActivity extends AppCompatActivity implements IMainActivityContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void setupLottieAnimationView() {

    }

    @Override
    public void setupHandlerThreads() {

    }

    @Override
    public void onImportContactButtonClicked() {

    }

    @Override
    public void onExportContactButtonClicked() {

    }

    @Override
    public void onShareButtonClicked() {

    }

    @Override
    public void setupRecyclerView() {

    }

    @Override
    public void changeViewVisibility() {

    }
}