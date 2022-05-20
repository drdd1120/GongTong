package com.gongtong

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.gongtong.databinding.ActivitySignUpBinding
import com.gongtong.preference.MyApplication
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")
private var firebaseFirestore: FirebaseFirestore? = null

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseFirestore = FirebaseFirestore.getInstance()

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        //이름
        binding.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val name = binding.name.text.toString()
                val regex = Regex("[가-힣]{2,10}")
                if (name.matches(regex)) {
                    binding.nameTest.setTextColor(Color.parseColor("#369F36"))
                    binding.nameTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.nameTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.nameTest.setText("올바른 이름 형식이 아닙니다.")
                    binding.btnRegister.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        //생년월일
        binding.birth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val birth = binding.birth.text.toString()
                val regex = Regex("(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\$")
                if (birth.matches(regex)) {
                    binding.birthTest.setTextColor(Color.parseColor("#369F36"))
                    binding.birthTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.birthTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.birthTest.setText("올바른 생년월일 형식이 아닙니다.")
                    binding.btnRegister.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        //전화번호
        binding.phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val phone = binding.phone.text.toString()
                val regex = Regex("01[016789][0-9]{3,4}[0-9]{4}$")
                if (phone.matches(regex)) {
                    binding.phoneTest.setTextColor(Color.parseColor("#369F36"))
                    binding.phoneTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.phoneTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.phoneTest.setText("올바른 핸드폰 형식이 아닙니다.")
                    binding.btnRegister.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        data class UserDTO(
            var name: String? = null,
            var birth: String? = null,
            var phone: String? = null
        )

        binding.btnRegister.setOnClickListener {
            val userDTO = UserDTO()
            userDTO.name = binding.name.text.toString()
            userDTO.birth = binding.birth.text.toString()
            userDTO.phone = binding.phone.text.toString()

            val phonenumber = userDTO.phone!!
            MyApplication.prefs.setString("prefphonenumber", phonenumber)

            val key = userDTO.birth!!.plus(userDTO.phone!!)
            MyApplication.prefs.setString("prefkey", key)

            if(userDTO.name!!.isNotEmpty() && userDTO.birth!!.isNotEmpty() && userDTO.phone!!.isNotEmpty()){
                firebaseFirestore?.collection("userinfo")?.document(key)?.set(userDTO)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "사용자정보 등록 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }?.addOnFailureListener {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}