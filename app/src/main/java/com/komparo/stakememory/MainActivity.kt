package com.komparo.stakememory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.komparo.stakememory.databinding.ActivityMainBinding
import com.komparo.stakememory.fragments.FiveFragment
import com.komparo.stakememory.fragments.FourFragment
import com.komparo.stakememory.fragments.ThreeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    var coroutine : Job? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        Glide.with(this).asGif().load(R.drawable.fingerclick).into(binding.ivFingerClick)
        Glide.with(this).asGif().load(R.drawable.fingerclick).into(binding.ivLevelsClick)
        binding.tvLevels.setOnClickListener {
           binding.cvLevels.visibility = View.VISIBLE
           showLevels()
           }
        binding.tvLight.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flMain, ThreeFragment())
                .commit()
            hideLevels()
           showFrame()
            binding.cvLogo.visibility = View.GONE
            binding.flMain.visibility = View.VISIBLE
            binding.cvLevels.visibility = View.GONE
        }
        binding.tvMedium.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flMain, FourFragment())
                .commit()
            hideLevels()
            showFrame()
            binding.cvLogo.visibility = View.GONE
            binding.flMain.visibility = View.VISIBLE
            binding.cvLevels.visibility = View.GONE
        }
        binding.tvStrong.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flMain, FiveFragment())
                .commit()
            hideLevels()
            showFrame()
            binding.cvLogo.visibility = View.GONE
            binding.flMain.visibility = View.VISIBLE
            binding.cvLevels.visibility = View.GONE
        }
    }
    private fun showFrame(){
        val animation = ScaleAnimation(1f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.flMain.startAnimation(animation)
    }
    private fun showLevels(){
        val animation = ScaleAnimation(1f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.cvLevels.startAnimation(animation)
    }
    private fun hideLevels(){
        val animation = ScaleAnimation(1f, 1f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.cvLevels.startAnimation(animation)
    }
    private fun hideSystemUI () {
        val window : Window? = window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        if (window != null) {
            WindowInsetsControllerCompat (window, window.decorView).let { controller ->
                controller.hide (WindowInsetsCompat.Type.systemBars ())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}