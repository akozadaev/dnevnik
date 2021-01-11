package ru.tsu.dnevnik.db.interfaces;

import ru.tsu.dnevnik.db.models.StudentUI;

import java.util.List;

/**
 * Created by Alexey on 01.10.2016.
 */
public interface DbStudents {

    StudentUI getStudentUI(String login);

    List<StudentUI> getAllStudents();

    int createStudent(String name);
 }
