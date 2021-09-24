package com.tyehooney.fedyourpet.ui

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.ProfileFragmentBinding
import com.tyehooney.fedyourpet.util.addNewProfile
import com.tyehooney.fedyourpet.util.getProfiles

class ProfileFragment : Fragment(), ProfileListener {
    val TAG = "Profile"

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileRecyclerView: RecyclerView
    private var profileAdapter: ProfileAdapter? = null

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        profileRecyclerView = binding.recyclerProfile
        profileRecyclerView.layoutManager = GridLayoutManager(context, 2)

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)

        val profile = sharedPreferences.getString("profile", null)
        profile?.let {
            findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
        }

        updateUI()
        binding.apply {
            buttonRanking.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_rankingFragment)
            }
        }
        return binding.root
    }

    /**
     * Recycler view
     */

    private fun updateUI() {
        val uid = sharedPreferences.getString("uid", null)
        uid?.let {
            getProfiles(it, this)
        }
    }

    private inner class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val icon: Button = itemView.findViewById(R.id.icon_profile)

        fun bind(name: String) {
            icon.text = name
            icon.setOnClickListener {
                // TODO: navigate 할 때 구성원 누군지 정보 넘겨줌
                sharedPreferences.edit().putString("profile", name).apply()
                Navigation.findNavController(itemView).navigate(R.id.mainFragment)
            }
        }
    }

    private inner class AddViewHolder(view: View, profiles: List<String>) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val uid = sharedPreferences.getString("uid", null)
                val builder = AlertDialog.Builder(this@ProfileFragment.context)
                val dialogView = layoutInflater.inflate(R.layout.profile_add_dialog, null)
                val inputText = dialogView.findViewById<EditText>(R.id.dialogEditText)
                var newProfile: String

                builder.setView(dialogView)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        newProfile = inputText.text.toString()
                        uid?.let {
                            // 새 프로필 추가
                            addNewProfile(uid, profiles + newProfile, this@ProfileFragment)
                        }
                    }
                    .setNegativeButton("취소") { dialogInterface, i -> }
                    .show()
            }
        }
    }

    private inner class ProfileAdapter(var profiles: List<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val viewTypeProfile = 1
        private val viewTypeAdd = 2

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                profiles.size -> viewTypeAdd
                else -> viewTypeProfile
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                viewTypeProfile -> {
                    ProfileViewHolder(
                        layoutInflater.inflate(
                            R.layout.profile_icon_list,
                            parent,
                            false
                        )
                    )
                }
                else -> {
                    AddViewHolder(layoutInflater.inflate(R.layout.profile_add, parent, false), profiles)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ProfileViewHolder) {
                val profile = profiles[position]
                holder.bind(profile)
            }
        }

        override fun getItemCount(): Int = profiles.size + 1
    }

    override fun onProfileReceived(profiles: List<String>) {
        profileAdapter = ProfileAdapter(profiles)
        profileRecyclerView.adapter = profileAdapter
    }

    override fun onNewProfileAdded() {
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}