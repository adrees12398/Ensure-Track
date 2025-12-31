package com.example.ensuretrackapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ensuretrackapplication.Adapter
import com.example.ensuretrackapplication.ClientModel
import com.example.ensuretrackapplication.R
import com.example.ensuretrackapplication.adapter.ClientAdapter
import com.example.ensuretrackapplication.databinding.FragmentClientsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ClientsFragment : Fragment() {
    lateinit var binding: FragmentClientsBinding
    lateinit var Adapter: ClientAdapter
    var list = mutableListOf<ClientModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientsBinding.inflate(layoutInflater, container, false)
        binding.recyclerViewClient.layoutManager = LinearLayoutManager(requireActivity())
        list.add(
            ClientModel(
                "1",
                "Hassan",
                "35202-8343332-4",
                "03039787623",
                "premium",
                "02-10-25",
                "Yes",
                "good",
                R.drawable.baseline_person_24
            )
        )
        list.add(
            ClientModel(
                "1",
                "Hassan",
                "35202-8343332-4",
                "03039787623",
                "premium",
                "02-10-25",
                "Yes",
                "good",
                R.drawable.baseline_person_24
            )
        )
        list.add(
            ClientModel(
                "1",
                "Hassan",
                "35202-8343332-4",
                "03039787623",
                "premium",
                "02-10-25",
                "Yes",
                "good",
                R.drawable.baseline_person_24
            )
        )
        list.add(
            ClientModel(
                "1",
                "Hassan",
                "35202-8343332-4",
                "03039787623",
                "premium",
                "02-10-25",
                "Yes",
                "good",
                R.drawable.baseline_person_24
            )
        )
        list.add(
            ClientModel(
                "1",
                "Hassan",
                "35202-8343332-4",
                "03039787623",
                "premium",
                "02-10-25",
                "Yes",
                "good",
                R.drawable.baseline_person_24
            )
        )
        Adapter = ClientAdapter(list) {

        }
        binding.recyclerViewClient.adapter = Adapter
        return binding.root
    }

}