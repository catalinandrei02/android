package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentFourthBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class FragmentFourth : Fragment() {

    private lateinit var binding: FragmentFourthBinding
    //Database initialization
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFourthBinding.inflate(inflater, container, false)
        dbRef = FirebaseDatabase.getInstance().getReference("CoffeMachine")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener {

            /*var apa: Int? = null
            var lapte: Int? = null
            var boabe: Int? = null
            var pahare: Int? = null
            var bani: Int? = null

            dbRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    apa = snapshot.getValue<Int>()
                    lapte = snapshot.getValue<Int>()
                    boabe = snapshot.getValue<Int>()
                    pahare = snapshot.getValue<Int>()
                    bani = snapshot.getValue<Int>()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })*/
            val dbWater = binding.apa.text.toString().toDouble()
            val dbMilk = binding.lapte.text.toString().toDouble()
            val dbBeans = binding.boabe.text.toString().toDouble()
            val dbCups = binding.pahare.text.toString().toDouble()
            val dbMoney = binding.bani.text.toString().toDouble()

            //create user hash id and values
            //val userID = dbRef.push().key!!
            val userCoffeMachine = ResurseOnline(dbWater,dbMilk,dbBeans,dbCups,dbMoney)
            dbRef.child("resources").setValue(userCoffeMachine)
                .addOnSuccessListener {
                    Toast.makeText(context,"Resources added successfully!",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_fragmentFourth_to_FirstFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(context,"Resources adding failed!",Toast.LENGTH_SHORT).show()
                }
        }
    }

}