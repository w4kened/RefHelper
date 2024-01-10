package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.UsersAidsEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



//@DataJpaTest // Provides a configured in-memory database and JPA settings for testing

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(TestDatabaseConfiguration.class)
public class UsersAidsRepositoryIntegrationTest {

    @Autowired
    private UsersAidsRepository usersAidsRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    @Sql("/data.sql") // Use this annotation to execute SQL scripts to populate the database
    void testFindRequestedAidsByAidIds() {
        // Given
        List<Long> aidIds = Arrays.asList(1L, 2L); // Add the desired aid IDs to test

        // When
        List<UsersAidsEntity> requestedAids = usersAidsRepository.findRequestedAidsByAidIds(aidIds);

        // Then
        // Add assertions to verify the results based on your test data
        assertEquals( 2, requestedAids.size());
        // Add more assertions as needed to validate the retrieved data
    }
}