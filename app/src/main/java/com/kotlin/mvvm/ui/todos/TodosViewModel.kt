package com.kotlin.mvvm.ui.todos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kotlin.mvvm.repository.api.network.Resource
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.repository.repo.TodosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Developed by Waheed on 20,June,2021
 *
 * A container for [TodoModel] related data to show on the UI.
 */

@HiltViewModel
class TodosViewModel @Inject constructor(private val todosRepository: TodosRepository) :
    ViewModel() {

    /**
     * Loading todos from server and database
     */
    private fun todos(): LiveData<Resource<List<TodoModel>?>> = todosRepository.getTodos()


    fun getTodos() = todos()


    /**
     * Delete todos from server and local database
     */
    private fun _deleteTodo(id: String): LiveData<Resource<TodoModel>> =
        todosRepository.deleteTodo(id)

    fun deleteTodo(id: String) = _deleteTodo(id)

    /**
     *
     */
    fun deleteTodoFromDB(todo: TodoModel) {
        todosRepository.deleteTodoFromDb(todo)
    }

}