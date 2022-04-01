package com.gongtong

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// shared 조사 적용
// sign up 코드 구현,
// 의사소통판 ui(약구현)
// 네이버 클라우드 플랫폼 연동


class SignUpActivity : AppCompatActivity() {
    lateinit var name: SharedPreferences
    lateinit var birth: SharedPreferences
    lateinit var phone: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}