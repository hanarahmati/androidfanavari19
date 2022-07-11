package co.fanavari.myapplication.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.fanavari.myapplication.api.UnsplashApi
import co.fanavari.myapplication.data.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1
class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String)
    : PagingSource<Int, UnsplashPhoto>() {


    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let {
            anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try{
            val response = unsplashApi.searchPhoto(query, position, params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if( position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if( photos.isEmpty()) null else position + 1
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (ex: HttpException){
            LoadResult.Error(ex)
        }
    }


}
