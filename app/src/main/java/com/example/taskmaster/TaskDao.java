package com.example.taskmaster;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM Task WHERE id = :id")
    Task getStudentById(Long id);

    @Insert
    Long insertStudent(Task student);
}
