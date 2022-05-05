TRUNCATE cars,roles,users,rentals,requests CASCADE;


INSERT INTO cars (id, mark, model, nr_of_seats, transmission_type, fuel_type, daily_cost, year, licence_plate)
VALUES ('a81bc81b-dead-4e5d-abff-90865d1e13b1', 'Ford', 'Focus 1.6 Aut', 5, 'Automatic', 'Petrol', 71, 2002, '123 ABC'),
       ('c8203520-c8b0-11ec-9d64-0242ac120002', 'Mercedes-Benz', 'C 220 AMG', 5, 'Automatic', 'Diesel', 50, 2012,
        '456 DEF');
INSERT INTO roles(id, name)
VALUES (1, 'ROLE_CUSTOMER'),
       (2, 'ROLE_MANAGER');

INSERT INTO rentals (id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id)
VALUES ('a81bc81b-dead-6e5d-ad75-90865d1e13b1', '2022-01-10T17:09:42.411', '2022-01-10T17:09:42.411', 'Lõunakeskus',
        'Kesklinn', 1, 'a81bc81b-dead-4e5d-abff-90865d1e13b1');

INSERT INTO rentals (id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id)
VALUES ('a81bc81b-abcd-6e5d-ad75-90865d1e13b1', '2022-04-12T17:09:42.411', '2022-04-17T17:09:42.411', 'Delta',
        'Maarjamõisa', 0, 'a81bc81b-dead-4e5d-abff-90865d1e13b1');

INSERT INTO invoices (id, date, status, rental_id)
VALUES ('dd06ca3f-613e-49c2-ae62-ab9d3f455194', '2022-05-05T17:19:23.411', 0,
        'a81bc81b-abcd-6e5d-ad75-90865d1e13b1');