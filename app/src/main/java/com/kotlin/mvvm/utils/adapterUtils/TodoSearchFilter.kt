package com.kotlin.mvvm.utils.adapterUtils

import android.widget.Filter
import com.kotlin.mvvm.repository.model.TodoModel
import com.kotlin.mvvm.ui.todos.TodosAdapter
import java.util.*

/**
 * Developed by Waheed on 20,June,2021
 *
 * Filter / local sorting helper class
 *
 */
class TodoSearchFilter(
    private val mAdapter: TodosAdapter,
    var todosList: ArrayList<TodoModel>
) : Filter() {

    private var filteredList: ArrayList<TodoModel> = ArrayList()

    override fun performFiltering(search: CharSequence): FilterResults {
        filteredList.clear()
        val results = FilterResults()
        if (search.isEmpty()) {
            filteredList.addAll(todosList)
        } else {
            for (contact in todosList) {
                if (contact.title.contains(search, ignoreCase = true)) {
                    filteredList.add(contact)
                }
            }
        }
        results.values = filteredList
        results.count = filteredList.size
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        mAdapter.replaceSortedItems(results.values as ArrayList<TodoModel>)
    }

}