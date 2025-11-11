package org.example;

import org.example.persistence.ObjectCollection;
import org.example.persistence.ObjectCollectionRegistry;
import org.example.persistence.PersistenceContext;
import org.example.persistence.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        /*try {
            ObjectCollection<Teacher> objectCollection = ObjectCollectionRegistry.getCollection(Teacher.class);
            Teacher teacher = new Teacher();
            teacher.setId(UUID.randomUUID());
            teacher.setName("Mr. Smith");

            List<Student> students = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Student student = new Student();
                student.setId(UUID.randomUUID());
                student.setName("Student " + i);
                students.add(student);
            }

            teacher.setStudents(students);
            //objectCollection.save(teacher);

            Teacher teacher1 = new Teacher();
            teacher1.setId(UUID.randomUUID());
            teacher1.setName("Ms. Johnson");
            teacher1.setStudents(students);
            //objectCollection.save(teacher1);

            objectCollection.deleteById(UUID.fromString("d5b8093c-2c05-49bd-bd83-55fd190de9b2"));
            PersistenceContext.printContext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}