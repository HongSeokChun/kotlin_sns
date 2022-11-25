package com.android.example.hongseokchun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.android.example.hongseokchun.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setBottomNav()
    }

    private fun setBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        // Toolbar에 표시되는 제목의 표시 유무를 설정. false로 해야 custom한 툴바의 이름이 화면에 보인다.
        ab.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼
        ab.setDisplayHomeAsUpEnabled(true)
        //왼쪽 버튼 아이콘 변경
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
    }


    fun setNavShow(tag: String){
            when (tag) {
                "view" -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNav.visibility = View.VISIBLE
                }
                "none2" -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                }
                "none3" -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.toolbar.visibility=View.VISIBLE
                    binding.bottomNav.visibility = View.GONE
                    binding.tvToolbarName.text = tag
                }
            }
        }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

}