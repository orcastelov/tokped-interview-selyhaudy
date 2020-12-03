package com.tokopedia.maps

import com.tokopedia.maps.model.CountryDto
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


interface MapsService {
    @Headers(
        "x-rapidapi-key: fbacba4899msh92be1e60dff7126p1975bdjsn94eb1bbfcde0",
        "x-rapidapi-host: restcountries-v1.p.rapidapi.com"
    )
    @GET("/all")
    fun getAllCountries(): Observable<List<CountryDto>>

    companion object {
        val mapsService: MapsService by lazy {
            createMapsService()
        }

        private fun createMapsService(): MapsService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .baseUrl("https://restcountries-v1.p.rapidapi.com/")
                    .build()

            return retrofit.create(MapsService::class.java)
        }
    }
}

