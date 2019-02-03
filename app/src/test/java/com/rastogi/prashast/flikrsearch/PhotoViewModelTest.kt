package com.rastogi.prashast.flikrsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rastogi.prashast.flikrsearch.viewmodel.FlikrViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class PhotoViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Test
    fun testPaginateQuery() {
        val viewModel = FlikrViewModel()

        viewModel.page = 1
        viewModel.query = "uber"

        //increase page test query
        viewModel.paginateQuery("uber")
        Assert.assertEquals(2, viewModel.page)

        //increase page test with null
        viewModel.paginateQuery(null)
        Assert.assertEquals(3, viewModel.page)

        //reset page test with new query
        viewModel.paginateQuery("uber eats")
        Assert.assertEquals(1, viewModel.page)


    }
}