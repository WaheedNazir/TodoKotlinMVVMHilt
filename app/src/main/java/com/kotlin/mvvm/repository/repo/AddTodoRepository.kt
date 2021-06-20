package com.kotlin.mvvm.repository.repo

import androidx.lifecycle.LiveData
import com.kotlin.mvvm.app.AppExecutors
import com.kotlin.mvvm.repository.api.ApiServices
import com.kotlin.mvvm.repository.api.network.NetworkResource
import com.kotlin.mvvm.repository.api.network.Resource
import com.kotlin.mvvm.repository.db.TodoDao
import com.kotlin.mvvm.repository.model.TodoModel
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Developed by Waheed on 20,June,2021
 */
@Singleton
class AddTodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val apiServices: ApiServices,
    private val appExecutors: AppExecutors = AppExecutors()
) {

    /**
     */
    fun addTodo(hashMap: HashMap<String, Any>): LiveData<Resource<TodoModel>> {
        return object : NetworkResource<TodoModel>() {
            override fun createCall(): LiveData<Resource<TodoModel>> {
                return apiServices.addTodo(hashMap)
            }

        }.asLiveData()
    }

    /**
     * Add new Todos object
     */
    fun addToDb(todo: TodoModel) {
        appExecutors.diskIO().execute {
            todoDao.addTodo(todo)
        }
    }


    /**
     * Update Todos object on server
     */
    fun updateTodo(todoModel: TodoModel): LiveData<Resource<TodoModel>> {
        return object : NetworkResource<TodoModel>() {
            override fun createCall(): LiveData<Resource<TodoModel>> {
                return apiServices.updateTodo(todoModel.id, todoModel.title, todoModel.completed)
            }

        }.asLiveData()
    }

    /**
     * update Todos object on local database
     */
    fun updateTodoDb(todo: TodoModel) {
        appExecutors.diskIO().execute {
            todoDao.addTodo(todo)
        }
    }


    /**
     * Delete todos object from server
     */
    fun deleteTodo(todoId: String): LiveData<Resource<TodoModel>> {
        return object : NetworkResource<TodoModel>() {
            override fun createCall(): LiveData<Resource<TodoModel>> {
                return apiServices.deleteTodo(todoId)
            }

        }.asLiveData()
    }

    /**
     * Delete todos object from local database
     */
    fun deleteTodoFromDb(todo: TodoModel) {
        appExecutors.diskIO().execute {
            todoDao.delete(todo)
        }
    }

}