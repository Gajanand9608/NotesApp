package com.example.notesapplication.api

import com.example.notesapplication.models.NoteRequest
import com.example.notesapplication.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesAPI {

    @GET("note")
    suspend fun getNotes() : Response<List<NoteResponse>>
    @POST("note")
    suspend fun createNote(@Body noteRequest: NoteRequest) : Response<NoteResponse>

    @PUT("note/{noteId}")
    suspend fun updateNote(@Body noteRequest: NoteRequest,@Path("noteId") noteId : String) : Response<NoteResponse>

    @DELETE("note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId : String) : Response<NoteResponse>

}