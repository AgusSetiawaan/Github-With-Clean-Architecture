package com.agussetiawan.application.githubcleanarchitecture.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.agussetiawan.application.githubcleanarchitecture.core.DataDummy
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.DataItem
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.SortType
import com.agussetiawan.application.githubcleanarchitecture.core.domain.repository.IGithubUserRepository
import com.agussetiawan.application.githubcleanarchitecture.core.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GithubUserInteractorTest{

    @get:Rule
    val instantExecutorRule =  InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: IGithubUserRepository

    private lateinit var interactor: GithubUserInteractor

    @Before
    fun setUp(){
        interactor = GithubUserInteractor(repository)
    }

    @Test
    fun `when Get Github User Return should not null`() = runTest {
        val dummyGithubUser = DataDummy.generateDummyGithubUser()
        val data: PagingData<GithubUser> = UserPagingSource.snapshot(dummyGithubUser)
        val dataFlow = flowOf(data)
        Mockito.`when`(repository.searchUsers("query", "order", SortType.ASC.desc)).thenReturn(
            dataFlow)
        val expectedResult = listOf(DataItem.Header(SortType.ASC))+dummyGithubUser.map { DataItem.UserItem(it) }

        val actualResult = interactor.searchUsers("query", "order", SortType.ASC)

        val differ = AsyncPagingDataDiffer(
            diffCallback = DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualResult.first())

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(expectedResult, differ.snapshot())
        Assert.assertEquals(expectedResult.size, differ.snapshot().size)
        Assert.assertEquals(expectedResult[0].id, differ.snapshot()[0]?.id)
    }

}

class UserPagingSource: PagingSource<Int, Flow<List<GithubUser>>>(){

    companion object{
        fun snapshot(user: List<GithubUser>): PagingData<GithubUser> {
            return PagingData.from(user)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<GithubUser>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<GithubUser>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object: ListUpdateCallback{
    override fun onInserted(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {

    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {

    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {

    }

}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}