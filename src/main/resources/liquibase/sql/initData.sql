--liquibase formatted sql

--changeSet skryagin:1
INSERT INTO personality_data("driver_license_number", "last_name", "name", "passport_number", "patronymic", "phone")
VALUES ('555 555', 'Герогиев', 'Герогий', '5555 555555', 'Герогиевич', '555 555 555');

INSERT INTO usrs("username", "password", "active", "personality_data_id")
VALUES ('q', 'q', 'true', 1);

INSERT INTO user_roles("user_id", "roles")
VALUES ('1', 'ROLE_ADMIN');

INSERT INTO public.cars (active, car_body, color, engine, gearbox, manufacturer, model, plate, power, price,
                         wheel_drive, "year")
VALUES (true, 'CAR_BODY_SUV', 'Черный', 'ENGINE_DIESEL', 'AUTO', 'Toyota', 'Land Cruiser Prado', 'А001АА154', 250,
        12000, 'ALL_WHEEL_DRIVE', 2023),
       (true, 'CAR_BODY_WAGON', 'Серый', 'ENGINE_PETROL', 'AUTO', 'Subaru', 'Outback', 'А002АА154', 250, 12000,
        'ALL_WHEEL_DRIVE', 2023),
       (true, 'CAR_BODY_CROSSOVER', 'Белый', 'ENGINE_PETROL', 'AUTO', 'KIA', 'Sorento', 'А003АА154', 170, 8000,
        'FRONT_WHEEL_DRIVE', 2023),
       (true, 'CAR_BODY_CROSSOVER', 'Черный', 'ENGINE_PETROL', 'AUTO', 'Mazda', 'CX-5', 'А004АА154', 170, 8000,
        'FRONT_WHEEL_DRIVE', 2023);

INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (4, 'CLASS_COMFORT');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (4, 'CLASS_SUV');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (3, 'CLASS_COMFORT');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (3, 'CLASS_SUV');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (2, 'CLASS_COMFORT');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (2, 'CLASS_SUV');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (1, 'CLASS_COMFORT');
INSERT INTO public.cars_classifications (car_id, classifications)
VALUES (1, 'CLASS_SUV');

INSERT INTO public.personality_data (driver_license_number, last_name, name, passport_number, patronymic, phone)
VALUES ('555 555', 'Герогиев', 'Герогий', '5555 55555', 'Герогиевич', '9999999');
