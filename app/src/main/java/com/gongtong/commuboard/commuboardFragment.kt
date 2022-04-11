package com.gongtong.commuboard

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.gongtong.MainActivity
import com.gongtong.MyApplication
import com.gongtong.R
import com.gongtong.databinding.ActivityHomeBinding
import com.gongtong.databinding.FragmentCommuboardBinding
import com.gongtong.databinding.FragmentSettingBinding
import com.gongtong.home.HomeActivity
import com.gongtong.settings.VoiceSettingActivity
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


var Tag: String = "HomeActivity"
val audioPlay = MediaPlayer()
private var statusProgress: ProgressBar?=null;
var gText: String? = null


class commuboardFragment : Fragment() {

    private var mainActivity: MainActivity? = null
    private var binding: FragmentCommuboardBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mBinding = FragmentCommuboardBinding.inflate(inflater, container, false)
        binding = mBinding
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        statusProgress = this.binding!!.progressBar1
        binding!!.button.setOnClickListener {
            gText = binding!!.editText.text.toString()
            if (gText == "") {
                Toast.makeText(context, "알림: 텍스트를 입력해 주세요.", Toast.LENGTH_LONG).show()
            }
            //AsyncTask를 수행한다.
            AsyncTaskExample().execute(gText)
        }
    }

    inner class AsyncTaskExample : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            statusProgress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {

            var voice = MyApplication.prefs.getString("voice", "ntaejin")
            var voicespeed = MyApplication.prefs.getString("voicespeed", "0")

            //APIExamTTS.main(args)
            val clientId = "m2b9osigjy"//애플리케이션 클라이언트 아이디값";
            val clientSecret = "l6GSek9Y6xMOU3rEhncHM91nPLN0xv0GmCtWtgEq"//애플리케이션 클라이언트 시크릿값";
            try {
                val text = URLEncoder.encode(params[0], "UTF-8")

                val apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts"
                val url = URL(apiURL)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId)
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret)
                // post request
                val postParams = "speaker="+voice+"&volume=0&speed="+voicespeed+"&pitch=0&format=mp3&text=$text"
                con.doOutput = true
                val wr = DataOutputStream(con.outputStream)
                wr.writeBytes(postParams)
                wr.flush()
                wr.close()
                val responseCode = con.responseCode
                val br: BufferedReader
                if (responseCode == 200) { // 정상 호출
                    val iss = con.inputStream
                    var read = 0
                    val bytes = ByteArray(1024)
                    // 랜덤한 이름으로 mp3 파일 생성
                    val dir = File(activity?.getExternalFilesDir(null), "NaverClova")
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }

                    val tempname = File("naverclova.mp3")
                    val f =
                        File(activity?.getExternalFilesDir(null).toString() + File.separator + "NaverClova/$tempname")
                    f.createNewFile()
                    val outputStream = FileOutputStream(f)
                    while (iss.read(bytes).also { read = it } != -1) {
                        outputStream.write(bytes, 0, read)
                    }
                    iss.close()

                    val filename = "${activity?.getExternalFilesDir(null)}/NaverClova/$tempname"
                    audioPlay.reset()
                    audioPlay.setDataSource(filename)
                    audioPlay.prepare()
                    audioPlay.start()


                } else {  // 오류 발생
                    br = BufferedReader(InputStreamReader(con.errorStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (br.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    br.close()
                    println(response.toString())
                }
            } catch (e: Exception) {
                println(e)
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            statusProgress?.visibility = View.GONE
            //
            binding!!.editText.setHint(gText)
            binding!!.editText.setText("")
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}