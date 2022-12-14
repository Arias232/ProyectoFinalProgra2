package com.example.proyectofinal2.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.proyectofinal2.app.MyApp;
import com.example.proyectofinal2.data.local.MovieRoomDatabase;
import com.example.proyectofinal2.data.local.dao.MovieDao;
import com.example.proyectofinal2.data.local.entity.MovieEntity;
import com.example.proyectofinal2.data.network.NetworkBoundResource;
import com.example.proyectofinal2.data.network.Resource;
import com.example.proyectofinal2.data.remote.ApiConstants;
import com.example.proyectofinal2.data.remote.MovieApiService;
import com.example.proyectofinal2.data.remote.RequestInterceptor;
import com.example.proyectofinal2.data.remote.model.MoviesResponse;


import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieRepository {

    private final MovieApiService movieApiService;
    private final MovieDao movieDao;

    public MovieRepository() {
        // Local > ROOM
        MovieRoomDatabase movieRoomDatabase = Room.databaseBuilder(
                MyApp.getContext(),
                MovieRoomDatabase.class,
                "db_movies"
        ).build();
        movieDao = movieRoomDatabase.getMovieDao();


        // RequestInterceptor: incluir en la cabecera (URL) de la
        // petición el TOKEN o API_KEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new RequestInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();


        // Remote > Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);

    }

    public LiveData<Resource<List<MovieEntity>>> getPopularMovies() {
        // Tipo que devuelve Room (BD local), Tipo que devuelve la API con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>() {

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                // los datos que dispongamos en Room, en la BD local
                return movieDao.loadMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                // obtenemos los datos de la API remota
                return movieApiService.loadPopularMovies();
            }
        }.getAsLiveData();
    }
}
