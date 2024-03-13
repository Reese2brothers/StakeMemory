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
import com.komparo.stakememory.databinding.FragmentFourBinding
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


class FourFragment : Fragment() {
    private lateinit var binding : FragmentFourBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    private val listBack = listOf(R.drawable.bluepika, R.drawable.bluering,
        R.drawable.violetfiveangle, R.drawable.redromb)
    private var getBackSymbol : Drawable.ConstantState? = null
    private var count = 0
    private var symbolCount = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFourBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("SaveName", AppCompatActivity.MODE_PRIVATE)!!
        val name = sharedPreferences.getString("name", "default")
        val key = sharedPreferences.getString("key", "default")

        binding.btStartGame4.setOnClickListener {
            binding.btStartGame4.visibility = View.GONE
            showSymbol4() }
        binding.ivRR1.setOnClickListener { showRR1() }
        binding.ivRR2.setOnClickListener { showRR2() }
        binding.ivRR3.setOnClickListener { showRR3() }
        binding.ivRR4.setOnClickListener { showRR4() }
        binding.ivRR5.setOnClickListener { showRR5() }
        binding.ivRR6.setOnClickListener { showRR6() }
        binding.ivRR7.setOnClickListener { showRR7() }
        binding.ivRR8.setOnClickListener { showRR8() }
        binding.ivRR9.setOnClickListener { showRR9() }
        binding.ivRR10.setOnClickListener { showRR10() }
        binding.ivRR11.setOnClickListener { showRR11() }
        binding.ivRR12.setOnClickListener { showRR12() }
        binding.btNext4.setOnClickListener {
            binding.ll4x4.visibility = View.GONE
            showSymbol4()
            if (symbolCount != 0) {
                binding.btNext4.isEnabled = false
            }
        }
        binding.btSaveCount4.setOnClickListener {
            val client = OkHttpClient()
            val json = """
    {
        "hash": "$key",
        "data": {
            "name": "$name",
            "medium": "$count"
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
    private fun showSymbol4(){
        coroutine?.cancel()
        binding.btNext4.isEnabled = false
        symbolCount = 0
        binding.tvSymbolCount4.text = "Need open :  $symbolCount"
        val viewSymbols = listOf(binding.ivSS1, binding.ivSS2, binding.ivSS3, binding.ivSS4, binding.ivSS5, binding.ivSS6,
            binding.ivSS7, binding.ivSS8, binding.ivSS9, binding.ivSS10, binding.ivSS11, binding.ivSS12)
        viewSymbols.forEach { imageView ->
            imageView.visibility = View.VISIBLE
        }
        val viewRubahas = listOf(binding.ivRR1, binding.ivRR2, binding.ivRR3, binding.ivRR4, binding.ivRR5, binding.ivRR6,
            binding.ivRR7, binding.ivRR8, binding.ivRR9, binding.ivRR10, binding.ivRR11, binding.ivRR12)
        viewRubahas.forEach { imageView ->
            imageView.visibility = View.GONE
        }
        coroutine = coroutineScope.launch {
            delay(500)
            binding.ivFront4.visibility = View.VISIBLE
            binding.ivFront4.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_up))
            delay(1000)
            binding.ivBack4.setImageResource(listBack.random())
            getBackSymbol = binding.ivBack4.drawable.constantState
            FlipCardAnimation(requireActivity(), binding.ivFront4, binding.ivBack4).flip()
            delay(1500)
            binding.ivBack4.visibility = View.GONE
            FlipCardAnimation(requireActivity(), binding.ivBack4, binding.ivFront4).flip()
            delay(1000)
            binding.ivFront4.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_down))
            binding.ivFront4.visibility = View.GONE
            binding.ll4x4.visibility = View.VISIBLE
            binding.tvPointsText4.visibility = View.VISIBLE
            binding.btNext4.visibility = View.VISIBLE
            binding.btSaveCount4.visibility = View.VISIBLE
            binding.tvSymbolCount4.visibility = View.VISIBLE
            viewSymbols.forEach { imageView ->
                imageView.setImageResource(listBack.random())
                if (imageView.drawable.constantState == getBackSymbol) {
                    symbolCount++
                    binding.tvSymbolCount4.text = "Need open :  $symbolCount"
                    binding.btNext4.isEnabled = false
                }
                if(symbolCount == 0){
                    binding.btNext4.isEnabled = true
                    binding.tvSymbolCount4.text = "Need open :  $symbolCount"
                }
            }
            delay(1000)
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
    private fun showRR1(){
        FlipCardAnimation(requireActivity(), binding.ivRR1, binding.ivSS1).flip()
        binding.ivRR1.visibility = View.GONE
        if(getBackSymbol == binding.ivSS1.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR2(){
        FlipCardAnimation(requireActivity(), binding.ivRR2, binding.ivSS2).flip()
        binding.ivRR2.visibility = View.GONE
        if (getBackSymbol == binding.ivSS2.drawable.constantState) {
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR3(){
        FlipCardAnimation(requireActivity(), binding.ivRR3, binding.ivSS3).flip()
        binding.ivRR3.visibility = View.GONE
        if(getBackSymbol == binding.ivSS3.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR4(){
        FlipCardAnimation(requireActivity(), binding.ivRR4, binding.ivSS4).flip()
        binding.ivRR4.visibility = View.GONE
        if(getBackSymbol == binding.ivSS4.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR5(){
        FlipCardAnimation(requireActivity(), binding.ivRR5, binding.ivSS5).flip()
        binding.ivRR5.visibility = View.GONE
        if(getBackSymbol == binding.ivSS5.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR6(){
        FlipCardAnimation(requireActivity(), binding.ivRR6, binding.ivSS6).flip()
        binding.ivRR6.visibility = View.GONE
        if(getBackSymbol == binding.ivSS6.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR7(){
        FlipCardAnimation(requireActivity(), binding.ivRR7, binding.ivSS7).flip()
        binding.ivRR7.visibility = View.GONE
        if(getBackSymbol == binding.ivSS7.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR8(){
        FlipCardAnimation(requireActivity(), binding.ivRR8, binding.ivSS8).flip()
        binding.ivRR8.visibility = View.GONE
        if(getBackSymbol == binding.ivSS8.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR9(){
        FlipCardAnimation(requireActivity(), binding.ivRR9, binding.ivSS9).flip()
        binding.ivRR9.visibility = View.GONE
        if(getBackSymbol == binding.ivSS9.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR10(){
        FlipCardAnimation(requireActivity(), binding.ivRR10, binding.ivSS10).flip()
        binding.ivRR10.visibility = View.GONE
        if(getBackSymbol == binding.ivSS10.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR11(){
        FlipCardAnimation(requireActivity(), binding.ivRR11, binding.ivSS11).flip()
        binding.ivRR11.visibility = View.GONE
        if(getBackSymbol == binding.ivSS11.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRR12(){
        FlipCardAnimation(requireActivity(), binding.ivRR12, binding.ivSS12).flip()
        binding.ivRR12.visibility = View.GONE
        if(getBackSymbol == binding.ivSS12.drawable.constantState){
            count++
            binding.tvPointsText4.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount4.text = "Need open :  $symbolCount"
            binding.btNext4.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText4.text = "Points :  $count"
            binding.ll4x4.visibility = View.GONE
            binding.tvPointsText4.visibility = View.GONE
            binding.btNext4.visibility = View.GONE
            binding.btSaveCount4.visibility = View.GONE
            binding.tvSymbolCount4.visibility = View.GONE
            binding.btStartGame4.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext4.isEnabled = true
        }
    }

}