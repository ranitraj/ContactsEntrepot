package com.android.ranit.contactsentrepot.data;

import java.util.List;

public class ContactResponse {
    private String id;
    private String name;
    private List<PhoneNumber> phoneNumberList;

    // Constructors
    public ContactResponse(String id, String name, List<PhoneNumber> phoneNumbers) {
        this.id = id;
        this.name = name;
        this.phoneNumberList = phoneNumbers;
    }

    public ContactResponse(String name, List<PhoneNumber> phoneNumbers) {
        this.name = name;
        this.phoneNumberList = phoneNumbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneNumber> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    // Static Inner Class
    public static class PhoneNumber {
        private String number;

        // Constructor
        public PhoneNumber(String phone) {
            this.number = phone;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
