package com.kotlin.mvvm.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kotlin.mvvm.repository.model.TodoModel


/**
 * Developed by Waheed on 20,June,2021
 *
 * Abstracts access to the news database
 */
@Dao
interface TodoDao {
    /**
     * Insert todos into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodos(todos: List<TodoModel>): List<Long>

    /**
     * Insert todos into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodo(todoModel: TodoModel)

    /**
     * Get all the todos from database
     */
    @Query("SELECT * FROM todos_table")
    fun getTodos(): LiveData<List<TodoModel>>

    /**
     * Delete all todos from database
     */
    @Query("DELETE FROM todos_table")
    abstract fun deleteAllTodos()

    /**
     * Delete single todos object from database
     */
    @Delete
    abstract fun delete(todoModel: TodoModel)
}