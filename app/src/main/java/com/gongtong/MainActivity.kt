package com.gongtong

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gongtong.databinding.ActivityMainBinding
import com.gongtong.emergency.EmergencyFragment
import com.gongtong.home.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private var firebaseFirestore: FirebaseFirestore? = null
private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore //파이어베이스.파이어스토어 설정
    val MY_PERMISSION_ACCESS_ALL = 100

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
                R.id.settingFragment

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

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_host, fragment)
        fragmentTransaction.addToBackStack(null).commit()

    }
}
