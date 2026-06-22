CREATE SCHEMA IF NOT EXISTS scooters;

DROP TABLE IF EXISTS scooters.tasks CASCADE;
DROP TABLE IF EXISTS scooters.trips CASCADE;
DROP TABLE IF EXISTS scooters.scooters CASCADE;
DROP TABLE IF EXISTS scooters.models CASCADE;
DROP TABLE IF EXISTS scooters.routes CASCADE;
DROP TABLE IF EXISTS scooters.zones CASCADE;
DROP TABLE IF EXISTS scooters.users CASCADE;
DROP TABLE IF EXISTS scooters.tariffs CASCADE;
DROP TABLE IF EXISTS scooters.juicers CASCADE;

CREATE TABLE scooters.juicers (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  full_name VARCHAR(500) NOT NULL,
  role VARCHAR(50) DEFAULT NULL,
  CONSTRAINT chk_role_real CHECK
((role = 'repairman') OR (role = 'constellator') OR (role ='administrator'))
);

CREATE TABLE scooters.models (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  brand VARCHAR(100) NOT NULL,
  model VARCHAR(150) NOT NULL,
  generation SMALLINT,
  model_coef NUMERIC(3,2) NOT null DEFAULT 1,
  CONSTRAINT chk_generation CHECK (generation > -1 AND generation < 50),
  CONSTRAINT chk_model_coef CHECK (model_coef > 0 AND model_coef < 1)
);

CREATE TABLE scooters.zones (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  address VARCHAR(255) NOT NULL,
  parking_available BOOLEAN NOT NULL
);

CREATE TABLE scooters.tariffs (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price_per_minute NUMERIC(12,2) NOT NULL,
  CONSTRAINT chk_price_per_minute CHECK (price_per_minute > 0 AND price_per_minute < 10)
);

CREATE TABLE scooters.users (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  tariff_id INT,
  full_name VARCHAR(500) NOT NULL,
  CONSTRAINT fk_user_tariff FOREIGN KEY (tariff_id) REFERENCES scooters.tariffs(id) ON DELETE RESTRICT
);

CREATE TABLE scooters.scooters (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  model_id INT NOT NULL,
  location_id INT NOT NULL,
  charge_level SMALLINT NOT NULL,
  CONSTRAINT fk_scooter_model FOREIGN KEY (model_id) REFERENCES scooters.models(id) ON DELETE RESTRICT,
  CONSTRAINT fk_scooter_location FOREIGN KEY (location_id) REFERENCES scooters.zones(id) ON DELETE RESTRICT,
  CONSTRAINT chk_charge_level CHECK (charge_level > -1 AND charge_level < 101)
);

CREATE TABLE scooters.routes (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  start_zone_id INT NOT NULL,
  end_zone_id INT NOT NULL,
  distance SMALLINT NOT NULL,
  speed_limit SMALLINT,
  CONSTRAINT fk_route_start FOREIGN KEY (start_zone_id) REFERENCES scooters.zones(id) ON DELETE RESTRICT,
  CONSTRAINT fk_route_end FOREIGN KEY (end_zone_id) REFERENCES scooters.zones(id) ON DELETE RESTRICT,
  CONSTRAINT chk_distance CHECK (distance > 0 AND distance < 100),
  CONSTRAINT chk_speed_limit CHECK (speed_limit > 9 AND speed_limit < 60)
);

CREATE TABLE scooters.trips (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  scooter_id INT NOT NULL,
  user_id INT NOT NULL,
  route_id INT NOT NULL,
  start_time TIMESTAMP NOT NULL DEFAULT NOW(),
  duration INTEGER NOT NULL,
  final_distance INTEGER NOT NULL,
  cost NUMERIC(12,2) NOT NULL,
  CONSTRAINT fk_trip_scooter FOREIGN KEY (scooter_id) REFERENCES scooters.scooters(id) ON DELETE RESTRICT,
  CONSTRAINT fk_trip_user FOREIGN KEY (user_id) REFERENCES scooters.users(id) ON DELETE RESTRICT,
  CONSTRAINT fk_trip_route FOREIGN KEY (route_id) REFERENCES scooters.routes(id) ON DELETE RESTRICT,
  CONSTRAINT chk_duration CHECK (duration > 0 AND duration <= 86400),  -- от 1 секунды до 24 часов
  CONSTRAINT chk_cost CHECK (cost > 0),
  CONSTRAINT chk_start_time_not_future CHECK (start_time <= CURRENT_TIMESTAMP + INTERVAL '1 minute'),
  CONSTRAINT chk_final_distance CHECK (final_distance > 0 AND final_distance <= 200000)
);

CREATE TABLE scooters.tasks (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  scooter_id INT NOT NULL,
  juicer_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  description VARCHAR(1500) NOT NULL,
  status BOOLEAN NOT NULL,
  CONSTRAINT fk_task_scooter FOREIGN KEY (scooter_id) REFERENCES scooters.scooters(id) ON DELETE RESTRICT,
  CONSTRAINT fk_task_juicers FOREIGN KEY (juicer_id) REFERENCES scooters.juicers(id) ON DELETE RESTRICT,
  CONSTRAINT chk_created_at CHECK (created_at <= CURRENT_TIMESTAMP + INTERVAL '1 minute')
);