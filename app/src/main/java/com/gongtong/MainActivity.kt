package com.gongtong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        if(prefKey!=""){
            //추후 삭제해야됨
            Toast.makeText(this, prefKey.toString(), Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, SignUpActivity::class.java)
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
                Toast.makeText(applicationContext, "비상전화", Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}