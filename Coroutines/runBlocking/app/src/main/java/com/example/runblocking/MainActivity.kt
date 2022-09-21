package com.example.runblocking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.runblocking.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainAcivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)


        Log.d(TAG,"Before runBlocking")
        runBlocking {
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG,"Finished IO Corutine 1")
            }
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG,"Finished IO Corutine 2")
            }

            Log.d(TAG,"Start of runBlocking")

            delay(5000L)
            Log.d(TAG,"End of runBlocking")
        }
        Log.d(TAG,"After runBlocking")


        val view = binding.root
        setContentView(view)
    }
}