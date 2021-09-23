package com.tyehooney.fedyourpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedPet.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedPet : Fragment() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_pet, container, false)
        init(view)
        setListener()
        return view
    }
    fun init(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
    }
    fun setListener(){
        toolbar.setNavigationOnClickListener{
            //Toast.makeText(this, "back",Toast.LENGTH_SHORT).
        }
    }

}