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
import com.komparo.stakememory.databinding.FragmentFiveBinding
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


class FiveFragment : Fragment() {
    private lateinit var binding : FragmentFiveBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    private val listBack = listOf(R.drawable.greeneightangle, R.drawable.greenromb,
        R.drawable.redromb, R.drawable.violetfiveangle, R.drawable.yellowtriangle)
    private var getBackSymbol : Drawable.ConstantState? = null
    private var count = 0
    private var symbolCount = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiveBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("SaveName", AppCompatActivity.MODE_PRIVATE)!!
        val name = sharedPreferences.getString("name", "default")
        val key = sharedPreferences.getString("key", "default")

        binding.btStartGame5.setOnClickListener {
            binding.btStartGame5.visibility = View.GONE
            showSymbol5() }
        binding.ivRRR1.setOnClickListener { showRRR1() }
        binding.ivRRR2.setOnClickListener { showRRR2() }
        binding.ivRRR3.setOnClickListener { showRRR3() }
        binding.ivRRR4.setOnClickListener { showRRR4() }
        binding.ivRRR5.setOnClickListener { showRRR5() }
        binding.ivRRR6.setOnClickListener { showRRR6() }
        binding.ivRRR7.setOnClickListener { showRRR7() }
        binding.ivRRR8.setOnClickListener { showRRR8() }
        binding.ivRRR9.setOnClickListener { showRRR9() }
        binding.ivRRR10.setOnClickListener { showRRR10() }
        binding.ivRRR11.setOnClickListener { showRRR11() }
        binding.ivRRR12.setOnClickListener { showRRR12() }
        binding.ivRRR13.setOnClickListener { showRRR13() }
        binding.ivRRR14.setOnClickListener { showRRR14() }
        binding.ivRRR15.setOnClickListener { showRRR15() }
        binding.btNext5.setOnClickListener {
            binding.ll5x5.visibility = View.GONE
            showSymbol5()
            if (symbolCount != 0) {
                binding.btNext5.isEnabled = false
            }
        }
        binding.btSaveCount5.setOnClickListener {
            val client = OkHttpClient()
            val json = """
    {
        "hash": "$key",
        "data": {
            "name": "$name",
            "strong": "$count"
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
    private fun showSymbol5(){
        coroutine?.cancel()
        binding.btNext5.isEnabled = false
        symbolCount = 0
        binding.tvSymbolCount5.text = "Need open :  $symbolCount"
        val viewSymbols = listOf(binding.ivSSS1, binding.ivSSS2, binding.ivSSS3, binding.ivSSS4, binding.ivSSS5, binding.ivSSS6,
            binding.ivSSS7, binding.ivSSS8, binding.ivSSS9, binding.ivSSS10, binding.ivSSS11, binding.ivSSS12, binding.ivSSS13, binding.ivSSS14, binding.ivSSS15)
        viewSymbols.forEach { imageView ->
            imageView.visibility = View.VISIBLE
        }
        val viewRubahas = listOf(binding.ivRRR1, binding.ivRRR2, binding.ivRRR3, binding.ivRRR4, binding.ivRRR5, binding.ivRRR6,
            binding.ivRRR7, binding.ivRRR8, binding.ivRRR9, binding.ivRRR10, binding.ivRRR11, binding.ivRRR12, binding.ivRRR13, binding.ivRRR14, binding.ivRRR15)
        viewRubahas.forEach { imageView ->
            imageView.visibility = View.GONE
        }
        coroutine = coroutineScope.launch {
            delay(500)
            binding.ivFront5.visibility = View.VISIBLE
            binding.ivFront5.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_up))
            delay(1000)
            binding.ivBack5.setImageResource(listBack.random())
            getBackSymbol = binding.ivBack5.drawable.constantState
            FlipCardAnimation(requireActivity(), binding.ivFront5, binding.ivBack5).flip()
            delay(1500)
            binding.ivBack5.visibility = View.GONE
            FlipCardAnimation(requireActivity(), binding.ivBack5, binding.ivFront5).flip()
            delay(1000)
            binding.ivFront5.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_down))
            binding.ivFront5.visibility = View.GONE
            binding.ll5x5.visibility = View.VISIBLE
            binding.tvPointsText5.visibility = View.VISIBLE
            binding.btNext5.visibility = View.VISIBLE
            binding.btSaveCount5.visibility = View.VISIBLE
            binding.tvSymbolCount5.visibility = View.VISIBLE
            viewSymbols.forEach { imageView ->
                imageView.setImageResource(listBack.random())
                if (imageView.drawable.constantState == getBackSymbol) {
                    symbolCount++
                    binding.tvSymbolCount5.text = "Need open :  $symbolCount"
                    binding.btNext5.isEnabled = false
                }
                if(symbolCount == 0){
                    binding.btNext5.isEnabled = true
                    binding.tvSymbolCount5.text = "Need open :  $symbolCount"
                }
            }
            delay(1200)
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
    private fun showRRR1(){
        FlipCardAnimation(requireActivity(), binding.ivRRR1, binding.ivSSS1).flip()
        binding.ivRRR1.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS1.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR2(){
        FlipCardAnimation(requireActivity(), binding.ivRRR2, binding.ivSSS2).flip()
        binding.ivRRR2.visibility = View.GONE
        if (getBackSymbol == binding.ivSSS2.drawable.constantState) {
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR3(){
        FlipCardAnimation(requireActivity(), binding.ivRRR3, binding.ivSSS3).flip()
        binding.ivRRR3.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS3.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR4(){
        FlipCardAnimation(requireActivity(), binding.ivRRR4, binding.ivSSS4).flip()
        binding.ivRRR4.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS4.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR5(){
        FlipCardAnimation(requireActivity(), binding.ivRRR5, binding.ivSSS5).flip()
        binding.ivRRR5.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS5.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR6(){
        FlipCardAnimation(requireActivity(), binding.ivRRR6, binding.ivSSS6).flip()
        binding.ivRRR6.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS6.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR7(){
        FlipCardAnimation(requireActivity(), binding.ivRRR7, binding.ivSSS7).flip()
        binding.ivRRR7.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS7.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR8(){
        FlipCardAnimation(requireActivity(), binding.ivRRR8, binding.ivSSS8).flip()
        binding.ivRRR8.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS8.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR9(){
        FlipCardAnimation(requireActivity(), binding.ivRRR9, binding.ivSSS9).flip()
        binding.ivRRR9.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS9.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR10(){
        FlipCardAnimation(requireActivity(), binding.ivRRR10, binding.ivSSS10).flip()
        binding.ivRRR10.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS10.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR11(){
        FlipCardAnimation(requireActivity(), binding.ivRRR11, binding.ivSSS11).flip()
        binding.ivRRR11.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS11.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR12(){
        FlipCardAnimation(requireActivity(), binding.ivRRR12, binding.ivSSS12).flip()
        binding.ivRRR12.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS12.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR13(){
        FlipCardAnimation(requireActivity(), binding.ivRRR13, binding.ivSSS13).flip()
        binding.ivRRR13.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS13.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR14(){
        FlipCardAnimation(requireActivity(), binding.ivRRR14, binding.ivSSS14).flip()
        binding.ivRRR14.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS14.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showRRR15(){
        FlipCardAnimation(requireActivity(), binding.ivRRR15, binding.ivSSS15).flip()
        binding.ivRRR15.visibility = View.GONE
        if(getBackSymbol == binding.ivSSS15.drawable.constantState){
            count++
            binding.tvPointsText5.text = "Points :  $count"
            symbolCount--
            binding.tvSymbolCount5.text = "Need open :  $symbolCount"
            binding.btNext5.isEnabled = false
        } else {
            count = 0
            binding.tvPointsText5.text = "Points :  $count"
            binding.ll5x5.visibility = View.GONE
            binding.tvPointsText5.visibility = View.GONE
            binding.btNext5.visibility = View.GONE
            binding.btSaveCount5.visibility = View.GONE
            binding.tvSymbolCount5.visibility = View.GONE
            binding.btStartGame5.visibility = View.VISIBLE
        }
        if (symbolCount == 0) {
            binding.btNext5.isEnabled = true
        }
    }
}