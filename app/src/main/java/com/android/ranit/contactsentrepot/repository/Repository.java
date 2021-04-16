package com.android.ranit.contactsentrepot.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.ranit.contactsentrepot.common.ExcelUtils;
import com.android.ranit.contactsentrepot.repository.contract.IRepositoryContract;
import com.android.ranit.contactsentrepot.repository.data.ContactResponse;
import com.android.ranit.contactsentrepot.repository.response.DataResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Class to fetch data
 *
 * Created By: Ranit Raj Ganguly on 16/04/2021
 */
public class Repository implements IRepositoryContract {
    private static final String TAG = Repository.class.getSimpleName();
    private static Repository INSTANCE = null;

    private ExcelUtils mExcelUtils;

    private List<ContactResponse> contactResponseList;

    private MutableLiveData<DataResponse<ContactResponse>> contactsMLD;

    /**
     * Returns a Singleton Instance of this class
     *
     * @return singleton Instance
     */
    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    // Constructor
    private Repository() {
        initialize();
    }


    @Override
    public void initialize() {
        contactResponseList = new ArrayList<>();

        contactsMLD = new MutableLiveData<>();
    }

    @Override
    public void clear() {

    }

    /**
     * Method: Retrieves all data from Contacts Application via Content Provider
     */
    public void initiateImport() {
        Log.e(TAG, "initiateImport: ");

    }

    /**
     * Method: Read Data from Excel Sheet
     */
    public void readDataFromExcel() {
        Log.e(TAG, "readDataFromExcel: ");


    }

    /**
     * Method: Generate Excel Sheet
     */
    public void generateExcel() {
        Log.e(TAG, "generateExcel: ");

    }
}
