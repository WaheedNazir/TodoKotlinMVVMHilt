package com.kotlin.mvvm.ui.todos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.BaseActivity
import com.kotlin.mvvm.ui.addTodo.AddTodoActivity
import com.kotlin.mvvm.ui.addTodo.AddTodoActivity.Companion.INTENT_ADD_UPDATE
import com.kotlin.mvvm.utils.SoftKeyboardUtils
import com.kotlin.mvvm.utils.adapterUtils.SortType
import com.kotlin.mvvm.utils.adapterUtils.SimpleItemTouchHelperCallback
import com.kotlin.mvvm.utils.ToastUtil
import com.kotlin.mvvm.utils.extensions.load
import com.kotlin.mvvm.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_todos.*
import kotlinx.android.synthetic.main.empty_layout_todos.*
import kotlinx.android.synthetic.main.progress_layout_todos.*

/**
 * Developed by Waheed on 20,June,2021
 */

@AndroidEntryPoint
class TodosActivity : BaseActivity() {

    private lateinit var adapter: TodosAdapter
    private val todosViewModel: TodosViewModel by viewModels()

    /**
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos)

        init()
    }

    /**
     *
     */
    private fun init() {
        todos_list.setEmptyView(empty_view)
        todos_list.setProgressView(progress_view)

        fabAddTodo.setOnClickListener {
            startActivity(Intent(this, AddTodoActivity::class.java))
        }

        adapter = TodosAdapter()
        adapter.onNewsClicked = { todoModel ->
            startActivity(
                Intent(this, AddTodoActivity::class.java)
                    .putExtra(INTENT_ADD_UPDATE, todoModel)
            )
        }

        adapter.onSwipeDelete = { todoModel, position ->
            deleteTodo(todoModel.id)
        }

        ItemTouchHelper(SimpleItemTouchHelperCallback(adapter)).attachToRecyclerView(todos_list)

        todos_list.adapter = adapter
        todos_list.layoutManager = LinearLayoutManager(this)

        getTodos()
    }

    /**
     * Get todos using Network & DB Bound Resource
     * Observing for data change from DB and Network Both
     */
    private fun getTodos() {
        todosViewModel.getTodos().observe(this, {
            when {
                it.status.isLoading() -> todos_list.showProgressView()

                it.status.isSuccessful() -> {
                    it?.load(todos_list) { todos ->
                        adapter.replaceItems(todos ?: emptyList())
                    }
                }
                it.status.isError() -> ToastUtil.showCustomToast(
                    this,
                    (it.errorMessage) ?: "Something went wrong"
                )

            }
        })
    }


    /**
     * Delete Todos when swipe, once done delete it from local database as well
     */
    private fun deleteTodo(todoId: String) {
        todosViewModel.deleteTodo(todoId).observe(this, {
            when {
                it.status.isLoading() -> {
                    SoftKeyboardUtils.hideKeyboard(this)
                }

                it.status.isSuccessful() -> {
                    toast("Successfully deleted.")
                    it.data?.let { todo ->
                        todosViewModel.deleteTodoFromDB(todo)
                    }
                }
                it.status.isError() -> {
                    toast((it.errorMessage) ?: "Something went wrong")
                }

            }
        })
    }

    /**
     * Search Todos in local list
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return true
    }

    /**
     *
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_all -> {
                adapter.sort(SortType.ALL)
                true
            }
            R.id.menu_completed -> {
                adapter.sort(SortType.COMPLETED)
                true
            }
            R.id.menu_pending -> {
                adapter.sort(SortType.PENDING)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
