package com.gongtong.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gongtong.databinding.ActivityMainBinding
import com.gongtong.databinding.ActivitySignUpBinding
import com.gongtong.databinding.ActivityVoiceSettingBinding

class VoiceSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityVoiceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}