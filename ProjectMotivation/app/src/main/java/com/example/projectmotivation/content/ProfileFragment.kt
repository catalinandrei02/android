package com.example.projectmotivation.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projectmotivation.R
import com.example.projectmotivation.databinding.FragmentProfileBinding
import com.example.projectmotivation.model.User
import com.example.projectmotivation.utils.Constants.Text.UPLOAD
import com.example.projectmotivation.utils.Constants.Url.IMAGE
import com.example.projectmotivation.utils.Constants.Url.USER
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

import java.io.IOException
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivUserImage.setOnClickListener{
                chooseImg()
            }

            btnSave.setOnClickListener {
                uploadImg()
            }
            btnLogout.setOnClickListener {
                auth.signOut()
                findNavController().navigate(R.id.action_mainFragment_to_welcomeFragment)
                Toast.makeText(context, "Logging you out!", Toast.LENGTH_LONG).show()
            }
            btnReset.setOnClickListener{
                // TODO: Not yet implemented!
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
                    binding.tvItemUsername.text = user.name
                    if (user.userImage == ""){
                        binding.ivUserImage.setImageResource(R.drawable.blank_profile)
                    } else {
                        context?.let { it1 -> Glide.with(it1).load(user.userImage.toString()).into(binding.ivUserImage) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load users",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun chooseImg(){
        val intent = Intent()
        intent.apply {
            type = IMAGE
            action = Intent.ACTION_GET_CONTENT
        }
        resultLauncher.launch(Intent.createChooser(intent,"select image"))
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                filePath = data.data
            }
            try {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireContext().contentResolver,filePath!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                binding.apply {
                    ivUserImage.setImageBitmap(bitmap)
                    btnSave.visibility = View.VISIBLE
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImg(){
        if (filePath != null){
            val dialog = setProgressDialog(requireContext(), UPLOAD)
            dialog.show()

            val firebase = Firebase.auth.currentUser
            val ref:StorageReference = storageRef.child(IMAGE + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["userImage"] = filePath.toString()
                    firebase?.let {
                        Firebase.database.reference.child(USER).child(it.uid)
                    }?.updateChildren(hashMap as Map<String, Any>)
                    binding.btnSave.visibility = View.GONE
                    dialog.hide()
                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                        dialog.hide()
                        Toast.makeText(context,"Failed" + it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(llPadding, llPadding, llPadding, llPadding)
            gravity = Gravity.CENTER
        }

        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.apply {
            isIndeterminate = true
            setPadding(0, 0, llPadding, 0)
            layoutParams = llParam
        }


        llParam = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER

        val tvText = TextView(context)
        tvText.apply {
            text = message
            setTextColor(Color.parseColor("#000000"))
            textSize = 20.toFloat()
            layoutParams = llParam
        }

        ll.apply {
            addView(progressBar)
            addView(tvText)
        }

        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(true)
            setView(ll)

        }

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
}