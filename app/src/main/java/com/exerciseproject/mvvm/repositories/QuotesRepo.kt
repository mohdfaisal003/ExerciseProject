package com.rooted.deviceinfo.mvvm.repositories

import com.rooted.deviceinfo.mvvm.pojos.QuotePojo
import com.rooted.deviceinfo.web_base.WebConnector
import retrofit2.Response

class QuotesRepo: WebConnector() {

    suspend fun getRandomQuote(): Response<List<QuotePojo>> {
        return apiCallBack.getRandomQuote()
    }
}