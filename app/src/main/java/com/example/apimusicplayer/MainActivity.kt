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
    lateinit var keeper:String


    lateinit var play_pause:ImageView
    lateinit var previous:ImageView
    lateinit var next:ImageView
    lateinit var songName:TextView
    lateinit var musicImage:ImageView
    lateinit var Relativelower:RelativeLayout
    lateinit var btnEnableVoice:MaterialButton

    var mode:String="ON"


    lateinit var mediaplayer:MediaPlayer
    var position:Int =0
    lateinit var mysongs:ArrayList<File>
    var msongName:String="ee"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        checkVoiceCommandPermission()
        parentRelativeLayout=findViewById(R.id.parentRelativeLayout)


        play_pause=findViewById(R.id.imgplay_pause)
        previous=findViewById(R.id.imgprevious)
        next=findViewById(R.id.imgnext)
        songName=findViewById(R.id.txtsongName)
        musicImage=findViewById(R.id.imglogo)
        Relativelower=findViewById(R.id.Relativelower)
        btnEnableVoice=findViewById(R.id.btnEnableVoice)


        speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
        speechRecognizerIntent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())



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

            override fun onResults(p0: Bundle?) {
                val matchesFound:ArrayList<String> = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
                if(matchesFound!=null)
                {
                    keeper=matchesFound.get(0)
                    Toast.makeText(this@MainActivity,"Result= "+keeper,Toast.LENGTH_SHORT).show()
                }

            }

        })


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
                btnEnableVoice.text="Enable Voice Command"
                Toast.makeText(this,"Press to enable voice command",Toast.LENGTH_LONG).show()
                Relativelower.visibility=View.VISIBLE
            }
            else{
                mode="ON";
                btnEnableVoice.text="Voice Command Enabled"
                Toast.makeText(this,"Speak to play song",Toast.LENGTH_LONG).show()
                Relativelower.visibility=View.GONE
            }
        }

    }


    private fun ValidateReceiveValuesandStartPlaying()
    {
        if(mediaplayer!=null)
        {
            mediaplayer.stop()
            mediaplayer.release()
        }

        mysongs=intent.getParcelableArrayListExtra<File>("song")

    }



    private fun checkVoiceCommandPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if(!(ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED))

        {
            val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+packageName))
            startActivity(intent)
            finish()
        }
    }



}
