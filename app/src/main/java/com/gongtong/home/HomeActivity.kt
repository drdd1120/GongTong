package com.gongtong.home

//import kotlinx.android.synthetic.main.activity_main.*
//import com.bumptech.glide.Glide
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gongtong.databinding.ActivityHomeBinding
import com.gongtong.settings.VoiceSettingActivity
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding

    var Tag: String = "HomeActivity"
    val audioPlay = MediaPlayer()
    private var statusProgress: ProgressBar?=null;
    var gText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}