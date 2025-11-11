package org.example;

import org.example.persistence.ObjectCollection;
import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        /*try {
            ObjectCollection<Teacher> teacherCollection = ObjectCollectionRegistry.getCollection(Teacher.class);
            ObjectCollection<Student> studentCollection = ObjectCollectionRegistry.getCollection(Student.class);

            Teacher teacher = new Teacher();
            teacher.setId(UUID.randomUUID());
            teacher.setName("Mr. Smith");

            List<Student> students = new ArrayList<>();

            for(int i = 1; i <= 3; i++) {
                Student student = new Student();
                student.setId(UUID.randomUUID());
                student.setName("Student " + i);

                student.setTeacher(teacher);
                studentCollection.save(student);
                students.add(student);
            }
            teacher.setStudents(students);
            teacherCollection.save(teacher);

            System.out.println(teacherCollection.findById(teacher.getId()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}