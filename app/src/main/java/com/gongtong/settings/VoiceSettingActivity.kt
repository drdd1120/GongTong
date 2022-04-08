package com.gongtong.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gongtong.R
import com.gongtong.databinding.ActivityVoiceSettingBinding

class VoiceSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoiceSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var voiceChoice = HashMap<String, String>()
        //남자
        voiceChoice.put("민상", "nminsang")
        voiceChoice.put("신우", "nsinu")
        voiceChoice.put("진호", "njinho")
        voiceChoice.put("지훈", "njihun")
        voiceChoice.put("주안", "njooahn")
        voiceChoice.put("성훈", "nseonghoon")
        voiceChoice.put("지환", "njihwan")
        voiceChoice.put("시윤", "nsiyoon")
        voiceChoice.put("태진", "ntaejin")
        voiceChoice.put("영일", "nyoungil")
        voiceChoice.put("승표", "nseungpyo")
        voiceChoice.put("원탁", "nwontak")
        voiceChoice.put("종현", "njonghyun")
        voiceChoice.put("준영", "njoonyoung")
        voiceChoice.put("재욱", "njaewook")
        voiceChoice.put("기효", "nes_c_kihyo")
        //여자
        voiceChoice.put("고은", "ngoeun")
        voiceChoice.put("기서", "ntiffany")
        voiceChoice.put("늘봄", "napple")
        voiceChoice.put("드림", "njangj")
        voiceChoice.put("미경", "nes_c_mikyung")
        voiceChoice.put("민서", "nminseo")
        voiceChoice.put("민영", "nminyoung")
        voiceChoice.put("보라", "nbora")
        voiceChoice.put("봄달", "noyj")
        voiceChoice.put("선경", "nsunkyung")
        voiceChoice.put("선희", "nsunhee")
        voiceChoice.put("소현", "nes_c_sohyun")
        voiceChoice.put("수진", "nsujin")
        voiceChoice.put("아라", "nara")
        voiceChoice.put("예진", "nyejin")
        voiceChoice.put("유진", "nyujin")
        voiceChoice.put("은영", "neunyoung")
        voiceChoice.put("지원", "njiwon")
        voiceChoice.put("지윤", "njiyun")
        voiceChoice.put("혜리", "nes_c_hyeri")
        //아이
        voiceChoice.put("하준", "nhajun")
        voiceChoice.put("다인", "ndain")
        voiceChoice.put("가람", "ngaram")


        binding!!.men.setOnClickListener {
            val items = arrayOf("기효", "민상", "성훈", "승표", "시윤", "신우", "영일", "원탁", "재욱", "종현", "주안", "준영", "지환", "지훈", "진호", "태진")
            var selectedItem: String? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("남성 음성선택")
                .setSingleChoiceItems(items, 0){ dialog, which ->
                    selectedItem = items[which]
                }
                .setPositiveButton("저장",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(selectedItem == null){
                            selectedItem = items[0]
                        }
                        voiceChoice.get(selectedItem)
                        Toast.makeText(this, voiceChoice.get(selectedItem), Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this, "취소누름", Toast.LENGTH_SHORT).show()
                })
            builder.show()
        }

        binding!!.women.setOnClickListener {
            val items = arrayOf("고은", "기서", "늘봄", "드림", "미경", "민서", "민영", "보라", "봄달", "선경", "선희", "소현", "수진", "아라", "예진", "유진", "은영", "지원", "지윤", "혜리")
            var selectedItem: String? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("여성 음성선택")
                .setSingleChoiceItems(items, 0){ dialog, which ->
                    selectedItem = items[which]
                }
                .setPositiveButton("저장",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(selectedItem == null){
                            selectedItem = items[0]
                        }
                        voiceChoice.get(selectedItem)
                        Toast.makeText(this, voiceChoice.get(selectedItem), Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this, "취소누름", Toast.LENGTH_SHORT).show()
                })
            builder.show()
        }

        binding!!.kid.setOnClickListener {
            val items = arrayOf("가람", "다인", "하준")
            var selectedItem: String? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("아이 음성선택")
                .setSingleChoiceItems(items, 0){ dialog, which ->
                    selectedItem = items[which]
                }

                .setPositiveButton("저장",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(selectedItem == null){
                            selectedItem = items[0]
                        }
                        voiceChoice.get(selectedItem)
                        Toast.makeText(this, voiceChoice.get(selectedItem), Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this, "취소누름", Toast.LENGTH_SHORT).show()
                })
            builder.show()
        }
    }
}