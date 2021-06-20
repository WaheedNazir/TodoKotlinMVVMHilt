package com.kotlin.mvvm.ui.addTodo

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.kotlin.mvvm.R
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.ui.BaseActivity
import com.kotlin.mvvm.utils.LogUtil
import com.kotlin.mvvm.utils.SoftKeyboardUtils
import com.kotlin.mvvm.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.activity_todos.*


/**
 *
 * Developed by Waheed on 20,June,2021
 *
 * Activity used to Add, Update, Delete Todos object
 */

@AndroidEntryPoint
class AddTodoActivity : BaseActivity() {

    companion object {
        const val INTENT_ADD_UPDATE = "Add_Update"
    }

    private val addTodoViewModel: AddTodoViewModel by viewModels()
    private var todoModel: TodoModel? = null

    /**
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        loadUI()
        listeners()
    }

    /**
     *
     */
    private fun loadUI() {
        if (intent.hasExtra(INTENT_ADD_UPDATE)) {
            // Update
            todoModel = intent?.getParcelableExtra<TodoModel>(INTENT_ADD_UPDATE) as TodoModel
            note_edittext.setText(todoModel?.title)
            checkboxStatus.isChecked = todoModel?.completed ?: false
            title = getString(R.string.update_todo)
            save.text = getString(R.string.update)
            delete.visibility = View.VISIBLE
        } else {
            // Add
            title = getString(R.string.add_todo)
            save.text = getString(R.string.save)
            delete.visibility = View.GONE
        }

        note_edittext.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
        note_edittext.isSingleLine = false
    }

    /**
     *
     */
    private fun listeners() {
        save.setOnClickListener {
            val todo = note_edittext.text.toString()
            if (todo.isEmpty()) {
                note_edittext.error = "Enter todo."
                return@setOnClickListener
            }

            if (save.text.equals("Save")) {
                addTodo()
            } else {
                updateTodo()
            }
        }

        delete.setOnClickListener {
            todoModel?.let { model ->
                deleteTodo(model)
            }
        }
    }

    /**
     * Add Todos object into server database and once successful add into local database as well.
     */
    private fun addTodo() {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["title"] = note_edittext.text.toString()
        hashMap["completed"] = checkboxStatus.isChecked

        addTodoViewModel.addTodo(hashMap).observe(this, {
            when {
                it.status.isLoading() -> {
                    SoftKeyboardUtils.hideKeyboard(this)
                }

                it.status.isSuccessful() -> {
                    toast("Successfully added.")
                    resetUI()
                    it.data?.let { todo ->
                        addTodoViewModel.addTodoIntoDB(todo)
                    }
                    finish()
                }
                it.status.isError() -> {
                    toast((it.errorMessage) ?: "Something went wrong")
                }

            }
        })
    }

    /**
     * Update Todos object into server database and once successful update into local database as well.
     */
    private fun updateTodo() {
        val todoModel = TodoModel(
            checkboxStatus.isChecked,
            (todoModel?.id ?: ""),
            note_edittext.text.toString()
        )
        addTodoViewModel.updateTodo(todoModel).observe(this, {
            when {
                it.status.isLoading() -> {
                    SoftKeyboardUtils.hideKeyboard(this)
                }

                it.status.isSuccessful() -> {
                    toast("Successfully updated.")
                    resetUI()
                    it.data?.let { todo ->
                        addTodoViewModel.updateTodoIntoDB(todo)
                    }
                    finish()
                }
                it.status.isError() -> {
                    toast((it.errorMessage) ?: "Something went wrong")
                }

            }
        })
    }


    /**
     * Delete Todos object into server database and once successful update into local database as well.
     */
    private fun deleteTodo(todoModel: TodoModel) {
        addTodoViewModel.deleteTodo(todoModel.id).observe(this, {
            when {
                it.status.isLoading() -> {
                    SoftKeyboardUtils.hideKeyboard(this)
                }

                it.status.isSuccessful() -> {
                    toast("Successfully deleted.")
                    addTodoViewModel.deleteTodoFromDB(todoModel)
                    finish()
                }
                it.status.isError() -> {
                    toast((it.errorMessage) ?: "Something went wrong")
                }

            }
        })
    }

    /**
     * Once done adding / updating clear views state
     */
    private fun resetUI() {
        note_edittext.setText("")
        checkboxStatus.isSelected = false
    }

    /**
     * It is required for back press to go to parent
     * activity without again reloading otherwise
     * parent Activity will reload all contents again.
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * It is required for back press to go to parent
     * activity without again reloading otherwise
     * parent Activity will reload all contents again.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
