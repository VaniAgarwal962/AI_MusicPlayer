//package com.example.aimusicplayer
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Build
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.Settings
//import android.speech.RecognitionListener
//import android.speech.RecognizerIntent
//import android.speech.SpeechRecognizer
//import android.view.MotionEvent
//import android.view.View
//import android.widget.*
//import androidx.core.content.ContextCompat
//import androidx.core.view.get
//import kotlinx.android.synthetic.main.activity_main.*
//import java.io.File
//import java.text.FieldPosition
//import java.util.*
//
//
//class SmartPlayerActivity : AppCompatActivity() {
//    lateinit var parentRelativeLayout: RelativeLayout
//
//
//    lateinit var speechRecognizer: SpeechRecognizer
//
//    lateinit var speechRecognizerIntent: Intent
//
//    var keeper=""
//
//    lateinit var songLogo:ImageView
//
//    lateinit var upper:RelativeLayout
//
//    lateinit var songName:TextView
//
//    lateinit var lower:RelativeLayout
//    lateinit var previous_btn: ImageView
//    lateinit var play_pause_btn:ImageView
//    lateinit var next_btn:ImageView
//    lateinit var voice_enabled_btn:Button
//
//    lateinit var myMediaPlayer:MediaPlayer
//    var position: Int=0;
//    lateinit var mysongs:ArrayList<File>
//    lateinit var mSongName:String
//    var mode="ON"
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_smart_player)
//        checkVoiceCommandPermission()
//        myMediaPlayer= MediaPlayer()
//        parentRelativeLayout=findViewById(R.id.parentRelativeLayout)
//        speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this@SmartPlayerActivity)
//        speechRecognizerIntent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//        songLogo=findViewById(R.id.songLogo)
//        upper=findViewById(R.id.upper)
//        songName=findViewById(R.id.songName)
//        lower=findViewById(R.id.lower)
//        previous_btn=findViewById(R.id.previous_btn)
//        play_pause_btn=findViewById(R.id.play_pause_btn)
//        next_btn=findViewById(R.id.next_btn)
//        voice_enabled_btn=findViewById(R.id.voice_enabled_btn)
//
//        validateReceiveValuesAndStartPlaying()
//        songLogo.setBackgroundResource(R.drawable.logo)
//
//        lower.visibility=View.GONE
//
//        speechRecognizer.setRecognitionListener(
//            object : RecognitionListener {
//                override fun onReadyForSpeech(params: Bundle?) {
//                }
//
//                override fun onRmsChanged(rmsdB: Float) {
//                    // onRmsChanged(rmsdB)
//                }
//
//                override fun onBufferReceived(buffer: ByteArray?) {
//                }
//
//                override fun onPartialResults(partialResults: Bundle?) {
//                }
//
//                override fun onEvent(eventType: Int, params: Bundle?) {
//                }
//
//                override fun onBeginningOfSpeech() {
//                }
//
//                override fun onEndOfSpeech() {
//                }
//
//                override fun onError(error: Int) {
//                }
//
//                override fun onResults(results: Bundle?) {
//                    val matchFounder=results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//
//                    if(matchFounder!=null){
//                        if(mode=="ON"){
//                            keeper=matchFounder.get(0)
//                            if(keeper=="pause the song"){
//                                playAndPause()
//
//                                Toast.makeText(this@SmartPlayerActivity,"Command: $keeper",Toast.LENGTH_LONG).show()
//
//                            }
//                            else if(keeper=="play the song"){
//                                playAndPause()
//
//                                Toast.makeText(this@SmartPlayerActivity,"Command: $keeper",Toast.LENGTH_LONG).show()
//
//                            }
//                            else if(keeper=="play next song"){
//                                if(myMediaPlayer.currentPosition>0)
//                                    playNextSong()
//
//                                Toast.makeText(this@SmartPlayerActivity,"Command: $keeper",Toast.LENGTH_LONG).show()
//
//                            }
//                            else if(keeper=="play previous song"){
//                                if(myMediaPlayer.currentPosition>0)
//                                    playPreviousSong()
//
//                                Toast.makeText(this@SmartPlayerActivity,"Command: $keeper",Toast.LENGTH_LONG).show()
//
//                            }
//
//
//
//                        }
//                    }
//                }
//
//            }
//        )
//
//        parentRelativeLayout.setOnTouchListener(View.OnTouchListener { v, motionEvent ->
//            when(motionEvent.action){
//                MotionEvent.ACTION_DOWN ->{
//                    speechRecognizer.startListening(speechRecognizerIntent)
//                    keeper=""
//
//                }
//                MotionEvent.ACTION_UP ->{
//                    speechRecognizer.stopListening()
//                }
//            }
//            return@OnTouchListener false
//        })
//
//        voice_enabled_btn.setOnClickListener {
//            if(mode=="ON"){
//                mode="OFF"
//                voice_enabled_btn.text="Voice Enabled Mode-OFF"
//                lower.visibility=View.VISIBLE
//            }
//            else{
//                mode="ON"
//                voice_enabled_btn.text="Voice Enabled Mode-ON"
//                lower.visibility=View.GONE
//
//            }
//
//        }
//        play_pause_btn.setOnClickListener{
//            playAndPause()
//        }
//        next_btn.setOnClickListener{
//            if(myMediaPlayer.currentPosition>0)
//                playNextSong()
//        }
//        previous_btn.setOnClickListener{
//            if(myMediaPlayer.currentPosition>0)
//                playPreviousSong()
//        }
//    }
//    fun checkVoiceCommandPermission(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if(!(ContextCompat.checkSelfPermission(this@SmartPlayerActivity,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED)){
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+packageName))
//                startActivity(intent)
//                finish()
//            }
//        }
//    }
//
//    fun validateReceiveValuesAndStartPlaying(){
//        //   var myMediaPlayer:MediaPlayer?=null
//        val songsModel=intent.getParcelableExtra<SongsModel>("song")
//        mysongs=songsModel.mysong
//        mSongName=songsModel.songName
//        position=songsModel.position
//        songName.text=mSongName
//        songName.isSelected=true
//        val uri =Uri.parse(mysongs.get(position).toString())
////        myMediaPlayer=Driver.getmediaPlayer(this@SmartPlayerActivity,uri) as MediaPlayer
//
//
//        if(myMediaPlayer !=null){
//            myMediaPlayer!!.stop()
//            myMediaPlayer!!.release()
//            Toast.makeText(this,"hhdkjsh",Toast.LENGTH_LONG).show()
//        }
//
//        myMediaPlayer = MediaPlayer.create(this@SmartPlayerActivity,uri)
//        myMediaPlayer.start()
//    }
//
//    override fun onBackPressed() {
//        // myMediaPlayer.stop()
//        //    myMediaPlayer.release()
//        super.onBackPressed()
//    }
//
//    override fun onDestroy() {
//        myMediaPlayer.stop()
//        myMediaPlayer.release()
//
//        super.onDestroy()
//    }
//    fun playAndPause(){
//        songLogo.setBackgroundResource(R.drawable.four)
//        if(myMediaPlayer.isPlaying){
//            play_pause_btn.setImageResource(R.drawable.play)
//            myMediaPlayer.pause()
//        }
//        else{
//            play_pause_btn.setImageResource(R.drawable.pause)
//            myMediaPlayer.start()
//
//        }
//    }
//    fun playNextSong(){
//        myMediaPlayer.stop()
//        myMediaPlayer.stop()
//        myMediaPlayer.release()
//        position=(position+1)%mysongs.size
//        val uri =Uri.parse(mysongs.get(position).toString())
//        myMediaPlayer=MediaPlayer.create(this@SmartPlayerActivity,uri)
//        mSongName=mysongs.get(position).toString()
//        songName.text=mSongName
//        myMediaPlayer.start()
//        songLogo.setImageResource(R.drawable.three)
//
//
//        if(myMediaPlayer.isPlaying){
//            play_pause_btn.setImageResource(R.drawable.pause)
//            // myMediaPlayer.pause()
//        }
//        else{
//            play_pause_btn.setImageResource(R.drawable.play)
//            //  myMediaPlayer.start()
//            songLogo.setBackgroundResource(R.drawable.five)
//        }
//    }
//    fun playPreviousSong(){
//        myMediaPlayer.stop()
//        myMediaPlayer.stop()
//        myMediaPlayer.release()
//        if((position-1)<0){
//            position=mysongs.size-1
//        }
//        else{
//            position=position-1
//        }
//        val uri =Uri.parse(mysongs.get(position).toString())
//        myMediaPlayer=MediaPlayer.create(this@SmartPlayerActivity,uri)
//
//
//        mSongName=mysongs.get(position).toString()
//        songName.text=mSongName
//        myMediaPlayer.start()
//        songLogo.setImageResource(R.drawable.two)
//
//
//        if(myMediaPlayer.isPlaying){
//            play_pause_btn.setImageResource(R.drawable.pause)
//            // myMediaPlayer.pause()
//        }
//        else{
//            play_pause_btn.setImageResource(R.drawable.play)
//            //  myMediaPlayer.start()
//            songLogo.setBackgroundResource(R.drawable.five)
//        }
//    }
//}