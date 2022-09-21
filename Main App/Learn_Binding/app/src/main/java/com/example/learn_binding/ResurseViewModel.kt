package com.example.learn_binding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class ResurseViewModel(resurseApplication: Application) : AndroidViewModel(resurseApplication) {
    private lateinit var dbRef: DatabaseReference
    private val _messageMutableLiveData =
        MutableLiveData(R.string.second_fragment)
    val messageLiveData: LiveData<Int> get() = _messageMutableLiveData
    private val db = ResurseOnline( 0.0,0.0,0.0,0.0,0.0)


    fun makeOnlineCoffe(water: Double,milk: Double, beans: Double,money: Double){
        //get values
        val apa = getWater()
        val lapte = getMilk()
        val boabe = getBeans()
        val pahare = getCups()
        val bani = getMoney()
        if (apa - water >= 0 && lapte - milk >= 0 && boabe - beans >= 0 && pahare >= 1) {
            val userCoffeMachine = ResurseOnline(apa - water,lapte - milk,boabe - beans,pahare - 1,bani + money)
            dbRef.child("resources").setValue(userCoffeMachine)
            _messageMutableLiveData.value = R.string.succes
        } else when {
            db.onlWater < water -> _messageMutableLiveData.value =
                R.string.errW
            db.onlMilk < milk -> _messageMutableLiveData.value =
                R.string.errM
            db.onlBeans < beans -> _messageMutableLiveData.value =
                R.string.errB
            db.onlCups < 1 -> _messageMutableLiveData.value =
                R.string.errC
        }
    }
    fun getWater(): Double {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlWater = it.child("onlWater").value.toString().toDouble()
        }
            return db.onlWater
    }
    fun getMilk(): Double {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlMilk = it.child("onlMilk").value.toString().toDouble()
        }
        return db.onlMilk
    }
    fun getBeans(): Double {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlBeans = it.child("onlBeans").value.toString().toDouble()
        }
        return db.onlBeans
    }
    fun getCups(): Double {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlCups = it.child("onlCups").value.toString().toDouble()
        }
        return db.onlCups
    }
    fun getMoney(): Double {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlMoney = it.child("onlMoney").value.toString().toDouble()
        }
        return db.onlMoney
    }
    fun setMoney() {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").child("onlMoney").setValue(0)
    }

}