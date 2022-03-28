package com.gongtong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gongtong.databinding.ActivityMainBinding
import com.gongtong.databinding.ActivitySignUpBinding
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
        binding.button.setOnClickListener { //쓰기 버튼 (닉네임을 파이어 스토어 저장)

            val aPlayerId = binding.editTextTextPersonName.text //firestore 두번째 칸 이름 (player id로 저장할 것)
            val aPlayerNickName = binding.editTextTextPersonName3.text  //필드에 저장할 넥네임 이름

            var aplayerdata = hashMapOf( //저장할 필드값들
                "nickname" to aPlayerNickName.toString() //필드가 1개 이상일 땐 , 붙인 다음 똑같이 필드를 만들면 됨
                //값은 int float 등등 가능
            )


            db.collection("player").document(aPlayerId.toString()) //첫번째칸 컬렉션.player id 저장 부분
                .set(aplayerdata) //필드데이터 설정
                .addOnSuccessListener { //쓰기 성공했을 경우
                    binding.textView.setText("write succeed " + aPlayerNickName)
                } //닉네임 필드값
                .addOnFailureListener() { //쓰기 실패 했을 경우
                    binding.textView.setText("write faile")
                }
        }

        //-----------------------------------------------------------------------
        binding.button2.setOnClickListener { //읽기 버튼

            val aPlayerId = binding.editTextTextPersonName.text //firestore 두번째 칸 이름 (읽을 데이터)

            db.collection("player") //첫번째칸 컬렉션 (player 부분 필드데이터를 전부 읽음)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) { //제대로 읽었다면
                        for (i in task.result!!) {
                            if (i.id == aPlayerId.toString()) { //입력한 데이터와 같은 이름이 있다면(player id 부분)

                                val theNickName = i.data["nickname"] //필드 데이터
                                binding.textView.text = theNickName.toString()
                                break

                            }
                        }


                    } else { //오류 발생시

                    }
                }
        }

    }
}