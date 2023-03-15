package dandi.dandi.common;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {


    private static final List<String> TABLE_NAMES = List.of("member", "refresh_token", "push_notification",
            "post", "additional_feeling_index");
    private static final String CAMEL_REGEX = "([a-z])([A-Z]+)";
    private static final String SNAKE_REPLACEMENT = "$1_$2";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> toSnakeCase(e.getName()))
                .collect(Collectors.toList());
    }

    private String toSnakeCase(String entityName) {
        return entityName.replaceAll(CAMEL_REGEX, SNAKE_REPLACEMENT)
                .toLowerCase();
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
