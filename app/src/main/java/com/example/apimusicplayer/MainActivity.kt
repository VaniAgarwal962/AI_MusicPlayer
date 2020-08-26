package com.example.apimusicplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var parentRelativeLayout:RelativeLayout

    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var speechRecognizerIntent: Intent
     var keeper=""


    lateinit var play_pause:ImageView
    lateinit var previous:ImageView
    lateinit var next:ImageView
    lateinit var songName:TextView
    lateinit var musicImage:ImageView
    lateinit var Relativelower:RelativeLayout
    lateinit var btnEnableVoice:MaterialButton

    var mode:String="ON"


    lateinit var mediaplayer: MediaPlayer
    //var mediaplayer:MediaPlayer?=null
     var position:Int=0
    lateinit var mySongs:ArrayList<File>
    lateinit var mSongName:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        checkVoiceCommandPermission()
        mediaplayer= MediaPlayer()






        speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
        speechRecognizerIntent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        parentRelativeLayout=findViewById(R.id.parentRelativeLayout)


        play_pause=findViewById(R.id.imgplay_pause)
        previous=findViewById(R.id.imgprevious)
        next=findViewById(R.id.imgnext)
        songName=findViewById(R.id.txtsongName)
        musicImage=findViewById(R.id.imglogo)
        Relativelower=findViewById(R.id.Relativelower)
        btnEnableVoice=findViewById(R.id.btnEnableVoice)





        ValidateReceiveValuesandStartPlaying()
        musicImage.setBackgroundResource(R.drawable.mymusiclogo)


        speechRecognizer.setRecognitionListener(object:RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) {
            }

            override fun onRmsChanged(p0: Float) {
            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(result: Bundle?) {



                val matchesFound = result!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)





                if(matchesFound!=null) {



                    if (mode=="ON") {



                        keeper = matchesFound.get(0)



                        if (keeper=="pause the song") {



                            playpauseSong()

                            Toast.makeText(this@MainActivity, "Command= "+keeper, Toast.LENGTH_LONG).show()

                        }
                        else if (keeper=="play the song") {
                            playpauseSong()
                            Toast.makeText(this@MainActivity, "Command= "+keeper, Toast.LENGTH_LONG)
                                .show()
                        }

                        //                 Toast.makeText(this@MainActivity,"Result= "+keeper,Toast.LENGTH_SHORT).show()


                        else if (keeper=="play next song") {
                            playNextSong()
                            Toast.makeText(this@MainActivity, "Command= "+keeper, Toast.LENGTH_LONG)
                                .show()
                        }
                        else if (keeper=="play previous song") {
                            playPreviousSong()
                            Toast.makeText(this@MainActivity, "Command= "+keeper, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
        )


        parentRelativeLayout.setOnTouchListener(View.OnTouchListener{v,motionEvent->
            when(motionEvent.action)
            {
                MotionEvent.ACTION_DOWN->
                {
                    speechRecognizer.startListening(speechRecognizerIntent)
                    keeper=""
                }
                MotionEvent.ACTION_UP->
                {
                    speechRecognizer.stopListening()
                }
            }
            return@OnTouchListener false
        }
        )




        btnEnableVoice.setOnClickListener {

            if(mode=="ON")
            {
                mode="OFF";
                btnEnableVoice.text="Voice Command is OFF"
                //Toast.makeText(this,"Press to enable voice command",Toast.LENGTH_LONG).show()
                Relativelower.visibility=View.VISIBLE


            }
            else{
                mode="ON";
                btnEnableVoice.text="Voice Command is ON"
               // Toast.makeText(this,"Speak to play song",Toast.LENGTH_LONG).show()
                Relativelower.visibility=View.GONE
            }
        }

        play_pause.setOnClickListener {
            playpauseSong()
        }

        previous.setOnClickListener {
            if(mediaplayer.currentPosition>0)
                playPreviousSong()

        }
        next.setOnClickListener {
            if(mediaplayer.currentPosition>0)
                playNextSong()

        }


    }


    fun ValidateReceiveValuesandStartPlaying()
    {

        val songlist=intent.getParcelableExtra<Songlist>("song")
        mySongs=songlist!!.song
        mSongName=songlist.songName
        position=songlist.position
        songName.text=mSongName
        songName.isSelected=true

        val uri=Uri.parse(mySongs.get(position).toString())



        if(mediaplayer!=null)
        {
            mediaplayer.stop()
            mediaplayer.release()
         //   Toast.makeText(this,"heyyyyy",Toast.LENGTH_LONG).show()

        }
        mediaplayer=MediaPlayer.create(this,uri)

        mediaplayer.start()

    }



     fun checkVoiceCommandPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if(!(ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED))

        {
            val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+packageName))
            startActivity(intent)
            finish()
        }
    }


     fun playpauseSong()
    {
        musicImage.setBackgroundResource(R.drawable.one)
        if(mediaplayer.isPlaying)
        {
            play_pause.setImageResource(R.drawable.play)
            mediaplayer.pause()
        }
        else
        {
            play_pause.setImageResource(R.drawable.pause)
            mediaplayer.start()
            musicImage.setBackgroundResource(R.drawable.two)
        }
    }

    override fun onDestroy() {
        mediaplayer.stop()
        mediaplayer.release()
        super.onDestroy()
    }

    fun playNextSong()
    {
        mediaplayer.pause()
        mediaplayer.stop()
        mediaplayer.release()

        position=((position+1)%mySongs.size)
         val uri=Uri.parse(mySongs.get(position).toString())
        mediaplayer=MediaPlayer.create(this,uri)
        mSongName=mySongs.get(position).toString()
        songName.text=mSongName
        musicImage.setBackgroundResource(R.drawable.three)

        if(mediaplayer.isPlaying)
        {
            play_pause.setImageResource(R.drawable.pause)

        }
        else
        {
            play_pause.setImageResource(R.drawable.play)

            musicImage.setBackgroundResource(R.drawable.two)
        }
    }

    fun playPreviousSong()
    {
        mediaplayer.pause()
        mediaplayer.stop()
        mediaplayer.release()

        if((position-1)<0)
            position=(mySongs.size-1)
        else
            position=(position-1)

        val uri=Uri.parse(mySongs.get(position).toString())
        mediaplayer=MediaPlayer.create(this,uri)
        mSongName=mySongs.get(position).toString()
        songName.text=mSongName
        musicImage.setBackgroundResource(R.drawable.four)


        if(mediaplayer.isPlaying)
        {
            play_pause.setImageResource(R.drawable.pause)

        }
        else
        {
            play_pause.setImageResource(R.drawable.play)

            musicImage.setBackgroundResource(R.drawable.two)
        }
    }
}
