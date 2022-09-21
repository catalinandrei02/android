package com.example.corutines_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.corutines_android.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        GlobalScope.launch(Dispatchers.IO) {
            //delay(3000L)// - can be called only inside of a coroutine or another suspend function
            Log.d(TAG,"Coroutine in thread ${Thread.currentThread().name}")
            val networkCall = doNetworkCall()// - can be called only inside of a coroutine or another suspend function
            withContext(Dispatchers.Main){
                Log.d(TAG,"Coroutine in thread ${Thread.currentThread().name}")
                binding.text.text = networkCall
            }
        }

        val view = binding.root
        setContentView(view)
    }
    suspend fun doNetworkCall(): String {
        delay(3000L)
        return "This is the answer"
    }
}