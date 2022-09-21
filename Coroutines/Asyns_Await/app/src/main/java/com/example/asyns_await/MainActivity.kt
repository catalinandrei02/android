package com.example.asyns_await

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val TAG = "Async-Await"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val raspuns1 = async {networkCall1()}
                val raspuns2 = async { networkCall2() }
                Log.d(TAG,"Answer 1 is ${raspuns1.await()}")
                Log.d(TAG,"Answer 2 is ${raspuns2.await()}")
            }
        Log.d(TAG,"Requests took $time ms.")
        }
    }

    suspend fun networkCall1(): String {
        delay(3000L)
        return "raspuns 1"
    }
    suspend fun networkCall2(): String {
        delay(3000L)
        return "raspuns 2"
    }
}