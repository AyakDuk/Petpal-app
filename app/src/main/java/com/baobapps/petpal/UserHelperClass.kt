package com.baobapps.petpal

import android.os.Parcel
import android.os.Parcelable

data class UserHelperClass(
    var firstName: String = "",
    var lastName: String = "",
    var userName: String = "",
    var emailAddress: String = "",
    var phoneNumber: String = "",
    var password: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(userName)
        parcel.writeString(emailAddress)
        parcel.writeString(phoneNumber)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserHelperClass> {
        override fun createFromParcel(parcel: Parcel): UserHelperClass {
            return UserHelperClass(parcel)
        }

        override fun newArray(size: Int): Array<UserHelperClass?> {
            return arrayOfNulls(size)
        }
    }
}
