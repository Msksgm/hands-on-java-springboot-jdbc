package com.example.hands_on_java_springboot_jdbc.integration.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class DbConnection {

    private static final int SEQUENCE_VALUE = 10000;

    /**
     * テスト時に DB 設定をするメソッド
     *
     * @return DataSource
     */
    public static DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/sample-db");
        hikariConfig.setUsername("sample-user");
        hikariConfig.setPassword("sample-pass");
        hikariConfig.setConnectionTimeout(500L);
        hikariConfig.setAutoCommit(true);
        hikariConfig.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        hikariConfig.setPoolName("realworldPool01");
        hikariConfig.setMaximumPoolSize(10);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * テスト時にテーブルのシーケンスを設定するメソッド
     * 全てのテスト実行前に呼び出すことで、該当カラムの初期値が固定される
     */
    public static void resetSequence() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
        String sql = "SELECT setval('articles_id_seq', " + SEQUENCE_VALUE + ");";
        // シーケンスをリセット
        namedParameterJdbcTemplate.queryForList(sql, new MapSqlParameterSource());
    }
}
