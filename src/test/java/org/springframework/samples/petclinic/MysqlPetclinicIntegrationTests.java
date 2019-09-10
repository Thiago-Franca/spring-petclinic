/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MySQLContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = MysqlPetclinicIntegrationTests.Initializer.class)
@ActiveProfiles("mysql")
public class MysqlPetclinicIntegrationTests {

    @Autowired
    private VetRepository vets;

    @Test
    public void testFindAll() throws Exception {
        vets.findAll();
        vets.findAll(); // served from cache
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static MySQLContainer<?> mysql;

        static {
            mysql = new MySQLContainer<>().withEnv("MYSQL_ROOT_PASSWORD", "petclinic")
                    .withDatabaseName("petclinic");
            mysql.start();
        }

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("spring.datasource.url=jdbc:mysql://localhost:" + mysql.getFirstMappedPort() + "/petclinic")
                    .applyTo(context);
        }

    }
}
