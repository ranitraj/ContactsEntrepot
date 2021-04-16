package com.android.ranit.contactsentrepot.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.android.ranit.contactsentrepot.R;
import com.android.ranit.contactsentrepot.databinding.ActivityMainBinding;
import com.android.ranit.contactsentrepot.repository.contract.IMainActivityContract;
import com.android.ranit.contactsentrepot.viewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * Created By: Ranit Raj Ganguly on 15/04/2021
 */
public class MainActivity extends AppCompatActivity implements IMainActivityContract.View {
    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;

    private Button importContactsButton;
    private Button exportExcelButton;
    private Button readExcelButton;
    private FloatingActionButton shareButton;
    private LottieAnimationView animationView;
    private RecyclerView contactsRecyclerView;
    private ConstraintLayout constraintLayout;

    private final String NO_DATA_ANIMATION = "no_data.json";
    private final String LOADING_ANIMATION = "loading.json";
    private final String ERROR_ANIMATION = "error.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        importContactsButton.setOnClickListener(view -> onImportContactButtonClicked());
        exportExcelButton.setOnClickListener(view -> onExportIntoExcelButtonClicked());
        readExcelButton.setOnClickListener(view -> onReadFromExcelButtonClicked());
        shareButton.setOnClickListener(view -> onShareButtonClicked());
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initializeViews() {
        importContactsButton = mBinding.importContactButton;
        exportExcelButton = mBinding.exportContactButton;
        readExcelButton = mBinding.readExcelDataButton;
        shareButton = mBinding.shareExcelFloatingButton;
        animationView = mBinding.lottieAnimationView;
        contactsRecyclerView = mBinding.displayContactsRecyclerView;
        constraintLayout = mBinding.constraintLayout;

        setupLottieAnimation(NO_DATA_ANIMATION);
    }

    @Override
    public void setupLottieAnimation(String animationName) {
        if (animationView.isAnimating()) {
            animationView.cancelAnimation();
        }
        animationView.setAnimation(animationName);
        animationView.playAnimation();
    }

    @Override
    public void setupHandlerThreads() {

    }

    @Override
    public void destroyHandlerThreads() {

    }

    @Override
    public void onImportContactButtonClicked() {
        displaySnackBar("Fetching Mobile contacts...");
        mViewModel.initiateImport();
    }

    @Override
    public void onExportIntoExcelButtonClicked() {
        displaySnackBar("Exporting data into Excel...");
        mViewModel.initiateExport();
    }

    @Override
    public void onReadFromExcelButtonClicked() {
        displaySnackBar("Reading data from excel...");
        mViewModel.initiateRead();
    }

    @Override
    public void onShareButtonClicked() {
        mViewModel.initiateSharing();
    }

    @Override
    public void setupRecyclerView() {
        changeAnimationViewVisibility();


    }

    @Override
    public void changeAnimationViewVisibility() {
        if (animationView.getVisibility() == View.VISIBLE) {
            animationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void displaySnackBar(String message) {
        Snackbar.make(constraintLayout, message, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }
}