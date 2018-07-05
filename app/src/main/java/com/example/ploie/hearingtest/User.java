package com.example.ploie.hearingtest;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String username;
    public String name;

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public User(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);

        this.username = data[0];
        this.name = data[1];
    }

    public String getUsername() {return username;}
    public void setUsername(String username) { this.username = username;}
    public String getname() { return name; }
    public void setname(String name) {
        this.name = name;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.username,
                                            this.name});
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
