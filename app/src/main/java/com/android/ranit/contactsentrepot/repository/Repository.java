package com.android.ranit.contactsentrepot.repository;

import android.util.Log;

import com.android.ranit.contactsentrepot.common.ExcelUtils;
import com.android.ranit.contactsentrepot.repository.contract.IRepositoryContract;

/**
 * Singleton Class to fetch data
 *
 * Created By: Ranit Raj Ganguly on 16/04/2021
 */
public class Repository implements IRepositoryContract {
    private static final String TAG = Repository.class.getSimpleName();
    private static Repository INSTANCE = null;

    private ExcelUtils mExcelUtils;

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

    }

    @Override
    public void clear() {

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
