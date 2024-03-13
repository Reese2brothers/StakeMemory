package com.komparo.stakememory.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.komparo.stakememory.FlipCardAnimation
import com.komparo.stakememory.R
import com.komparo.stakememory.databinding.FragmentThreeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ThreeFragment : Fragment() {
    private lateinit var binding : FragmentThreeBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    private val listBack = listOf(R.drawable.bluering, R.drawable.greeneightangle,
        R.drawable.greenromb)
    private var getBackSymbol : Drawable.ConstantState? = null
    private var count = 0
    private var symbolCount = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("SaveName", AppCompatActivity.MODE_PRIVATE)!!
        val name = sharedPreferences.getString("name", "default")
        val key = sharedPreferences.getString("key", "default")

        binding.btStartGame.setOnClickListener {
            binding.btStartGame.visibility = View.GONE
            showSymbol() }
        binding.ivR1.setOnClickListener { showR1() }
        binding.ivR2.setOnClickListener { showR2() }
        binding.ivR3.setOnClickListener { showR3() }
        binding.ivR4.setOnClickListener { showR4() }
        binding.ivR5.setOnClickListener { showR5() }
        binding.ivR6.setOnClickListener { showR6() }
        binding.ivR7.setOnClickListener { showR7() }
        binding.ivR8.setOnClickListener { showR8() }
        binding.ivR9.setOnClickListener { showR9() }
        binding.btNext.setOnClickListener {
            binding.ll3x3.visibility = View.GONE
            showSymbol()
            if (symbolCount != 0) {
                binding.btNext.isEnabled = false
            }
        }
        binding.btSaveCount.setOnClickListener {
            val client = OkHttpClient()
            val json = """
    {
        "hash": "$key",
        "data": {
            "name": "$name",
            "light": "$count"
        }
    }
        """.trimIndent()

            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("https://stake-memory.store/apiV4-0xkey4ddjdf445egsgsas2/")
                .post(body)
                .build()
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    withContext(Dispatchers.Main) {
                        println(response.body?.string())
                        Toast.makeText(requireActivity(), "Saved $count points", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        println(e.message)
                    }
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showSymbol(){
        coroutine?.cancel()
        binding.btNext.isEnabled = false
        symbolCount = 0
        binding.tvSymbolCount.text = "Need open :  $symbolCount"
        val viewSymbols = listOf(binding.ivS1, binding.ivS2, binding.ivS3, binding.ivS4, binding.ivS5, binding.ivS6,
            binding.ivS7, binding.ivS8, binding.ivS9)
        viewSymbols.forEach { imageView ->
            imageView.visibility = View.VISIBLE
        }
        val viewRubahas = listOf(binding.ivR1, binding.ivR2, binding.ivR3, binding.ivR4, binding.ivR5, binding.ivR6,
            binding.ivR7, binding.ivR8, binding.ivR9)
        viewRubahas.forEach { imageView ->
            imageView.visibility = View.GONE
        }
        coroutine = coroutineScope.launch {
            delay(500)
            binding.ivFront.visibility = View.VISIBLE
            binding.ivFront.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_up))
            delay(1000)
            binding.ivBack.setImageResource(listBack.random())
            getBackSymbol = binding.ivBack.drawable.constantState
            FlipCardAnimation(requireActivity(), binding.ivFront, binding.ivBack).flip()
            delay(1500)
            binding.ivBack.visibility = View.GONE
            FlipCardAnimation(requireActivity(), binding.ivBack, binding.ivFront).flip()
            delay(1000)
            binding.ivFront.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_down))
            binding.ivFront.visibility = View.GONE
            binding.ll3x3.visibility = View.VISIBLE
            binding.tvPointsText.visibility = View.VISIBLE
            binding.btNext.visibility = View.VISIBLE
            binding.btSaveCount.visibility = View.VISIBLE
            binding.tvSymbolCount.visibility = View.VISIBLE
            viewSymbols.forEach { imageView ->
                imageView.setImageResource(listBack.random())
                if (imageView.drawable.constantState == getBackSymbol) {
                    symbolCount++
                    binding.tvSymbolCount.text = "Need open :  $symbolCount"
                    binding.btNext.isEnabled = false
                }
               if(symbolCount == 0){
                   binding.btNext.isEnabled = true
                   binding.tvSymbolCount.text = "Need open :  $symbolCount"
                }
            }
            delay(800)
            viewSymbols.forEach { imageView ->
                imageView.visibility = View.GONE
            }
            viewRubahas.forEach { imageView ->
                imageView.visibility = View.VISIBLE
            }
            coroutine?.cancel()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR1(){
        FlipCardAnimation(requireActivity(), binding.ivR1, binding.ivS1).flip()
        binding.ivR1.visibility = View.GONE
        if(getBackSymbol == binding.ivS1.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
            binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR2(){
        FlipCardAnimation(requireActivity(), binding.ivR2, binding.ivS2).flip()
        binding.ivR2.visibility = View.GONE
        if (getBackSymbol == binding.ivS2.drawable.constantState) {
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR3(){
        FlipCardAnimation(requireActivity(), binding.ivR3, binding.ivS3).flip()
        binding.ivR3.visibility = View.GONE
        if(getBackSymbol == binding.ivS3.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR4(){
        FlipCardAnimation(requireActivity(), binding.ivR4, binding.ivS4).flip()
        binding.ivR4.visibility = View.GONE
        if(getBackSymbol == binding.ivS4.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR5(){
        FlipCardAnimation(requireActivity(), binding.ivR5, binding.ivS5).flip()
        binding.ivR5.visibility = View.GONE
        if(getBackSymbol == binding.ivS5.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR6(){
        FlipCardAnimation(requireActivity(), binding.ivR6, binding.ivS6).flip()
        binding.ivR6.visibility = View.GONE
        if(getBackSymbol == binding.ivS6.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR7(){
        FlipCardAnimation(requireActivity(), binding.ivR7, binding.ivS7).flip()
        binding.ivR7.visibility = View.GONE
        if(getBackSymbol == binding.ivS7.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR8(){
        FlipCardAnimation(requireActivity(), binding.ivR8, binding.ivS8).flip()
        binding.ivR8.visibility = View.GONE
        if(getBackSymbol == binding.ivS8.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showR9(){
        FlipCardAnimation(requireActivity(), binding.ivR9, binding.ivS9).flip()
        binding.ivR9.visibility = View.GONE
        if(getBackSymbol == binding.ivS9.drawable.constantState){
            count++
            binding.tvPointsText.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount.text = "Need open :  $symbolCount"
            binding.btNext.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText.text = "Points :  $count"
            binding.ll3x3.visibility = View.GONE
            binding.tvPointsText.visibility = View.GONE
            binding.btNext.visibility = View.GONE
            binding.btSaveCount.visibility = View.GONE
              binding.tvSymbolCount.visibility = View.GONE
            binding.btStartGame.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext.isEnabled = true
        }
    }
}