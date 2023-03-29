package dandi.dandi.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    private static final String SYS_CONFIG_TABLE_NAME = "sys_config";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    private final List<String> tableNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet rs = databaseMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                addExceptSysConfigTable(rs);
            }
        } catch (SQLException e) {
        }
    }

    private void addExceptSysConfigTable(ResultSet rs) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        if (!tableName.equals(SYS_CONFIG_TABLE_NAME)) {
            tableNames.add(tableName);
        }
    }

    @Transactional
    public void truncate() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.flush();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @Transactional
    public void initializeAutoIncrement() {
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1")
                    .executeUpdate();
        }
    }
}
