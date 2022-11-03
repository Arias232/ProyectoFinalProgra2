package com.example.proyectofinal2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinal2.data.network.Resource;
import com.example.proyectofinal2.data.MovieRepository;
import com.example.proyectofinal2.data.local.entity.MovieEntity;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private final LiveData<Resource<List<MovieEntity>>> popularMovies;
    private MovieRepository movieRepository;

    public MovieViewModel() {
        movieRepository = new MovieRepository();
        popularMovies = movieRepository.getPopularMovies();
    }

    public LiveData<Resource<List<MovieEntity>>> getPopularMovies() { return popularMovies; }
}