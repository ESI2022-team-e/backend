TRUNCATE cars,roles,users,rentals,requests CASCADE;


INSERT INTO cars (id, mark, model, nr_of_seats, transmission_type, fuel_type, daily_cost, year, licence_plate)
VALUES ('a81bc81b-dead-4e5d-abff-90865d1e13b1', 'Ford', 'Focus 1.6 Aut', 5, 'Automatic', 'Petrol', 71, 2002, '123 ABC'),
       ('c8203520-c8b0-11ec-9d64-0242ac120002', 'Mercedes-Benz', 'C 220 AMG', 5, 'Automatic', 'Diesel', 50, 2012,
        '456 DEF');

INSERT INTO roles(id, name)
VALUES (1, 'ROLE_CUSTOMER'),
       (2, 'ROLE_MANAGER');

INSERT INTO users(dtype, id, username, email, password)
VALUES ('User', 3, 'manager1', 'manager1@email.com', '$2a$10$ldLnBKd/8B0tjRwVxFeAO.xyi/NCBTRjmEuFQ8udAh43t6J6mX4Ua');

INSERT INTO users(dtype, id, username, email, password,first_name, last_name, date_of_birth,drivers_license_number,working_permit_number,identification_document_number)
VALUES ('Customer', 1, 'customer1', 'customer1@email.com', '$2a$10$o3pbWzJM3cHq2O//i1oyduYarn6eMytd1M1bS9Buzn/27J0tFnrsS','John','Doe','1989-01-10','1234','1234','1234'),
       ('Customer', 2, 'customer2', 'customer2@email.com', '$2a$10$o3pbWzJM3cHq2O//i1oyduYarn6eMytd1M1bS9Buzn/27J0tFnrsS','Jane','Doe','1984-05-22','2345','2345','2345');
INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 2);

INSERT INTO requests(id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id,
                     customer_id)
VALUES ('a82bc31b-dead-6a5d-ad65-90865d1e13b2', '2022-01-10T17:09:42.411', '2022-01-10T17:09:42.411', 'Lõunakeskus',
        'Kesklinn', 1, 'a81bc81b-dead-4e5d-abff-90865d1e13b1', 1);

INSERT INTO requests(id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id,
                     customer_id)
VALUES ('d22be32f-dead-6a5d-ad65-90865d1e13b2', '2022-05-22T17:09:42.411', '2022-05-28T17:09:42.411', 'Lõunakeskus',
        'Kesklinn', 1, 'c8203520-c8b0-11ec-9d64-0242ac120002', 1);


INSERT INTO rentals (id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id,
                     customer_id)
VALUES ('a81bc81b-dead-6e5d-ad75-90865d1e13b1', '2022-01-10T17:09:42.411', '2022-01-10T17:09:42.411', 'Lõunakeskus',
        'Kesklinn', 1, 'a81bc81b-dead-4e5d-abff-90865d1e13b1', 1);

INSERT INTO rentals (id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id,
                     customer_id)
VALUES ('a81bc81b-abcd-6e5d-ad75-90865d1e13b1', '2022-04-12T17:09:42.411', '2022-04-17T17:09:42.411', 'Delta',
        'Maarjamõisa', 0, 'a81bc81b-dead-4e5d-abff-90865d1e13b1', 2);

INSERT INTO rentals (id, pickup_datetime, dropoff_datetime, pickup_location, dropoff_location, status, car_id,
                     customer_id)
VALUES ('a81bc81b-ffff-6e5d-ad75-90865d1e13b1', '2022-03-18T17:09:42.411', '2022-03-20T17:09:42.411', 'Riia',
        'Anne', 1, 'a81bc81b-dead-4e5d-abff-90865d1e13b1', 1);

INSERT INTO invoices (id, date, status, price, rental_id, customer_id)
VALUES ('dd06ca3f-613e-49c2-ae62-ab9d3f455194', '2022-05-05T17:19:23.411', 0, 450.0, 'a81bc81b-abcd-6e5d-ad75-90865d1e13b1',
        2);

INSERT INTO invoices (id, date, status, price, rental_id, customer_id)
VALUES ('dd06ca3f-614e-49c2-ae62-ab9d3f455194', '2022-03-18T17:09:42.411', 0, 645.2, 'a81bc81b-ffff-6e5d-ad75-90865d1e13b1',
        1);
