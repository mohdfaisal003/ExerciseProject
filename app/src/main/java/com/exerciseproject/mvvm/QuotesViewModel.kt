package com.exerciseproject.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rooted.deviceinfo.mvvm.pojos.QuotePojo
import com.rooted.deviceinfo.mvvm.repositories.QuotesRepo
import kotlinx.coroutines.launch

class QuotesViewModel : ViewModel() {

    private val quotesRepo = QuotesRepo()
    val quotes = MutableLiveData<QuotePojo>()

    fun getRandomQuote() {
        viewModelScope.launch {
            try {
                val response = quotesRepo.getRandomQuote()
                if (response.isSuccessful) {
                    Log.d("value",response.body()?.get(0).toString())
                    quotes.value = response.body()?.get(0)
                } else if (response.code() >= 400) {

                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}