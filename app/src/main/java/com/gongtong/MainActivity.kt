package com.gongtong

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gongtong.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private lateinit var binding: ActivityMainBinding
private var statusProgress: ProgressBar?=null;
var gText: String? = null
val audioPlay = MediaPlayer()
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        val navView: BottomNavigationView = binding.bottomNavView
        val navController = findNavController(R.id.fragment_host)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.commuboardFragment,
                R.id.emergencyFragment,
                R.id.settingFragment,
                R.id.everydaylifeFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var prefKey = MyApplication.prefs.getString("prefkey", "")

        if (prefKey != "") {
            //추후 삭제해야됨
            Toast.makeText(this, prefKey.toString(), Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding!!.speekbutton.setOnClickListener {
            gText = binding!!.editText.text.toString()
            if (gText == "") {
                Toast.makeText(this, "알림: 텍스트를 입력해 주세요.", Toast.LENGTH_LONG).show()
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
            val voice = MyApplication.prefs.getString("voice", "ntaejin")
            val voicespeed = MyApplication.prefs.getString("voicespeed", "0")

            //APIExamTTS.main(args)
            val clientId = "5ueydnnck3"//애플리케이션 클라이언트 아이디값";
            val clientSecret = "BzYfc4S3DTMSjdhbHJHds5843B5ptvWDdonVe2Bi"//애플리케이션 클라이언트 시크릿값";
            try {
                val text = URLEncoder.encode(params[0], "UTF-8")

                val apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts"
                val url = URL(apiURL)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId)
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret)
                // post request
                val postParams =
                    "speaker=$voice&volume=0&speed=$voicespeed&pitch=0&format=mp3&text=$text"
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
                    val f =
                        File(getExternalFilesDir(null).toString() + File.separator + "NaverClova/$tempname")
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
            binding!!.editText.setHint(gText)
            binding!!.editText.setText("")
        }
    }

    fun checkPermission() {
        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val callPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우
            startProcess()

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 99)
    }

    // 권한 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                }
            }
        }
    }

    fun startProcess() {
        Toast.makeText(this, "비상전화", Toast.LENGTH_SHORT).show()
        var intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:0537207900")
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.action_call -> { //검색 버튼 눌렀을 때
                checkPermission()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    //프래그먼트에서 값전달받아서 edittext에 넣기
    /*
    fun receiveData(tmp:String){
        val tmp2 = binding.editText.text.toString()
        binding.editText.setText(tmp2.plus(tmp))
    }
    */


    //프래그먼트 이동 메소드
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_host, fragment)
        fragmentTransaction.addToBackStack(null).commit()
    }
}
