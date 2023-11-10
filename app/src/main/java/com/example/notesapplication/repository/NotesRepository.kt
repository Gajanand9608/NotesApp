package com.example.notesapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapplication.api.NotesAPI
import com.example.notesapplication.models.NoteRequest
import com.example.notesapplication.models.NoteResponse
import com.example.notesapplication.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData : LiveData<NetworkResult<List<NoteResponse>>> get() = _notesLiveData
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>> get() = _statusLiveData
    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.getNotes()
        handleResponse(response)
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNote(noteRequest)
        noteAPIResponseHandler(response,"Note Created")
    }

    suspend fun deleteNote(noteId: String){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.deleteNote(noteId)
        noteAPIResponseHandler(response,"Note Deleted")
    }
    suspend fun updateNote(noteRequest: NoteRequest, noteId : String){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNote(noteRequest, noteId)
        noteAPIResponseHandler(response, "Note Updated")
    }

    private fun noteAPIResponseHandler(response: Response<NoteResponse>, message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error(message))
        }
    }

    private fun handleResponse(response: Response<List<NoteResponse>>) {
        if(response.isSuccessful && response.body() != null){
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody() !=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _notesLiveData.postValue(NetworkResult.Loading())
        }
    }
}