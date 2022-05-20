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
import androidx.navigation.ui.setupWithNavController
import com.gongtong.databinding.ActivityMainBinding
import com.gongtong.preference.MyApplication
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private lateinit var binding: ActivityMainBinding
private var statusProgress: ProgressBar?=null;
var gText: String? = null
val audioPlay = MediaPlayer()
var backKeyPressedTime : Long = 0
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //툴바 이름 삭제
        supportActionBar?.setDisplayShowTitleEnabled(false);
        //네비게이션메뉴 적용
        initNavigation()
        //sharedPreference 값확인, 없으면 회원가입으로, 있으면 메인으로

        var prefKey = MyApplication.prefs.getString("prefkey", "")
        if (prefKey != "") {
            //토스트 메세지 출력 테스트
            //Toast.makeText(this, prefKey.toString(), Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 글자 한번에 지우기
        binding!!.deleteTextButton.setOnClickListener{
            binding!!.editText.setText(null)
        }

        //스피커버튼 네이버클로바 음성출력
        binding!!.speekbutton.setOnClickListener {
            gText = binding!!.editText.text.toString()
            if (gText == "") {
                Toast.makeText(this, "알림: 텍스트를 입력해 주세요.", Toast.LENGTH_LONG).show()
            }
            //AsyncTask를 수행한다.
            AsyncTaskExample().execute(gText)
        }
    }

    //안드로이드 제트팩 네비게이션 구현
    private fun initNavigation() {
        val navController = findNavController(R.id.fragment_host)
        binding.bottomNavView.setupWithNavController(navController)
    }

    inner class AsyncTaskExample : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            statusProgress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            val voice = MyApplication.prefs.getString("voice", "ntaejin")
            var tmp = MyApplication.prefs.getString("voicespeed", "0").toInt()*-1
            var voicespeed = tmp.toString()

            //APIExamTTS.main(args)
            val clientId = "g98fbfgxwm"//애플리케이션 클라이언트 아이디값";
            val clientSecret = "xAyNzw1tRJMrPoZcw6DEbNtBKS4zPgsxxptqAXJn"//애플리케이션 클라이언트 시크릿값";
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
            binding!!.editText.setText(gText)
            //커서 항상 오른쪽으로
            binding.editText.setSelection(binding.editText.length())
        }
    }

    //전화 권한 체크
    private fun checkPermission() {
        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val callPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission == PackageManager.PERMISSION_GRANTED) {
            // 전화 권한이 승인된 상태일 경우
            startProcess()
        } else {
            // 전화 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 99)
    }

    // 3. 권한 처리
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

    //비상전화버튼 실행
    private fun startProcess() {
        Toast.makeText(this, "비상전화", Toast.LENGTH_SHORT).show()
        var intent = Intent(Intent.ACTION_DIAL)
        var phonenumber = MyApplication.prefs.getString("prefphonenumber","")
        intent.data = Uri.parse("tel:".plus(phonenumber))
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    //옵션메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    //전화 버튼 눌렀을때 메소드 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.action_call -> {
                checkPermission()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //프래그먼트에서 값전달받아서 edittext에 넣기
    fun receiveData(tmp:String){
        val tmp2 = binding.editText.text.toString()
        binding.editText.setText(tmp2.plus(" ").plus(tmp))
        //커서 항상 오른쪽으로
        binding.editText.setSelection(binding.editText.length())
    }

    //네이게이션바 숨김 메소드
    fun HideBottomNavi(state: Boolean){
        if(state) binding.bottomNavView.visibility = View.GONE else binding.bottomNavView.visibility = View.VISIBLE
    }

    //프래그먼트 이동 메소드, 번들에 값 저장하여 프래그먼트<->프래그먼트 값전달
    fun replaceFragment(fragment: Fragment, result: Int) {
        var bundle = Bundle()
        bundle.putInt("result",result)
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_host, fragment)
        fragmentTransaction.addToBackStack(null).commit()
    }

    //백스택에 값이 없을 경우 뒤로가기 2번으로 앱종료
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount==0){
            if(System.currentTimeMillis() - backKeyPressedTime >=2000 ) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
                finish() //액티비티 종료
            }
        }
        else{
            super.onBackPressed()
        }
    }
}


