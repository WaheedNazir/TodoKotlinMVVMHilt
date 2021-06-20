package com.kotlin.mvvm.ui.addTodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kotlin.mvvm.repository.api.network.Resource
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.repository.repo.AddTodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *  Developed by Waheed on 20,June,2021
 *
 * A container for [TodoModel] related data to show on the UI.
 */

@HiltViewModel
class AddTodoViewModel @Inject constructor(private val addTodoRepository: AddTodoRepository) :
    ViewModel() {

    /**
     * Loading todos from server and local database
     */
    private fun _addTodo(hashMap: HashMap<String, Any>): LiveData<Resource<TodoModel>> =
        addTodoRepository.addTodo(hashMap)

    fun addTodo(hashMap: HashMap<String, Any>) = _addTodo(hashMap)

    /**
     * Add single todos object int database
     */
    fun addTodoIntoDB(todo: TodoModel) {
        addTodoRepository.addToDb(todo)
    }


    /**
     * Update todos object on server database
     */
    private fun _updateTodo(todoModel: TodoModel): LiveData<Resource<TodoModel>> =
        addTodoRepository.updateTodo(todoModel)

    fun updateTodo(todoModel: TodoModel) = _updateTodo(todoModel)


    /**
     * Update todos object into local database
     */
    fun updateTodoIntoDB(todoModel: TodoModel) {
        addTodoRepository.addToDb(todoModel)
    }


    /**
     * Deleting todos object from internet and database
     */
    private fun _deleteTodo(id: String): LiveData<Resource<TodoModel>> =
        addTodoRepository.deleteTodo(id)

    fun deleteTodo(id: String) = _deleteTodo(id)

    /**
     *
     */
    fun deleteTodoFromDB(todo: TodoModel) {
        addTodoRepository.deleteTodoFromDb(todo)
    }

}