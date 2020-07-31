package com.example.apimusicplayer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class Songlist (
    val song:ArrayList<File>,
    val songName:String,
    val position:Int
):Parcelable
