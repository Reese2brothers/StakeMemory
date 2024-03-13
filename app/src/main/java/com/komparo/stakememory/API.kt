package com.komparo.stakememory

import com.komparo.stakememory.model.Datas
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface API {
    @GET("apiV4-0xkey4ddjdf445egsgsas2/sorted.php")
    fun getTop() : Call<Datas>

    companion object {

        var BASE_URL = "https://stake-memory.store/"

        fun create() : API {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(API::class.java)

        }
    }

}