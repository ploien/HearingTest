package com.example.ploie.hearingtest;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String username;
    public String firstName;
    public String lastName;

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);

        this.username = data[0];
        this.firstName = data[1];
        this.lastName = data[2];
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.username,
                                            this.firstName,
                                            this.lastName});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
