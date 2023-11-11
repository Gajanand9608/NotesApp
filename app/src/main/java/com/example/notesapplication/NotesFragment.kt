package com.example.notesapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapplication.databinding.FragmentNotesBinding
import com.example.notesapplication.models.NoteRequest
import com.example.notesapplication.models.NoteResponse
import com.example.notesapplication.utils.NetworkResult
import com.example.notesapplication.utils.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private  var _binding : FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private var note : NoteResponse? = null
    private val noteViewModel  by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater ,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.noteStatusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(noteId = it._id) }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val desc = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(desc, title)
            if(note == null){
                noteViewModel.createNote(noteRequest)
            }else{
                noteViewModel.updateNote(noteRequest, note!!._id)
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if(jsonNote!=null){
            note = Gson().fromJson(jsonNote,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }else{
            binding.addEditText.text = "Add Note"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}