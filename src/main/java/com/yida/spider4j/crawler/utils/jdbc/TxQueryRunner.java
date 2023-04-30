package com.yida.spider4j.crawler.utils.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TxQueryRunner extends QueryRunner {
    public TxQueryRunner() {
    }

    public TxQueryRunner(DataSource dataSource) {

    }

    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        int[] result = super.batch(connection, sql, params);
        JDBCUtils.releaseConnection(connection);
        return result;
    }

    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        T result = super.query(connection, sql, rsh, params);
        JDBCUtils.releaseConnection(connection);
        return result;
    }

    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        T result = super.query(connection, sql, rsh);
        JDBCUtils.releaseConnection(connection);
        return result;
    }

    public int update(String sql) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        int result = super.update(connection, sql);
        JDBCUtils.releaseConnection(connection);
        return result;
    }

    public int update(String sql, Object param) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        int result = super.update(connection, sql, param);
        JDBCUtils.releaseConnection(connection);
        return result;
    }

    public int update(String sql, Object... params) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        int result = super.update(connection, sql, params);
        JDBCUtils.releaseConnection(connection);
        return result;
    }
}

