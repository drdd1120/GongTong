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
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusProgress = this.binding.progressBar1

        binding.button.setOnClickListener {
            gText = binding.editText.text.toString()
            if (gText == ""){
                Toast.makeText(this, "알림: 텍스트를 입력해 주세요.",Toast.LENGTH_LONG).show()
            }
            //AsyncTask를 수행한다.
            AsyncTaskExample().execute(gText)
        }

        binding.button2.setOnClickListener {
            val intent = Intent(this, VoiceSettingActivity::class.java)
            startActivity(intent)
        }
    }

    //AsyncTask
    inner class AsyncTaskExample : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            statusProgress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params : String?): String {

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
                val postParams = "speaker=ntaejin&volume=0&speed=0&pitch=0&format=mp3&text=$text"
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
                    val dir = File(getExternalFilesDir(null), "NaverClova")
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }

                    val tempname = File("naverclova.mp3")
                    val f = File(getExternalFilesDir(null).toString() + File.separator + "NaverClova/$tempname")
                    f.createNewFile()
                    val outputStream = FileOutputStream(f)
                    while (iss.read(bytes).also { read = it } != -1) {
                        outputStream.write(bytes, 0, read)
                    }
                    iss.close()

                    val filename = "${getExternalFilesDir(null)}/NaverClova/$tempname"
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
            binding.editText.setHint(gText)
            binding.editText.setText("")
        }
    }
}