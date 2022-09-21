package com.example.p3coffemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FillResources : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_resources)

        //getting user input
        val resWater : EditText = findViewById(R.id.editTextNumber2)
        val resMilk : EditText = findViewById(R.id.editTextNumber3)
        val resBeans : EditText = findViewById(R.id.editTextNumber4)
        val resCups : EditText = findViewById(R.id.editTextNumber5)
        //add button
        val addBtn = findViewById<Button>(R.id.button)
        addBtn.setOnClickListener {
                //Checks if the user filled all the mandatory data.
            if (resWater.text.toString().isNotEmpty() &&
                resMilk.text.toString().isNotEmpty() &&
                resBeans.text.toString().isNotEmpty() &&
                resCups.text.toString().isNotEmpty()) {
                //adds the new values to the data class
                resurse.water = resurse.water + resWater.text.toString().toInt()
                resurse.milk = resurse.milk + resMilk.text.toString().toInt()
                resurse.beans = resurse.beans + resBeans.text.toString().toInt()
                resurse.cups = resurse.cups + resCups.text.toString().toInt()
                //prompts a message
                Toast.makeText(this,"YOU ADDED NEW RESOURCES", Toast.LENGTH_LONG).show()
                //goes back to MainAtivity cass
                val change = Intent(this, MainActivity::class.java)
                startActivity(change)
            } else {
                //Prompts a message for the user to add all the mandatory data.
                Toast.makeText(this,"Please Fill All Fields",Toast.LENGTH_SHORT).show()
            }

        }
    }
}