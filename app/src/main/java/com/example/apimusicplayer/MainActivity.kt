package com.example.apimusicplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {




    lateinit var parentRelativeLayout:RelativeLayout

    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var speechRecognizerIntent: Intent
    lateinit var keeper:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        checkVoiceCommandPermission()
        parentRelativeLayout=findViewById(R.id.parentRelativeLayout)

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
