package com.example.myapplication2

import android.os.Parcel
import android.os.Parcelable

class LogData(
    var date: String,
    var time: String,
    var mealType: String,
    var glucoseReading: String,
    var carbsConsumed: String,
    var insulinTaken: String
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
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(mealType)
        parcel.writeString(glucoseReading)
        parcel.writeString(carbsConsumed)
        parcel.writeString(insulinTaken)
    }

    override fun describeContents(): Int {
        return 0
    }
//    fun getDate(): String{
//        return date;
//    }
//    fun getTime(): String{
//        return time;
//    }
//
//    fun getMealType(): String{
//        return mealType;
//    }
//    fun getGlucoseReading(): String{
//        return glucoseReading;
//    }
//    fun getCarbsConsumed(): String{
//        return carbsConsumed;
//    }
//    fun getInsulinTaken(): String{
//        return insulinTaken;
//    }


    companion object CREATOR : Parcelable.Creator<LogData> {
        override fun createFromParcel(parcel: Parcel): LogData {
            return LogData(parcel)
        }

        override fun newArray(size: Int): Array<LogData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$glucoseReading mg/dL"
    }
}
