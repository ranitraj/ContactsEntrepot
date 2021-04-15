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
        void onExportContactButtonClicked();
        void onShareButtonClicked();
        void setupRecyclerView();
        void changeViewVisibility();
        void displaySnackBar(String message);
    }

    // View-Model
    interface ViewModel {

    }
}
