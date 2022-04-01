package com.gongtong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gongtong.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private var firebaseFirestore: FirebaseFirestore? = null

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val db = Firebase.firestore //파이어베이스.파이어스토어 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //-----------------------------------------------------------------------
        binding.button2.setOnClickListener { //읽기 버튼
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}