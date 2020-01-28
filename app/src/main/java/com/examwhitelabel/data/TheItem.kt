package com.examwhitelabel.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "the_item")
@Parcelize
data class TheItem(

    @PrimaryKey
    @SerializedName(value = "id")
    val id: String,

    @ColumnInfo(name = "field1")
    @SerializedName(value = "field1")
    val field1: String,

    @ColumnInfo(name = "field2")
    @SerializedName(value = "field2")
    val field2: String,

    @ColumnInfo(name = "field3")
    @SerializedName(value = "field3")
    val field3: String,

    @ColumnInfo(name = "field4")
    @SerializedName(value = "field4")
    val field4: String
) : Parcelable
