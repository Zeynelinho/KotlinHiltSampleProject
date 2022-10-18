package com.zeynelinho.artbooktesting.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zeynelinho.artbooktesting.R
import com.zeynelinho.artbooktesting.api.RetrofitAPI
import com.zeynelinho.artbooktesting.repo.ArtRepository
import com.zeynelinho.artbooktesting.repo.ArtRepositoryInterface
import com.zeynelinho.artbooktesting.roomDB.ArtDao
import com.zeynelinho.artbooktesting.roomDB.ArtDatabase
import com.zeynelinho.artbooktesting.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context)  = Room.databaseBuilder(
        context,ArtDatabase::class.java,"ArtBookDB"
        ).build()

    @Singleton
    @Provides
    fun injectDao(database : ArtDatabase) = database.artDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI() : RetrofitAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo(dao : ArtDao, api : RetrofitAPI) = ArtRepository(dao,api) as ArtRepositoryInterface


    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.image)
                .error(R.drawable.image))



}