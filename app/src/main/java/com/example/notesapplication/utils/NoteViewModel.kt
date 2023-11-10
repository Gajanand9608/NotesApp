package com.example.notesapplication.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapplication.models.NoteRequest
import com.example.notesapplication.models.NoteResponse
import com.example.notesapplication.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {
    val noteResponseLiveData : LiveData<NetworkResult<List<NoteResponse>>> get() = notesRepository.notesLiveData
    val noteStatusLiveData : LiveData<NetworkResult<String>>  get() = notesRepository.statusLiveData
    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.createNote(noteRequest)
        }
    }

    fun updateNote(noteRequest: NoteRequest, noteId : String){
        viewModelScope.launch {
            notesRepository.updateNote(noteRequest, noteId)
        }
    }
    fun deleteNote(noteId: String){
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }
    fun getAllNotes(){
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }
}