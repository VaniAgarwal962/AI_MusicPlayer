package com.example.apimusicplayer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import android.Manifest.permission
import android.content.Intent
import com.karumi.dexter.Dexter

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import com.karumi.dexter.listener.PermissionRequest
import java.io.File

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.content.ContextCompat.getSystemService

class Main2Activity : AppCompatActivity() {

    lateinit var itemsAll:ArrayList<String>
    lateinit var msongslist:ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        msongslist=findViewById(R.id.songsList)

        appExternalStorageStoragePermission()

    }


         fun appExternalStorageStoragePermission()
        {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse)
                    {
                        displayAudioSongsName()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse)
                    {

                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    )
                    {
                        token!!.continuePermissionRequest()
                    }
                }).check()
        }


    fun readOnlyAudioSongs(file: File):ArrayList<File>
    {
            val arraylist=ArrayList<File>()
        val allfiles = file.listFiles()

        for(individualfile in allfiles)
        {
            if(individualfile.isDirectory() && !individualfile.isHidden())
            {
                arraylist.addAll(readOnlyAudioSongs(individualfile))
            }
            else if(individualfile.name.endsWith(".mp3") || individualfile.name.endsWith(".aac")||individualfile.name.endsWith(".wav")||individualfile.name.endsWith(".wma"))
          //  {
                {
                    arraylist.add(individualfile)
                }
            //}
        }

        return arraylist
    }


fun displayAudioSongsName()
{
    val audioSongs:ArrayList<File> = readOnlyAudioSongs(Environment.getExternalStorageDirectory())
    //audioSongs=
   // itemsAll=arrayOf(audioSongs.size.toString())
    itemsAll=arrayListOf()
    for (songcounter in 0 until  audioSongs.size)
    {
        //itemsAll[songcounter]=audioSongs.get(songcounter).name
        itemsAll.add(audioSongs.get(songcounter).name)
    }

    val arrayadapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,itemsAll)
    msongslist.adapter=arrayadapter


    var i=0
    msongslist.setOnClickListener {
        var songName=msongslist.getItemAtPosition(i).toString()
        val intent=Intent(this,MainActivity::class.java)

        val Songlist=Songlist(audioSongs,songName,i)
            intent.putExtra("song",Songlist)
        //below are not possible hence made model "Songlist" and written above two lines
//        intent.putExtra("song",audioSongs)
//        intent.putExtra("name",songName)
//        intent.putExtra("position",i)
        i=i+1
        startActivity(intent)
    }
}
    }

