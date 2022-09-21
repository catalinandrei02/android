package com.example.p3coffemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BuyCoffe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_coffe)
        //Back Button (goes back to MainActivity on click)
        val backBtn = findViewById<Button>(R.id.button7)
        backBtn.setOnClickListener {
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }

        /*The Coffe Button should take the necessary resources from Resurse data class if they are available
       and change buyText to a "Im making coffe" message else it should promt you what resource is missing*/
        val expressoBtn = findViewById<Button>(R.id.button2)
        expressoBtn.setOnClickListener { makecoffe(250,0,16,4) }
        val latteBtn = findViewById<Button>(R.id.button4)
        latteBtn.setOnClickListener{ makecoffe(350,75,20,7) }
        val cappuccinoBtn = findViewById<Button>(R.id.button5)
        cappuccinoBtn.setOnClickListener{ makecoffe(200,100,10,6) }

    }
    //Makes Coffe
    private fun makecoffe(water: Int, milk: Int, beans: Int, money: Int){
        //TextView
        val buyText: TextView = findViewById(R.id.textView)
        if (resurse.water - water>= 0 && resurse.milk - milk >= 0 && resurse.beans - beans >= 0  && resurse.cups >= 1) {
                resurse.water -= water
                resurse.milk -= milk
                resurse.beans -= beans
                resurse.money += money
                resurse.cups--
                buyText.text = getString(R.string.succes)
            } else when {
                resurse.water < water -> buyText.text = getString(R.string.errW)
                resurse.milk < milk -> buyText.text = getString(R.string.errM)
                resurse.beans < beans -> buyText.text = getString(R.string.errB)
                resurse.cups < 1 -> buyText.text = getString(R.string.errC)
            }

    }
}

