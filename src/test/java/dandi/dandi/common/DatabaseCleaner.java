package dandi.dandi.common;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {


    private static final List<String> TABLE_NAMES = List.of("member", "refresh_token", "push_notification",
            "post", "additional_feeling_index", "post_like");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterPropertiesSet() {
    }

    @Transactional
    public void clear() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.flush();
        for (String tableName : TABLE_NAMES) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery(
                            "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1")
                    .executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
