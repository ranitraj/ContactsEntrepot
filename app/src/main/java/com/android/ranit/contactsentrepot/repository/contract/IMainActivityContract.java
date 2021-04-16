package com.android.ranit.contactsentrepot.repository.contract;

/**
 * Contract to be implemented by MainActivity (View) and MainActivityViewModel (ViewModel)
 *
 * Created by: Ranit Raj Ganguly on 15/04/21.
 */
public interface IMainActivityContract {

    // View
    interface View {
        void initializeViews();
        void setupLottieAnimation(String animationName);
        void setupHandlerThreads();
        void destroyHandlerThreads();
        void onImportContactButtonClicked();
        void onExportIntoExcelButtonClicked();
        void onReadFromExcelButtonClicked();
        void onShareButtonClicked();
        void switchVisibility(android.view.View view, int visibility);
        void enableUIComponents();
        void disableUIComponents();
        void setupRecyclerView();
        void displaySnackBar(String message);
        boolean checkPermissionsAtRuntime();
        void requestPermissions();
    }

    // View-Model
    interface ViewModel {
        void initiateImport();
        void initiateExport();
        void initiateRead();
        void initiateSharing();
    }
}
