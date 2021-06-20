package com.kotlin.mvvm.repository.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.kotlin.mvvm.app.AppExecutors
import com.kotlin.mvvm.repository.db.TodoDao
import com.kotlin.mvvm.repository.api.ApiServices
import com.kotlin.mvvm.repository.api.network.NetworkAndDBBoundResource
import com.kotlin.mvvm.repository.api.network.NetworkResource
import com.kotlin.mvvm.repository.api.network.Resource
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.utils.ConnectivityUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Developed by Waheed on 20,June,2021
 */

/**
 * Repository abstracts the logic of fetching the data and persisting it for
 * offline. They are the data source as the single source of truth.
 *
 */

@Singleton
class TodosRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context,
    private val appExecutors: AppExecutors = AppExecutors()
) {

    /**
     * Fetch the todos from database if exist else fetch from web
     * and persist them in the database
     */
    fun getTodos(): LiveData<Resource<List<TodoModel>?>> {

        return object : NetworkAndDBBoundResource<List<TodoModel>, List<TodoModel>>(appExecutors) {
            override fun saveCallResult(item: List<TodoModel>) {
                if (item.isNotEmpty()) {
                    todoDao.deleteAllTodos()
                    todoDao.insertTodos(item)
                }
            }

            override fun shouldFetch(data: List<TodoModel>?) =
                (ConnectivityUtil.isConnected(context))

            override fun loadFromDb() = todoDao.getTodos()

            override fun createCall() = apiServices.getTodos()

        }.asLiveData()
    }

    /**
     * Delete todos from server
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