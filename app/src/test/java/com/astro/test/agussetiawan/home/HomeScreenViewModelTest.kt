package com.astro.test.agussetiawan.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.astro.test.agussetiawan.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelTest{

    @get:Rule
    val instantExecutorRule =  InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

}
