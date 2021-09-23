package com.tyehooney.fedyourpet.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {
    val TAG = "Profile"

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileRecyclerView: RecyclerView
    private var profileAdapter: ProfileAdapter? = null
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

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
        updateUI()
        return binding.root
    }

    /**
     * Recycler view
     */

    private fun updateUI() {
        val profiles = profileViewModel.profiles
        profileAdapter = ProfileAdapter(profiles)
        profileRecyclerView.adapter = profileAdapter
    }

    private inner class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var family: Family
        val icon: Button = itemView.findViewById(R.id.icon_profile)

        init {
            icon.setOnClickListener {
                // TODO: navigate 할 때 구성원 누군지 정보 넘겨줌
                Navigation.findNavController(view).navigate(R.id.mainFragment)
            }
        }

        fun bind(family: Family) {
            this.family = family
            icon.text = family.name

            // 추가 버튼
            if (family.id == -1) {
                icon.setOnClickListener {
                    // TODO: 추가 팝업 생성
                    val test = Family()
                    test.name = "아들"
                    test.id = 1
                    profileViewModel.profiles.add(test)
                    profileAdapter!!.notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    private inner class ProfileAdapter(var profiles: List<Family>)
        : RecyclerView.Adapter<ProfileViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val view = layoutInflater.inflate(R.layout.profile_icon_list, parent, false)
            return ProfileViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            val profile = profiles[position]

            holder.bind(profile)

            if (position == profiles.size - 1) { // FIXME: id == -1 -> 마지막 패밀리 일 때
                val emptyFamily = Family()
                emptyFamily.id = -1
                emptyFamily.name = "+"
                holder.bind(emptyFamily)
            }
        }

        override fun getItemCount(): Int = profiles.size
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}