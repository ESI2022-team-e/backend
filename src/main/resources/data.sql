TRUNCATE cars,reservations RESTART IDENTITY;

INSERT INTO cars (id,mark, model, nr_of_seats, transmission_type, fuel_type, daily_cost, year, licence_plate) VALUES
    ('a81bc81b-dead-4e5d-abff-90865d1e13b1','Ford','Focus 1.6 Aut',5,'Automatic','Petrol',71,2002,'123 ABC'),
    ('c8203520-c8b0-11ec-9d64-0242ac120002','Mercedes-Benz','C 220 AMG',5,'Automatic','Diesel',50,2012,'456 DEF');