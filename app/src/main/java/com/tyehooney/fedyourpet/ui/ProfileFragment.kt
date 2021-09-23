package com.tyehooney.fedyourpet.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.ProfileFragmentBinding
import com.tyehooney.fedyourpet.util.getProfiles

class ProfileFragment : Fragment(), ProfileListener {
    val TAG = "Profile"

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileRecyclerView: RecyclerView
    private var profileAdapter: ProfileAdapter? = null
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        profileRecyclerView = binding.recyclerProfile
        profileRecyclerView.layoutManager = GridLayoutManager(context, 2)

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)

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

        init {
            icon.setOnClickListener {
                // TODO: navigate 할 때 구성원 누군지 정보 넘겨줌
                Navigation.findNavController(view).navigate(R.id.mainFragment)
            }
        }

        fun bind(name: String) {
            icon.text = name
        }
    }

    private inner class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val uid = sharedPreferences.getString("uid", null)
                uid?.let {
                    // 새 프로필 추가
                    // addNewProfile() 활용
                }
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
                    AddViewHolder(layoutInflater.inflate(R.layout.profile_add, parent, false))
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
        TODO("Not yet implemented")
    }
}