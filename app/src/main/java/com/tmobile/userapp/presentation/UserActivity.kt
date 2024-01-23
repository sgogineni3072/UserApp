package com.tmobile.userapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tmobile.userapp.R
import com.tmobile.userapp.framework.UserEvent
import com.tmobile.userapp.framework.UserUIModel
import com.tmobile.userapp.framework.UserViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : ComponentActivity() {
    private val viewModel by viewModel<UserViewModel>()
    lateinit var progress: ProgressBar
    lateinit var recyclerView: RecyclerView

    private var stateJob: Job? = null
    private var eventJob: Job? = null

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        progress = findViewById(R.id.progress)

        setupRecyclerView()
        viewModel.getUsers()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        userAdapter = UserAdapter(this)
        recyclerView.adapter = userAdapter
    }

    private fun observeViewState() {
        stateJob = lifecycleScope.launch() {
            viewModel.viewState.collectLatest { viewState ->
                showLoadingStatus(viewState.shouldShowLoadingMessage)
                displayUsers(viewState.users)
            }
        }
    }

    private fun showLoadingStatus(showLoading: Boolean) {
        progress.isVisible = showLoading
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayUsers(items: List<UserUIModel>) {
        Log.d("UserActivity", "ShowItems $items")
        userAdapter.setData(items)
        userAdapter.notifyDataSetChanged()
    }

    private fun observeEvents() {
        eventJob = lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is UserEvent.ShowError -> showErrorMessage(event.errorMessage)
                }
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        observeViewState()
        observeEvents()
    }

    override fun onStop() {
        super.onStop()
        stateJob?.cancel()
        eventJob?.cancel()
    }
}
