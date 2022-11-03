package com.example.proyectofinal2.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.proyectofinal2.data.local.dao.MovieDao;
import com.example.proyectofinal2.data.local.entity.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao getMovieDao();
}
