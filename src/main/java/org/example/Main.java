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
            ObjectCollection<Person> personCollection = ObjectCollectionRegistry.getCollection(Person.class);
            ObjectCollection<Car> carCollection = ObjectCollectionRegistry.getCollection(Car.class);

            Person person1 = new Person();
            person1.setId(UUID.randomUUID());
            person1.setName("Alice");

            List<Car> cars = new ArrayList<>();
            for(int i = 0; i < 2; i++) {
                Car car = new Car();
                car.setId(UUID.randomUUID());
                car.setModel("Model " + (i + 1));

                cars.add(car);
            }

            Person person2 = new Person();
            person2.setId(UUID.randomUUID());
            person2.setName("Bob");
            person2.setCars(cars);

            //personCollection.save(person1);
            //personCollection.save(person2);

            System.out.println(personCollection.findAll());
            //personCollection.deleteById(UUID.fromString("40f6b52a-f268-4ac4-bde0-3e05b814958a"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}