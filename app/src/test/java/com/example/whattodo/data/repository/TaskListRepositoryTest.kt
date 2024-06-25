package com.example.whattodo.data.repository

import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.notificationScheduler.NotificationScheduler
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.tasks.TasksListRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.time.LocalDateTime

class TaskListRepositoryTest {
    private val taskItemDao = TaskItemDaoFake()
    private val taskListDao = TaskListDaoFake()
    private val notificationScheduler = mockk<NotificationScheduler>()
    private val taskListRepository: TasksListRepository = TaskListRepositoryImpl(
        taskListDao = taskListDao,
        taskItemDao = taskItemDao,
        notificationScheduler = notificationScheduler
    )

    @Before
    fun setup() = runBlocking {
        taskListDao.clearDb()
        taskItemDao.clearDb()
    }

    @Test
    fun createNewTaskList_verifyActiveListIsCreatedWithCorrectTitle() = runBlocking {
        // Arrange
        val expectedTaskList = CreateTaskList(isActive = true, title = "My Task List")

        // Act
        taskListRepository.insert(expectedTaskList)
        val createdList = taskListRepository.getActive()

        // Assert
        Assertions.assertEquals(expectedTaskList.title, createdList?.title)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllTaskLists_returnsLoadingAndSuccessResults() = runTest {
        // Arrange
        val expectedTaskList = CreateTaskList(isActive = true, title = "My Task List")
        taskListRepository.insert(expectedTaskList)

        // Act
        val results = mutableListOf<DataResult<List<TaskList>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            taskListRepository.getAll().toList(results)
        }

        // Assert
        Assertions.assertEquals(2, results.size)
        Assertions.assertTrue(results[0] is DataResult.Loading)

        val successResult = results[1] as? DataResult.Success
        Assertions.assertNotNull(successResult)
        Assertions.assertEquals(1, successResult?.data?.size)
        Assertions.assertEquals(expectedTaskList.title, successResult?.data?.get(0)?.title)
    }

    @Test
    fun importAll_verifyImportWithClearDb() = runTest {
        // Arrange
        every { notificationScheduler.cancelAllScheduledNotification() }.returns(Unit)
        val taskItem = createTestTaskItem()
        val taskList = createTestTaskList(taskItem)
        val expectedTaskList = listOf(
            taskList.copy(id = 1, todoTasksItems = listOf(taskItem.copy(id = 1, parentListId = 1)))
        )
        // Act
        taskListRepository.importAll(expectedTaskList, clearDb = true)
        // Assert
        val importedTaskList = taskListRepository.getActive()
        Assertions.assertEquals(expectedTaskList.firstOrNull(), importedTaskList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAll_verifyImportWithoutClearDb() = runTest {
        // Arrange
        val taskItem = createTestTaskItem()
        val taskList = createTestTaskList(taskItem)
        val expectedTaskList = listOf(
            taskList.copy(id = 1, todoTasksItems = listOf(taskItem.copy(id = 1, parentListId = 1)))
        )
        val expectedTaskList2 = listOf(
            taskList.copy(
                id = 2,
                isActive = false,
                title = "job",
                todoTasksItems = listOf(taskItem.copy(id = 2, parentListId = 2, title = "ride to job"))
            )
        )
        // Act
        taskListRepository.importAll(expectedTaskList, clearDb = false)
        taskListRepository.importAll(expectedTaskList2, clearDb = false)
        // Assert
        val importedTaskList = taskListRepository.getActive()
        val allLists = mutableListOf<DataResult<List<TaskList>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            taskListRepository.getAll().toList(allLists)
        }
        Assertions.assertEquals(expectedTaskList.firstOrNull(), importedTaskList)
        val successResult = allLists[1] as? DataResult.Success
        Assertions.assertNotNull(successResult)
        Assertions.assertEquals(expectedTaskList2, successResult?.data?.filter { !it.isActive })
    }

    private fun createTestTaskItem() = TaskItem(
        id = 3,
        title = "shopping",
        parentListId = 2,
        isDone = false,
        content = "",
        isValid = true,
        priority = TaskPriority.Low,
        createdAt = LocalDateTime.now(),
        validUntil = LocalDateTime.now().plusDays(1)
    )

    private fun createTestTaskList(taskItem: TaskItem) = TaskList(
        id = 2,
        title = "test default",
        allTasksCount = 1,
        doneTasksCount = 0,
        doneTasksItems = emptyList(),
        todoTasksCount = 1,
        todoTasksItems = listOf(
            taskItem
        ),
        isActive = true,
    )
}
