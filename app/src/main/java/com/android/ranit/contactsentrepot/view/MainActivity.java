package com.android.ranit.contactsentrepot.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.android.ranit.contactsentrepot.R;
import com.android.ranit.contactsentrepot.common.Constants;
import com.android.ranit.contactsentrepot.data.ContactResponse;
import com.android.ranit.contactsentrepot.data.response.DataResponse;
import com.android.ranit.contactsentrepot.data.response.StateDefinition;
import com.android.ranit.contactsentrepot.databinding.ActivityMainBinding;
import com.android.ranit.contactsentrepot.contract.IMainActivityContract;
import com.android.ranit.contactsentrepot.viewModel.MainActivityViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created By: Ranit Raj Ganguly on 15/04/2021
 */
public class MainActivity extends AppCompatActivity implements IMainActivityContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;

    private HandlerThread importContactsHandlerThread;
    private Handler contactsHandler;

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

    private final String[] PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private List<ContactResponse> contactsList;

    /**
     * Observer for getContactsFromCPLiveData
     */
    private final Observer<DataResponse<ContactResponse>> importContactsFromCPObserver = contactResponse ->  {
        Log.e(TAG, "importContactsFromCPObserver onChanged()");

        if (contactResponse.getState() == StateDefinition.State.SUCCESS) {
            setupLottieAnimation(NO_DATA_ANIMATION);

            if (contactResponse.getData().size() > 0) {
                contactsList.clear();
                contactsList.addAll(contactResponse.getData());
                displaySnackBar("Retrieved "+contactsList.size()+" contacts from device.");

            } else {
                displaySnackBar("No contacts found");
                setupLottieAnimation(ERROR_ANIMATION);
            }

        } else if (contactResponse.getState() == StateDefinition.State.ERROR) {
            setupLottieAnimation(ERROR_ANIMATION);

            String errorMessage = (contactResponse.getErrorData().getErrorStatus()
                    + contactResponse.getErrorData().getErrorMessage());

            displaySnackBar(errorMessage);
        } else {
            setupLottieAnimation(LOADING_ANIMATION);
        }
    };

    /**
     * Importing contacts Runnable to parse data in a Background HandlerThread
     */
    private final Runnable importContactsRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "importContactsRunnable run: ");
            mViewModel.initiateImport();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        contactsList = new ArrayList<>();

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.getContactsFromCPLiveData().observe(this, importContactsFromCPObserver);

        initializeViews();
        setupHandlerThreads();
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean isPermissionGranted = checkPermissionsAtRuntime();

        if (isPermissionGranted) {
            importContactsButton.setOnClickListener(view -> onImportContactButtonClicked());
            exportExcelButton.setOnClickListener(view -> onExportIntoExcelButtonClicked());
            readExcelButton.setOnClickListener(view -> onReadFromExcelButtonClicked());
        } else {
            requestPermissions();
        }

        shareButton.setOnClickListener(view -> onShareButtonClicked());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        importContactsHandlerThread.quitSafely();

        mViewModel.getContactsFromCPLiveData().removeObservers(this);
    }

    @Override
    public void initializeViews() {
        Log.e(TAG, "initializeViews: ");
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
        Log.e(TAG, "setupLottieAnimation: ");
        if (animationView.isAnimating()) {
            animationView.cancelAnimation();
        }
        animationView.setAnimation(animationName);
        animationView.playAnimation();
    }

    @Override
    public void setupHandlerThreads() {
        Log.e(TAG, "setupHandlerThreads: ");

        // Import Contacts handler thread
        importContactsHandlerThread = new HandlerThread("ImportContactsThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        importContactsHandlerThread.start();
        contactsHandler = new Handler(importContactsHandlerThread.getLooper());
    }

    @Override
    public void destroyHandlerThreads() {
        Log.e(TAG, "destroyHandlerThreads: ");
    }

    @Override
    public void onImportContactButtonClicked() {
        Log.e(TAG, "onImportContactButtonClicked: ");
        contactsHandler.post(importContactsRunnable);
    }

    @Override
    public void onExportIntoExcelButtonClicked() {
        Log.e(TAG, "onExportIntoExcelButtonClicked: ");
        displaySnackBar("Exporting data into Excel...");
        mViewModel.initiateExport();
    }

    @Override
    public void onReadFromExcelButtonClicked() {
        Log.e(TAG, "onReadFromExcelButtonClicked: ");
        displaySnackBar("Reading data from excel...");
        mViewModel.initiateRead();
    }

    @Override
    public void onShareButtonClicked() {
        Log.e(TAG, "onShareButtonClicked: ");
        mViewModel.initiateSharing();
    }

    @Override
    public void switchVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    @Override
    public void enableUIComponents() {
        Log.e(TAG, "enableUIComponents: ");
        importContactsButton.setClickable(true);
        exportExcelButton.setClickable(true);
        readExcelButton.setClickable(true);
    }

    @Override
    public void disableUIComponents() {
        Log.e(TAG, "disableUIComponents: ");
        importContactsButton.setClickable(false);
        exportExcelButton.setClickable(false);
        readExcelButton.setClickable(false);
    }

    @Override
    public void setupRecyclerView() {
        Log.e(TAG, "setupRecyclerView: ");
        switchVisibility(animationView, View.GONE);
    }

    @Override
    public void displaySnackBar(String message) {
        Snackbar.make(constraintLayout, message, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean checkPermissionsAtRuntime() {
        Log.e(TAG, "checkPermissionsAtRuntime: ");
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestPermissions() {
        Log.e(TAG, "requestPermissions: ");
        ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.REQUEST_PERMISSION_ALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isAlertDialogInflated = false;

        if (requestCode == Constants.REQUEST_PERMISSION_ALL) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // Called when user selects 'NEVER ASK AGAIN'
                        isAlertDialogInflated = true;

                    } else {
                        // Called when user selects 'DENY'
                        displaySnackBar("Enable all permissions");
                        disableUIComponents();
                    }
                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // Called when user selects 'ALLOW'
                    enableUIComponents();
                }
            }

            inflateAlertDialog(isAlertDialogInflated);
        }

    }

    /**
     * Method: Show Alert Dialog when User denies permission permanently
     */
    private void inflateAlertDialog(boolean isTrue) {
        if (isTrue) {
            // Inflate Alert Dialog
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Permissions Mandatory")
                    .setMessage("Kindly enable all permissions through Settings")
                    .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            launchAppSettings();
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    /**
     * Method: Launch App-Settings Screen
     */
    private void launchAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_PERMISSION_SETTING);
    }
}