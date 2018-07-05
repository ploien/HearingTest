package com.example.ploie.hearingtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The User class is used to store temporary information for the application to use for screening
 * and for saving the User's results. User is Parcelable so that a user object can be passed between
 * activities for use in other activities.
 */
public class User implements Parcelable {

    /**
     * The username is the email of the current user signed in through Firebase Auth.
     */
    public String username;
    /**
     * The name is the name of the current screening participant.
     */
    public String name;

    /**
     * Constructor for the Current User.
     * @param username
     * @param name
     */
    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    /**
     * This is a constructor for the parcelable User Object.
     * @param in
     */
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


    /**
     * Used by Parcelable to pass the User.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Takes a user and writes it into a parcelable version of itself.
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.username,
                                            this.name});
    }

    /**
     * Actually creates the Parcelled User.
     */
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
