package com.example.spotifyp3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ListView
import com.example.spotifyp3.R
import com.example.spotifyp3.adapters.ListAdapter
import com.example.spotifyp3.adapters.SearchAdapter
import com.example.spotifyp3.databinding.FragmentSearchBinding
import com.example.spotifyp3.ui.settings.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var listView: GridView
    lateinit var arrayList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.searchMenu
        arrayList = resources.getStringArray(R.array.search_menu)
        val arrayAdapter = context?.let { SearchAdapter(it, arrayList) }
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
                else -> {}
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}