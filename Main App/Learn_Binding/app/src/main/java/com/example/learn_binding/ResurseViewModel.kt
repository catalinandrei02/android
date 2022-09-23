package com.example.learn_binding
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class ResurseViewModel(resurseApplication: Application) : AndroidViewModel(resurseApplication) {

    lateinit var dbRef: DatabaseReference
    private val _messageMutableLiveData =
        MutableLiveData(R.string.second_fragment)
    val messageLiveData: LiveData<Int> get() = _messageMutableLiveData

    val db = ResurseOnline( 0.0,0.0,0.0,0.0,0.0)

    fun makeOnlineCoffe(water: Double,milk: Double, beans: Double,money: Double){
        getResources()
        if (db.onlWater - water >= 0 && db.onlMilk - milk >= 0 && db.onlBeans - beans >= 0 && db.onlCups >= 1) {
            val userCoffeMachine = ResurseOnline(db.onlWater - water,db.onlMilk - milk,db.onlBeans - beans,db.onlCups - 1,db.onlMoney + money)
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
    fun getResources(): ResurseOnline {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").get().addOnSuccessListener {
            db.onlWater = it.child("onlWater").value.toString().toDouble()
            db.onlMilk = it.child("onlMilk").value.toString().toDouble()
            db.onlBeans = it.child("onlBeans").value.toString().toDouble()
            db.onlCups = it.child("onlCups").value.toString().toDouble()
            db.onlMoney = it.child("onlMoney").value.toString().toDouble()
        }
        return db
    }
    fun setMoney() {
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").child("onlMoney").setValue(0)
    }
}
