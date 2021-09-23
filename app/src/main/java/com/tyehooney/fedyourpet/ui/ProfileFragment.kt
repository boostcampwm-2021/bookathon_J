package com.tyehooney.fedyourpet.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {
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
                Toast.makeText(context, "${family.name}", Toast.LENGTH_SHORT).show() // FIXME
            }
        }

        fun bind(family: Family) {
            this.family = family
            icon.text = family.name

            // 추가 버튼
            if (family.id == -1) {
                icon.setOnClickListener {
                    // TODO: 추가 팝업 생성
                    Toast.makeText(context, "${family.name}", Toast.LENGTH_SHORT).show() // FIXME
                    profileViewModel.profiles.add(Family())
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

            if (position == profiles.size - 1) {
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