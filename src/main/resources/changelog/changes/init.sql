	CREATE USER postgres;
CREATE DATABASE messaging-service
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
GRANT ALL PRIVILEGES ON DATABASE messaging-service TO postgres;