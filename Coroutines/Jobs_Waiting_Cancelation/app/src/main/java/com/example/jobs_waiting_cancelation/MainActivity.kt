package com.example.jobs_waiting_cancelation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jobs_waiting_cancelation.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Coroutine Jobs, how to wait for them and/or cancel them
        val job = GlobalScope.launch(Dispatchers.Default){
            Log.d(TAG,"Strating long running calculation")
            withTimeout(500L){
                for (i in 30..40){
                    if (isActive){
                        Log.d(TAG,"Result for i = $i: ${fib(i)}")
                    }
                }
            }

            Log.d(TAG,"Ending long running calculation")
        }

        /*runBlocking {
            delay(500L)
            job.cancel()
            //job.join() // block our thread until this coroutine is finished
            Log.d(TAG,"Canceled Job....")
        }*/

        val view = binding.root
        setContentView(view)
    }
    fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib( n - 2)
     }
}