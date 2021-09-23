package com.tyehooney.fedyourpet.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tyehooney.fedyourpet.R

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private inner class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var family: Family
//        var profileIcon: Icon = view.findViewById(R.id.)

        init {
            // TODO: on click
            // 프로필 선택, 프로필 추가

        }


        fun bind(family: Family) {
            // val name = family.name
            // val name

        }
    }

    private inner class ProfileAdapter(var profiles: List<Family>)
        : RecyclerView.Adapter<ProfileViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val view = layoutInflater.inflate(R.layout.profile_icon_list, parent, false)
            return ProfileViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            holder.bind(profiles[position])
        }

        override fun getItemCount(): Int = profiles.size
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}

class Family // TODO
