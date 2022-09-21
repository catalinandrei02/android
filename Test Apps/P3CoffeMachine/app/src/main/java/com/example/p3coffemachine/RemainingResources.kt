package com.example.p3coffemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RemainingResources : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remaining_resources)

        //Back Button goes back to MainActivity
        val backBtn = findViewById<Button>(R.id.button8)
        backBtn.setOnClickListener {
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }
        //Displays the values of the Resurse data class
        val remainingWater: TextView = findViewById(R.id.textView4)
        remainingWater.text = getString(R.string.resw,resurse.water)
        val remainingMilk: TextView = findViewById(R.id.textView5)
        remainingMilk.text = getString(R.string.resm,resurse.milk)
        val remainingBeans: TextView = findViewById(R.id.textView6)
        remainingBeans.text = getString(R.string.resb,resurse.beans)
        val remainingCups: TextView = findViewById(R.id.textView7)
        remainingCups.text = getString(R.string.resc,resurse.cups)
        val remainingMoney: TextView = findViewById(R.id.textView8)
        remainingMoney.text = getString(R.string.resd,resurse.money)
    }

}