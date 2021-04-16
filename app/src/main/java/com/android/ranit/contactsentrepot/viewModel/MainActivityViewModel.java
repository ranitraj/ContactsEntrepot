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

import com.android.ranit.contactsentrepot.common.Constants;
import com.android.ranit.contactsentrepot.common.ExcelUtils;
import com.android.ranit.contactsentrepot.contract.IMainActivityContract;
import com.android.ranit.contactsentrepot.data.ContactResponse;
import com.android.ranit.contactsentrepot.data.response.BooleanResponse;
import com.android.ranit.contactsentrepot.data.response.DataResponse;
import com.android.ranit.contactsentrepot.data.response.ErrorData;
import com.android.ranit.contactsentrepot.data.response.StateDefinition;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel
        implements IMainActivityContract.ViewModel {
    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private final List<ContactResponse> contactResponseList;
    private List<ContactResponse> parsedExcelDataList;

    private final MutableLiveData<DataResponse<ContactResponse>> contactsMLD;
    private final MutableLiveData<BooleanResponse> generateExcelMLD;
    private final MutableLiveData<DataResponse<ContactResponse>> excelContactsDataMLD;

    // Constructor
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        contactResponseList = new ArrayList<>();
        parsedExcelDataList = new ArrayList<>();

        contactsMLD = new MutableLiveData<>();
        generateExcelMLD = new MutableLiveData<>();
        excelContactsDataMLD = new MutableLiveData<>();
    }

    @Override
    public void initiateImport() {
        Log.e(TAG, "initiateImport: ");
        DataResponse<ContactResponse> response;

        // Initially setting Status as 'LOADING' and set/post value to contactsMLD
        response = new DataResponse(StateDefinition.State.LOADING, null, null);
        setContactsMLD(response);

        queryContactsContentProvider();

        Log.e(TAG, "initiateImport SIZE: " + contactResponseList.size());

        if (contactResponseList.size() > 0) {
            response = new DataResponse(StateDefinition.State.SUCCESS, contactResponseList, null);
        } else {
            response = new DataResponse(StateDefinition.State.ERROR, null,
                    new ErrorData(StateDefinition.ErrorState.INTERNAL_ERROR, "No Contacts queried"));
        }
        setContactsMLD(response);
    }

    @Override
    public void initiateExport(List<ContactResponse> dataList) {
        Log.e(TAG, "initiateExport: ");
        BooleanResponse response;

        // Initially setting Status as 'LOADING' and set/post value to generateExcelMLD
        response = new BooleanResponse(StateDefinition.State.LOADING, false, null);
        setGenerateExcelMLD(response);

        boolean isExcelGenerated = ExcelUtils.exportDataIntoWorkbook(getApplication(),
                Constants.EXCEL_FILE_NAME, dataList);

        if (isExcelGenerated) {
            response = new BooleanResponse(StateDefinition.State.SUCCESS, true, null);
        } else {
            response = new BooleanResponse(StateDefinition.State.ERROR, false,
                    new ErrorData(StateDefinition.ErrorState.EXCEL_GENERATION_ERROR, "Excel not generated"));
        }

        setGenerateExcelMLD(response);
    }

    @Override
    public void initiateRead() {
        Log.e(TAG, "initiateRead: ");
        DataResponse<ContactResponse> response;

        // Initially setting Status as 'LOADING' and set/post value to excelContactsDataMLD
        response = new DataResponse(StateDefinition.State.LOADING, null, null);
        readExcelMLD(response);

        parsedExcelDataList = ExcelUtils.readFromExcelWorkbook(getApplication(),
                Constants.EXCEL_FILE_NAME);

         if (parsedExcelDataList.size() > 0) {
             response = new DataResponse(StateDefinition.State.SUCCESS, parsedExcelDataList, null);
         } else {
             response = new DataResponse(StateDefinition.State.ERROR, null,
                     new ErrorData(StateDefinition.ErrorState.FILE_NOT_FOUND_ERROR, "Error reading data from excel"));
         }

        readExcelMLD(response);
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
     * Live Data for status of Excel Workbook Generation
     */
    public LiveData<BooleanResponse> isExcelGeneratedLiveData() {
        return generateExcelMLD;
    }

    /**
     * Live Data for Reading Excel Workbook data
     */
    public LiveData<DataResponse<ContactResponse>> readContactsFromExcelLiveData() {
        return excelContactsDataMLD;
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
     * Set/ Post Value for Generate Excel MLD
     */
    private void setGenerateExcelMLD(BooleanResponse response) {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            // Post data in Main-Thread
            generateExcelMLD.setValue(response);
        } else {
            // Post data in BG-Thread
            generateExcelMLD.postValue(response);
        }
    }

    /**
     * Set/ Post Value for Read Excel MLD
     */
    private void readExcelMLD(DataResponse<ContactResponse> response) {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            // Post data in Main-Thread
            excelContactsDataMLD.setValue(response);
        } else {
            // Post data in BG-Thread
            excelContactsDataMLD.postValue(response);
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

                if (name != null) {
                    // Check if current contact has phone numbers
                    if (contactCursor.getInt(contactCursor
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        List<ContactResponse.PhoneNumber> phoneNumberList = new ArrayList<>();

                        // Query
                        Cursor phoneNumberCursor = contentResolver
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                        null);

                        // Iterate
                        while (phoneNumberCursor.moveToNext()) {
                            String phoneNumber = phoneNumberCursor.getString(phoneNumberCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            phoneNumberList.add(new ContactResponse.PhoneNumber(phoneNumber));
                        }
                        contactResponseList.add(new ContactResponse(id, name, phoneNumberList));
                    }
                }
            }
        }
    }
}
