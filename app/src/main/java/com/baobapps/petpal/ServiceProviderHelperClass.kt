package com.baobapps.petpal

import android.os.Parcel
import android.os.Parcelable

data class ServiceProviderHelperClass(
    var firstName: String = "",
    var lastName: String = "",
    var emailAddress: String = "",
    var phoneNumber: String = "",
    var location: String = "",
    var serviceType: String = ""
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
        parcel.writeString(emailAddress)
        parcel.writeString(phoneNumber)
        parcel.writeString(location)
        parcel.writeString(serviceType)
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<ServiceProviderHelperClass> {
        override fun createFromParcel(parcel: Parcel): ServiceProviderHelperClass {
            return ServiceProviderHelperClass(parcel)
        }
        override fun newArray(size: Int): Array<ServiceProviderHelperClass?> {
            return arrayOfNulls(size)
        }
    }
}