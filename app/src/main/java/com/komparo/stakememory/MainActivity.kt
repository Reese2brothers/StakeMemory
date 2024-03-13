package com.komparo.stakememory

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.komparo.stakememory.databinding.ActivityMainBinding
import com.komparo.stakememory.fragments.FiveFragment
import com.komparo.stakememory.fragments.FourFragment
import com.komparo.stakememory.fragments.ThreeFragment
import com.komparo.stakememory.model.Datas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    var coroutine : Job? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var open = false
    private var openTable = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        sharedPreferences = getSharedPreferences("KeyManager", Context.MODE_PRIVATE)
        sharedPreferences = getSharedPreferences("SaveName", MODE_PRIVATE)
        val savename = sharedPreferences.getBoolean("savename", false)
        val name = sharedPreferences.getString("name", "default")
        if(savename == true){
            binding.etEnterName.visibility = View.GONE
            binding.btSaveName.visibility = View.GONE
            binding.cvLogo.visibility = View.VISIBLE
        }
        if (name != "default"){
            binding.tvPlayerName.text = name
        } else {
            binding.tvPlayerName.text = ""
        }
        binding.btSaveName.setOnClickListener {
            open = true
            binding.tvPlayerName.text = binding.etEnterName.text.toString()
            val key = getKey()
            Log.d("TAG", "Key: $key")
            Log.d("TAG", "Name: $name")
            binding.etEnterName.visibility = View.GONE
            binding.btSaveName.visibility = View.GONE
            binding.cvLogo.visibility = View.VISIBLE

            sharedPreferences = getSharedPreferences("SaveName", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor?.putBoolean("savename", open)
            editor?.putString("name", binding.tvPlayerName.text as String)
            editor?.putString("key", key)
            editor?.apply()
            hideSystemUI()
            val ims = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            ims.hideSoftInputFromWindow(binding.etEnterName.windowToken, 0)
        }
        Glide.with(this).asGif().load(R.drawable.fingerclick).into(binding.ivFingerClick)
        Glide.with(this).asGif().load(R.drawable.fingerclick).into(binding.ivLevelsClick)
        binding.tvLevels.setOnClickListener {
            binding.cvLevels.visibility = View.VISIBLE
            showLevels()
        }
        binding.tvTableRecords.setOnClickListener {
                val retrofit = RetrofitClient()
                    .getClient("https://stake-memory.store/")
                    .create(API::class.java)
                coroutineScope.launch {
                    retrofit.getTop().enqueue(object : Callback<Datas> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(call: Call<Datas>, response: Response<Datas>) {
                            binding.tvLightOneName.text = response.body()!!.level1.getOrNull(0)?.name ?: "empty"
                            binding.tvLightTwoName.text = response.body()!!.level1.getOrNull(1)?.name ?: "empty"
                            binding.tvLightThreeName.text = response.body()!!.level1.getOrNull(2)?.name ?: "empty"
                            binding.tvLightFourName.text = response.body()!!.level1.getOrNull(3)?.name ?: "empty"
                            binding.tvLightFiveName.text = response.body()!!.level1.getOrNull(4)?.name ?: "empty"

                            binding.tvLightOnePoints.text = response.body()!!.level1.getOrNull(0)?.points ?: "0"
                            binding.tvLightTwoPoints.text = response.body()!!.level1.getOrNull(1)?.points ?: "0"
                            binding.tvLightThreePoints.text = response.body()!!.level1.getOrNull(2)?.points ?: "0"
                            binding.tvLightFourPoints.text = response.body()!!.level1.getOrNull(3)?.points ?: "0"
                            binding.tvLightFivePoints.text = response.body()!!.level1.getOrNull(4)?.points ?: "0"

                        binding.tvMediumOneName.text = response.body()!!.level2.getOrNull(0)?.name ?: "empty"
                        binding.tvMediumTwoName.text = response.body()!!.level2.getOrNull(1)?.name ?: "empty"
                        binding.tvMediumThreeName.text = response.body()!!.level2.getOrNull(2)?.name ?: "empty"
                        binding.tvMediumFourName.text = response.body()!!.level2.getOrNull(3)?.name ?: "empty"
                        binding.tvMediumFiveName.text = response.body()!!.level2.getOrNull(4)?.name ?: "empty"

                        binding.tvMediumOnePoints.text = response.body()!!.level2.getOrNull(0)?.points ?: "0"
                        binding.tvMediumTwoPoints.text = response.body()!!.level2.getOrNull(1)?.points ?: "0"
                        binding.tvMediumThreePoints.text = response.body()!!.level2.getOrNull(2)?.points ?: "0"
                        binding.tvMediumFourPoints.text = response.body()!!.level2.getOrNull(3)?.points ?: "0"
                        binding.tvMediumFivePoints.text = response.body()!!.level2.getOrNull(4)?.points ?: "0"

                        binding.tvStrongOneName.text = response.body()!!.level3.getOrNull(0)?.name ?: "empty"
                        binding.tvStrongTwoName.text = response.body()!!.level3.getOrNull(1)?.name ?: "empty"
                        binding.tvStrongThreeName.text = response.body()!!.level3.getOrNull(2)?.name ?: "empty"
                        binding.tvStrongFourName.text = response.body()!!.level3.getOrNull(3)?.name ?: "empty"
                        binding.tvStrongFiveName.text = response.body()!!.level3.getOrNull(4)?.name ?: "empty"

                        binding.tvStrongOnePoints.text = response.body()!!.level3.getOrNull(0)?.points ?: "0"
                        binding.tvStrongTwoPoints.text = response.body()!!.level3.getOrNull(1)?.points ?: "0"
                        binding.tvStrongThreePoints.text = response.body()!!.level3.getOrNull(2)?.points ?: "0"
                        binding.tvStrongFourPoints.text = response.body()!!.level3.getOrNull(3)?.points ?: "0"
                        binding.tvStrongFivePoints.text = response.body()!!.level3.getOrNull(4)?.points ?: "0"
                        }

                        override fun onFailure(call: Call<Datas>, t: Throwable) {

                        }
                    })
                }
            if (openTable == false){
                binding.cvTableRecords.visibility = View.VISIBLE
                showTable()
                openTable = true
            } else {
                binding.cvTableRecords.visibility = View.GONE
                showTableBack()
                openTable = false
            }
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
            binding.tvSmallLight.visibility = View.VISIBLE
            binding.tvSmallMedium.visibility = View.GONE
            binding.tvSmallStrong.visibility = View.GONE
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
            binding.tvSmallMedium.visibility = View.VISIBLE
            binding.tvSmallLight.visibility = View.GONE
            binding.tvSmallStrong.visibility = View.GONE
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
            binding.tvSmallStrong.visibility = View.VISIBLE
            binding.tvSmallMedium.visibility = View.GONE
            binding.tvSmallLight.visibility = View.GONE
            binding.cvLevels.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
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
    private fun showTable(){
        val animation = ScaleAnimation(1f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.cvTableRecords.startAnimation(animation)
    }
    private fun showTableBack(){
        val animation = ScaleAnimation(1f, 1f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.cvTableRecords.startAnimation(animation)
    }
    private fun hideLevels(){
        val animation = ScaleAnimation(1f, 1f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 700
        binding.cvLevels.startAnimation(animation)
    }
    private fun getKey(): String {
        var key = sharedPreferences.getString("key", null)
        if (key == null) {
            key = Key().generateKey()
            saveKey(key)
        }
        return key
    }
    private fun saveKey(key: String) {
        with(sharedPreferences.edit()) {
            putString("key", key)
            apply()
        }
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