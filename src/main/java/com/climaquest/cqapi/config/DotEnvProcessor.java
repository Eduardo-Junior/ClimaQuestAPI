package com.climaquest.cqapi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.*;
import java.util.Properties;

public class DotEnvProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        File envFile = new File(".env");
        if (!envFile.exists()) return;

        Properties props = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) continue;
                int idx = line.indexOf('=');
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                props.setProperty(key, value);
            }
        } catch (IOException e) {
            System.err.println("[DotEnv] Erro ao ler .env: " + e.getMessage());
        }

        environment.getPropertySources().addFirst(
                new PropertiesPropertySource("dotenv", props)
        );
    }
}