package com.example.notesapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapplication.databinding.FragmentMainBinding
import com.example.notesapplication.models.NoteResponse
import com.example.notesapplication.utils.NetworkResult
import com.example.notesapplication.utils.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

   private var _binding : FragmentMainBinding? =null
    private val binding get()  = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding  = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked)// this is used to pass funcition declared in main fragment to adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        noteViewModel.getAllNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_notesFragment)
        }
    }


    private fun onNoteClicked(noteResponse: NoteResponse){
        // here we have to pass this noteresponse to note fragemnt and also redirec to note fragment
        val bundle  = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_notesFragment, bundle)
    }
    private fun bindObservers() {
        noteViewModel.noteResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressbar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    // in case of success we will have list of network list, so build recycler view
                    adapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressbar.isVisible = true
                }
            }
        })
    }

}