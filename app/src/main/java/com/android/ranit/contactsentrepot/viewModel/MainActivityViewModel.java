package com.android.ranit.contactsentrepot.viewModel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.ranit.contactsentrepot.contract.IMainActivityContract;
import com.android.ranit.contactsentrepot.data.ContactResponse;
import com.android.ranit.contactsentrepot.data.response.DataResponse;
import com.android.ranit.contactsentrepot.data.response.ErrorData;
import com.android.ranit.contactsentrepot.data.response.StateDefinition;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel
        implements IMainActivityContract.ViewModel {
    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private List<ContactResponse> contactResponseList;
    private List<ContactResponse.PhoneNumber> phoneNumberList;

    private MutableLiveData<DataResponse<ContactResponse>> contactsMLD;

    // Constructor
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        contactResponseList = new ArrayList<>();
        phoneNumberList = new ArrayList<>();

        contactsMLD = new MutableLiveData<>();
    }

    @Override
    public void initiateImport() {
        Log.e(TAG, "initiateImport: ");
        DataResponse<ContactResponse> response;

        // Initially setting Status as 'LOADING' and set/post value to contactsMLD
        response = new DataResponse(StateDefinition.State.LOADING, null, null);
        setContactsMLD(response);

        queryContactsContentProvider();

        Log.e(TAG, "initiateImport SIZE: "+contactResponseList.size() );

        if (contactResponseList != null && contactResponseList.size() > 0) {
            response = new DataResponse(StateDefinition.State.SUCCESS, contactResponseList, null);
        } else {
            response = new DataResponse(StateDefinition.State.ERROR, null,
                    new ErrorData(StateDefinition.ErrorState.INTERNAL_ERROR, "No Contacts queried"));
        }
        setContactsMLD(response);
    }

    @Override
    public void initiateExport() {
        Log.e(TAG, "initiateExport: ");
    }

    @Override
    public void initiateRead() {
        Log.e(TAG, "initiateRead: ");
    }

    @Override
    public void initiateSharing() {

    }

    /**
     * Live Data for Querying of Content Provider
     */
    public LiveData<DataResponse<ContactResponse>> getContactsFromCPLiveData() {
        return contactsMLD;
    }

    /**
     * Set/ Post Value for Contacts MLD
     */
    private void setContactsMLD(DataResponse<ContactResponse> response) {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            // Post data in Main-Thread
            contactsMLD.setValue(response);
        } else {
            // Post data in BG-Thread
            contactsMLD.postValue(response);
        }
    }

    /**
     * Method: Queries Contacts Content Provider to access contacts
     */
    private void queryContactsContentProvider() {
        contactResponseList.clear();

        ContentResolver contentResolver = getApplication().getContentResolver();

        Cursor contactCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (contactCursor != null && contactCursor.getCount() > 0) {
            // Iterate
            while (contactCursor.moveToNext()) {
                String id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // Check if current contact has phone numbers
                if (contactCursor.getInt(contactCursor
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    // Query
                    Cursor phoneNumberCursor = contentResolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                    null);

                    // Iterate
                    phoneNumberList.clear();

                    while (phoneNumberCursor.moveToNext()) {
                        String phoneNumber = phoneNumberCursor.getString(phoneNumberCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneNumberList.add(new ContactResponse.PhoneNumber(phoneNumber));
                    }
                }
                contactResponseList.add(new ContactResponse(id, name, phoneNumberList));
            }
        }
    }
}
