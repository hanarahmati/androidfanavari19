package co.fanavari.myapplication.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.fanavari.myapplication.api.UnsplashApi
import co.fanavari.myapplication.data.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException

private const val UNSAPLSH_STARTING_PAGE_INDX = 1
class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
): PagingSource<Int, UnsplashPhoto> (){

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let {
            anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1) }
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSAPLSH_STARTING_PAGE_INDX

        return try {
            val resoponse = unsplashApi.searchPhoto(query, position, params.loadSize)
            val photos = resoponse.results

            LoadResult.Page(
                data = photos,
                prevKey = if(position == UNSAPLSH_STARTING_PAGE_INDX) null else position - 1,
                nextKey = if(photos.isEmpty()) null else position + 1
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (ex: HttpException){
            LoadResult.Error(ex)
        }
    }
}
