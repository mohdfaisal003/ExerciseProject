package com.rooted.deviceinfo.web_base

import com.rooted.deviceinfo.mvvm.pojos.QuotePojo
import retrofit2.Response
import retrofit2.http.GET

interface ApiCallBack {

    @GET(WebCons.RANDOM_QUOTE_KEY)
    suspend fun getRandomQuote(): Response<List<QuotePojo>>
}