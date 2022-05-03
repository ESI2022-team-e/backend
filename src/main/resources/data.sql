TRUNCATE cars,roles,users,rentals CASCADE;

INSERT INTO cars (id,mark, model, nr_of_seats, transmission_type, fuel_type, daily_cost, year, licence_plate) VALUES
  ('a81bc81b-dead-4e5d-abff-90865d1e13b1','Ford','Focus 1.6 Aut',5,'Automatic','Petrol',71,2002,'123 ABC'),
  ('c8203520-c8b0-11ec-9d64-0242ac120002','Mercedes-Benz','C 220 AMG',5,'Automatic','Diesel',50,2012,'456 DEF');

INSERT INTO roles(id,name) VALUES
  (1,'ROLE_CUSTOMER'),
  (2,'ROLE_MANAGER');

INSERT INTO rentals (id, pickup_date, dropoff_date, pickup_time, dropoff_time, pickup_location, dropoff_location, status, car_id) VALUES
  ('a81bc81b-dead-6e5d-ad75-90865d1e13b1','20.01.2022','22.01.2022','1:00PM','2:00PM','Lõunakeskus','Kesklinn', 1, 'a81bc81b-dead-4e5d-abff-90865d1e13b1');