package com.tyehooney.fedyourpet.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tyehooney.fedyourpet.R
import com.tyehooney.fedyourpet.databinding.FragmentMainBinding
import com.tyehooney.fedyourpet.model.Pet
import com.tyehooney.fedyourpet.util.getMyPets

class MainFragment : Fragment(R.layout.fragment_main), MainListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var petAdapter: PetAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.apply {
            chooseProfileButton.setOnClickListener {
                this@MainFragment.findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
            }
        }

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)

        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.profileFragment) {
                sharedPreferences.edit().remove("profile").apply()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profile = sharedPreferences.getString("profile", null)
        profile?.let {
            binding.profileTextView.text = it
        }
    }

    override fun onResume() {
        super.onResume()
        val uid = sharedPreferences.getString("uid", null)
        uid?.let {
            getMyPets(uid, this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(pet: Pet) {
            Glide.with(itemView.context)
                .load(pet.image)
                .into(itemView.findViewById(R.id.pet_image_view))

            itemView.findViewById<TextView>(R.id.pet_name_text_view).text = pet.name

            itemView.setOnClickListener {
                findNavController()
                    .navigate(
                        MainFragmentDirections.actionMainFragmentToFeedPetFragment(
                            pet.id, pet.name, pet.image
                        )
                    )
            }
        }
    }

    private inner class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                // 새 애완동물 추가
                this@MainFragment.findNavController().navigate(R.id.action_mainFragment_to_animalAddFragment)
            }
        }
    }

    private inner class PetAdapter(var pets: List<Pet>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val viewTypePet = 1
        private val viewTypeAdd = 2

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                pets.size -> viewTypeAdd
                else -> viewTypePet
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                viewTypePet -> {
                    PetViewHolder(
                        layoutInflater.inflate(
                            R.layout.item_pet,
                            parent,
                            false
                        )
                    )
                }
                else -> {
                    AddViewHolder(layoutInflater.inflate(R.layout.item_add_pet, parent, false))
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is PetViewHolder) {
                val pet = pets[position]
                holder.bind(pet)
            }
        }

        override fun getItemCount(): Int = pets.size + 1
    }

    override fun onGetMyPetsSuccess(pets: List<Pet>) {
        petAdapter = PetAdapter(pets)
        binding.petsRecyclerView.adapter = petAdapter
    }

    override fun onGetMyPetsFailed(msg: String) {
        Toast.makeText(context, "error : $msg", Toast.LENGTH_SHORT).show()
    }
}