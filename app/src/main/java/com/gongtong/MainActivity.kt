package com.gongtong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gongtong.databinding.ActivityMainBinding
import com.gongtong.home.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private var firebaseFirestore: FirebaseFirestore? = null
private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore //파이어베이스.파이어스토어 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var prefKey = MyApplication.prefs.getString("prefkey", "")

        if(prefKey!=""){
            //추후 삭제해야됨
            Toast.makeText(this, prefKey.toString(), Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}