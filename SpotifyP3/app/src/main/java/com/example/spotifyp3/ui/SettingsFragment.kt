package com.example.spotifyp3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.spotifyp3.R
import com.example.spotifyp3.adapters.ListAdapter
import com.example.spotifyp3.databinding.FragmentSettingsBinding
import com.example.spotifyp3.model.User
import com.example.spotifyp3.ui.settings.*
import com.example.spotifyp3.utils.Constants.Url.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var listView: ListView
    lateinit var arrayList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        getUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.settingsMenu
        arrayList = resources.getStringArray(R.array.settings_menu)
        val arrayAdapter = context?.let { ListAdapter(it, arrayList) }
        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> replaceFragment(AccountFragment())
                1 -> replaceFragment(DataSaverFragment())
                2 -> replaceFragment(LanguagesFragment())
                3 -> replaceFragment(PlaybackFragment())
                4 -> replaceFragment(ExplicitFragment())
                5 -> replaceFragment(DevicesFragment())
                6 -> replaceFragment(CarFragment())
                7 -> replaceFragment(SocialFragment())
                8 -> replaceFragment(AssistantFragment())
                9 -> replaceFragment(AudioQualityFragment())
                10 -> replaceFragment(VideoQualityFragment())
                11 -> replaceFragment(StorageFragment())
                12 -> replaceFragment(NotificationsFragment())
                13 -> replaceFragment(LocalFilesFragment())
                14 -> replaceFragment(AboutFragment())
                else -> {}
            }
        }
        binding.apply {
            ivBack.setOnClickListener{
                replaceFragment(HomeFragment())
            }
            btnLogout.setOnClickListener {
                auth.signOut()
                findNavController().navigate(R.id.action_mainFragment_to_welcomeFragment)
                Toast.makeText(context, "Logging you out!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getUser(){
        val firebase = Firebase.auth.currentUser
        val dbRef: DatabaseReference = Firebase.database.reference.child(USER).child(firebase!!.uid)
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                user?.let {
                    binding.tvName.text = user.userName
                    if (user.userImage == ""){
                        binding.ivItemUserImage.setImageResource(R.drawable.blank_profile)
                    } else {
                        context?.let { it1 -> Glide.with(it1).load(user.userImage.toString()).into(binding.ivItemUserImage) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load users",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}
