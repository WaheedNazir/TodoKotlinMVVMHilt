package com.kotlin.mvvm.ui.todos

import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mvvm.R
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.utils.adapterUtils.SortType
import com.kotlin.mvvm.utils.adapterUtils.TodoSearchFilter
import com.kotlin.mvvm.utils.adapterUtils.ItemTouchHelperAdapter
import com.kotlin.mvvm.utils.extensions.inflate
import kotlinx.android.synthetic.main.item_todo.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Developed by Waheed on 20,June,2021
 *
 * The Todos adapter to show the todos in a list.
 */
class TodosAdapter : RecyclerView.Adapter<TodosAdapter.TodoViewHolder>(),
    ItemTouchHelperAdapter,
    Filterable {

    /**
     * List of news articles
     */
    var todos: ArrayList<TodoModel> = ArrayList()

    private var todosOriginal: ArrayList<TodoModel> = ArrayList()


    var onNewsClicked: ((TodoModel) -> Unit)? = null

    var onSwipeDelete: ((TodoModel, Int) -> Unit)? = null

    /**
     * Inflate the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TodoViewHolder(parent.inflate(R.layout.item_todo))

    /**
     * Bind the view with the data
     */
    override fun onBindViewHolder(todoViewHolder: TodoViewHolder, position: Int) =
        todoViewHolder.bind(todos[position])

    /**
     * Number of items in the list to display
     */
    override fun getItemCount() = todos.size

    /**
     * View Holder Pattern
     */
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Binds the UI with the data and handles clicks
         */
        fun bind(todoItem: TodoModel) = with(itemView) {
            tvTitle.text = todoItem.title
            tvIsCompleted.text =
                String.format(" %s", if (todoItem.completed) "Completed" else "Pending")

            tvIsCompleted.apply {
                this.setTextColor(
                    if (todoItem.completed) resources.getColor(R.color.color_green) else resources.getColor(
                        R.color.color_red
                    )
                )
            }

            itemView.setOnClickListener {
                onNewsClicked?.invoke(todoItem)
            }

        }
    }

    /**
     * Swap function to set new data on updating
     */
    fun replaceItems(items: List<TodoModel>) {
        todos.clear()
        todos.addAll(items)
        todosOriginal.clear()
        todosOriginal.addAll(items)
        todos.reverse()
        todosOriginal.reverse()
        notifyDataSetChanged()
    }

    /**
     * Swap function to set new data on updating
     */
    fun replaceSortedItems(items: List<TodoModel>) {
        todos.clear()
        todos.addAll(items)
        notifyDataSetChanged()
    }

    private fun getCompletedList(): List<TodoModel> = todosOriginal.filter { it.completed }

    private fun getPendingList(): List<TodoModel> = todosOriginal.filter { !it.completed }

    /**
     * filter
     */
    fun sort(sortType: SortType) {
        when (sortType) {
            SortType.ALL -> replaceSortedItems(todosOriginal)
            SortType.COMPLETED -> replaceSortedItems(getCompletedList())
            SortType.PENDING -> replaceSortedItems(getPendingList())
        }
    }


    /**
     * This callback triggered when user performed swipe to delete
     */
    override fun onItemDismiss(position: Int) {
        onSwipeDelete?.invoke(todos[position], position)
        todos.removeAt(position)
        notifyItemRemoved(position)
    }


    /**
     * Item position changing can listen into this callback
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(todos, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(todos, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)

    }

    /**
     * Search function would be triggered from here
     */
    override fun getFilter(): android.widget.Filter {
        return TodoSearchFilter(this, todosOriginal)
    }
}