package com.zeynelinho.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.zeynelinho.artbooktesting.api.RetrofitAPI
import com.zeynelinho.artbooktesting.model.Art
import com.zeynelinho.artbooktesting.model.ImageResponse
import com.zeynelinho.artbooktesting.roomDB.ArtDao
import com.zeynelinho.artbooktesting.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao : ArtDao,
    private val retrofitAPI: RetrofitAPI
    ) : ArtRepositoryInterface{

    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
       return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {

        return try {
            val response = retrofitAPI.imageSearch(imageString)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                }
            }else {
                Resource.error("Error",null)
            }

        }catch (e : Exception) {
            e.printStackTrace()
            Resource.error("No Data",null)
        }
    }
}