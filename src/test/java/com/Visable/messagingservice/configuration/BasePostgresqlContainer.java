package com.Visable.messagingservice.configuration;

import org.testcontainers.containers.PostgreSQLContainer;

public class BasePostgresqlContainer extends PostgreSQLContainer<BasePostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";
    private static BasePostgresqlContainer container;

    private BasePostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BasePostgresqlContainer getInstance() {
        if (container == null) {
            container = new BasePostgresqlContainer()
                    .withDatabaseName("messaging-service")
                    .withUsername("postgres")
                    .withPassword("root");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
