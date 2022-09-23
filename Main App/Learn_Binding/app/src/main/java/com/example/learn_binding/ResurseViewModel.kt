package com.example.learn_binding
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class ResurseViewModel(resurseApplication: Application) : AndroidViewModel(resurseApplication) {

    //Am creeat referinta pentru DataBase
    lateinit var dbRef: DatabaseReference
    //Mutable live data care modifica mesajul din al 2-lea fragment in functie de
    private val _messageMutableLiveData =
        MutableLiveData(R.string.second_fragment)
    val messageLiveData: LiveData<Int> get() = _messageMutableLiveData
    //Aici am creeat un obiect local DB unde vor fi salvate valorile din DataBase
    val db = ResurseOnline( 0.0,0.0,0.0,0.0,0.0)

    fun makeOnlineCoffe(water: Double,milk: Double, beans: Double,money: Double){
        getResources()//apelam database ca sa updateze db
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
        //Am facut functia getResources pentru a putea updata resursele de fiecare data cand am nevoie
        //pentru ca nu am stiut cum sa fac cu metoda oferita in documentatia de la firebase (cea cu ValueEventListener si onDataChange)
        //Functia o sa ia resursele din DataBase si o sa le stocheze local in DataClass-ul "db" cat timp e aplicatia pornita si o sa updateze "db" de fiecare data cand este chemata
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
        //modifica valoarea "money" in 0 in interiorul DataBase-ului
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")
        dbRef.child("resources").child("onlMoney").setValue(0)
    }
}
