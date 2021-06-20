package com.kotlin.mvvm.repository.model


import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Developed by Waheed on 20,June,2021
 *
 */
@Entity(tableName = "todos_table")
data class TodoModel(
    @SerializedName("completed")
    val completed: Boolean = false,

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String = "",

    @SerializedName("title")
    val title: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (completed) 1 else 0)
        parcel.writeString(id)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoModel> {
        override fun createFromParcel(parcel: Parcel): TodoModel {
            return TodoModel(parcel)
        }

        override fun newArray(size: Int): Array<TodoModel?> {
            return arrayOfNulls(size)
        }
    }
}