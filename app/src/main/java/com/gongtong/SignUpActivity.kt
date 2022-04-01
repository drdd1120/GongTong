package com.gongtong

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.gongtong.databinding.ActivitySignUpBinding
import com.google.firebase.firestore.FirebaseFirestore

// shared 조사 적용
// sign up 코드 구현,
// 의사소통판 ui(약구현)
// 네이버 클라우드 플랫폼 연동

private var firebaseFirestore: FirebaseFirestore? = null

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseFirestore = FirebaseFirestore.getInstance()

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        //이름
        binding.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var regex = Regex("[가-힣]{2,10}")
                var name = binding.name.text.toString()
                if (name.matches(regex)) {
                    binding.nameTest.setTextColor(Color.parseColor("#369F36"))
                    binding.nameTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.nameTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.nameTest.setText("이름 형식이 아닙니다.")
                    binding.btnRegister.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        //날짜
        binding.birth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var regex = Regex("(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])")
                var birth = binding.birth.text.toString()
                if (birth.matches(regex)) {
                    binding.birthTest.setTextColor(Color.parseColor("#369F36"))
                    binding.birthTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.birthTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.birthTest.setText("핸드폰 형식이 아닙니다.")
                    binding.btnRegister.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })


        //전화번호
        binding.phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var regex = Regex("01[016789][0-9]{3,4}[0-9]{4}\$")
                var phone = binding.phone.text.toString()
                if (phone.matches(regex)) {
                    binding.phoneTest.setTextColor(Color.parseColor("#369F36"))
                    binding.phoneTest.setText("입력되었습니다.")
                    binding.btnRegister.isEnabled = true
                }
                else {
                    binding.phoneTest.setTextColor(Color.parseColor("#ff0000"))
                    binding.phoneTest.setText("핸드폰 형식이 아닙니다.")
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
            var userDTO = UserDTO()
            userDTO.name = binding.name.text.toString()
            userDTO.birth = binding.birth.text.toString()
            userDTO.phone = binding.phone.text.toString()

            if(userDTO.name!!.isNotEmpty() && userDTO.birth!!.isNotEmpty() && userDTO.phone!!.isNotEmpty()){
                firebaseFirestore?.collection("userinfo")?.document(userDTO.birth!!.plus(userDTO.phone!!))?.set(userDTO)
                    ?.addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "사용자 정보 등록 완료",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }?.addOnFailureListener {
                        Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                    }
            }

        }

    }
}