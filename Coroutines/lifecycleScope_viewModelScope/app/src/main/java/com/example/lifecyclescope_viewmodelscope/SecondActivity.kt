package com.example.lifecyclescope_viewmodelscope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecyclescope_viewmodelscope.databinding.ActivityMainBinding
import com.example.lifecyclescope_viewmodelscope.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        binding = ActivitySecondBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
    }
}