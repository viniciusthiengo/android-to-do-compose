package com.example.to_docompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.data.repositories.DataStoreRepository
import com.example.to_docompose.data.repositories.ToDoRepository
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.Constants.MAX_TITLE_LENGTH
import com.example.to_docompose.util.RequestState
import com.example.to_docompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository
                    .getAllTasks
                    .collect { toDoTasksList ->
                        _allTasks.value = RequestState.Success(data = toDoTasksList)
                    }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(error = e)
        }
    }

    fun searchTasks(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository
                    .searchDatabase(query = searchQuery)
                    .collect { tasks ->
                        _searchedTasks.value = RequestState.Success(data = tasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(error = e)
        }

        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository
                .getSelectedTask(taskId = taskId)
                .collect { task ->
                    _selectedTask.value = task
                }
        }
    }

    fun handlerDatabaseActions(action: Action) {
        when (action) {
            Action.ADD,
            Action.UNDO -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            else -> {
                //TODO()
            }
        }

        this.action.value = Action.NO_ACTION
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.addTask(toDoTask = toDoTask)
        }

        searchAppBarState.value = SearchAppBarState.CLOSED
        searchTextState.value = ""
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }
}