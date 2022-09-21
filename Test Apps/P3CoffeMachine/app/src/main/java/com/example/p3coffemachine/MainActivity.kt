package com.example.p3coffemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

val resurse = Resurse(400,540,120,9,550)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buyBtn = findViewById<Button>(R.id.button2)
        buyBtn.setOnClickListener{
            val change = Intent(this,BuyCoffe::class.java)
            startActivity(change)
        }
        val fillBtn = findViewById<Button>(R.id.button4)
        fillBtn.setOnClickListener{
            val change = Intent(this,FillResources::class.java)
            startActivity(change)
        }
        val takeBtn = findViewById<Button>(R.id.button5)
        takeBtn.setOnClickListener{
            Toast.makeText(this,"I gave you ${resurse.money} RON",Toast.LENGTH_LONG).show()
            resurse.money = 0
        }
        val checkBtn = findViewById<Button>(R.id.button6)
        checkBtn.setOnClickListener{
            val change = Intent(this,RemainingResources::class.java)
            startActivity(change)
        }
        val exitBtn = findViewById<Button>(R.id.button7)
        exitBtn.setOnClickListener{
            finishAffinity()
        }


    }
}
data class Resurse(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int)
