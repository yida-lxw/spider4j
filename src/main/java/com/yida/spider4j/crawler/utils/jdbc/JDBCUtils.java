package com.yida.spider4j.crawler.utils.jdbc;

import com.yida.spider4j.crawler.utils.common.GsonUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JDBCUtils {
    public static void main(String[] args) {
        /*User user1 = new User(1, "Jack", 35);
        User user2 = new User(2, "Tom", 32);
        User user3 = new User(3, "Hunter", 25);
        User user4 = new User(4, "Jerry", 27);
        User user5 = new User(5, "John", 21);
        User user6 = new User(6, "morohe", 29);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
        Map<String, Object> dataMap = SQLBuilder.buildBatchInsertSQL(userList, true);
        String insertSQL = dataMap.get("sql").toString();
        Object[][] params = (Object[][])dataMap.get("params");
        boolean result = JDBCUtils.insertBatch(insertSQL, params, true);
        System.out.println("batch insert successfully?" + result);*/

        User user = JDBCUtils.findById(129, User.class, new BeanHandler<User>(User.class), true);
        System.out.println("User Info:" + GsonUtils.getInstance().beanToString(user));

        String querySQL = "select * from t_user where id > ?";
        List<User> userList = JDBCUtils.queryList(querySQL, new BeanListHandler<>(User.class), new Object[] {100},
                true);
        System.out.println("userList:" + GsonUtils.getInstance().beanToString(userList));

    }

    private static Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

    private static DataSource dataSource;

    private static QueryRunner queryRunner;


    /**
     * 事物自动提交：默认关闭
     */
    private static boolean autoCommit = false;


    static {
        HikariConfig config = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(config);
        queryRunner = new TxQueryRunner(dataSource);
    }

    /**
     * @description MySQL 查询操作,返回List<T>
     * @author yida
     * @date 2023-04-26 10:48:02
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return {@link Long}
     */
    public static Long queryCount(String sql, Object[] params,
                                        boolean closeConnection) {
        Long result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            result = queryRunner.query(connection, sql, new ScalarHandler<Long>(), params);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    /**
     * @description MySQL 查询操作,返回List<T>
     * @author yida
     * @date 2023-04-26 10:48:02
     * @param sql
     * @param resultSetHandler
     * @param params
     * @param closeConnection
     *
     * @return {@link T}
     */
    public static <T> List<T> queryList(String sql, ResultSetHandler<List<T>> resultSetHandler, Object[] params,
                              boolean closeConnection) {
        List<T> result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            result = queryRunner.query(connection, sql, resultSetHandler, params);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    /**
     * @description MySQL 查询操作
     * @author yida
     * @date 2023-04-26 10:48:02
     * @param sql
     * @param resultSetHandler
     * @param params
     * @param closeConnection
     *
     * @return {@link T}
     */
    public static <T> T query(String sql, ResultSetHandler<T> resultSetHandler, Object[] params,
                              boolean closeConnection){
        T result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            result = queryRunner.query(connection, sql, resultSetHandler, params);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    /**
     * @description 批量更新
     * @author yida
     * @date 2023-04-27 16:20:09
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return boolean
     */
    public static boolean updateBatch(String sql, Object[][] params, boolean closeConnection) {
        int[] result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            openTransaction(connection);
            result = queryRunner.batch(connection, sql, params);
            commitQuietly(connection);
        } catch (Exception e) {
            rollbackQuietly(connection);
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        if(result != null && result.length > 0) {
            boolean success = true;
            for(Integer num : result) {
                if(num <= 0) {
                    success = false;
                    break;
                }
            }
            return success;
        } else {
            return false;
        }
    }

    /**
     * @description MySQL 更新操作
     * @author yida
     * @date 2023-04-26 10:53:23
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return boolean
     */
    public static boolean update(String sql, Object[] params, boolean closeConnection) {
        int result = updateAndGetAffectsCount(sql, params, closeConnection);
        return result > 0;
    }

    /**
     * @description MySQL 更新操作
     * @author yida
     * @date 2023-04-26 10:53:23
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return int
     */
    public static int updateAndGetAffectsCount(String sql, Object[] params, boolean closeConnection) {
        int result = 0;
        Connection connection = null;
        try {
            connection = getConnection();
            openTransaction(connection);
            result = queryRunner.update(connection, sql, params);
            commitQuietly(connection);
        } catch (Exception e) {
            rollbackQuietly(connection);
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    public static <P>P insertAndGetId(String sql, Object[] params, String primaryFieldName, Class<P> idFieldType,
                             boolean closeConnection){
        P result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            openTransaction(connection);
            result = queryRunner.insert(connection, sql, new GeneratedKeysHandler<P>(primaryFieldName, idFieldType),
                    params);
            commitQuietly(connection);
        } catch (Exception e) {
            rollbackQuietly(connection);
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    /**
     * @description MySQL insert操作
     * @author yida
     * @date 2023-04-26 11:07:01
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return boolean  insert是否成功
     */
    public static boolean insert(String sql, Object[] params, boolean closeConnection){
        int affectsCount = updateAndGetAffectsCount(sql, params, closeConnection);
        return affectsCount > 0;
    }

    /**
     * @description MySQL delete where id in(?,?,?)操作
     * @author yida
     * @date 2023-04-26 11:07:01
     * @param sql
     * @param idList
     * @param closeConnection
     *
     * @return boolean  delete是否成功
     */
    public static <P>boolean deleteByIds(String sql, List<P> idList, boolean closeConnection){
        int affectsCount = updateAndGetAffectsCount(sql, idList.toArray(), closeConnection);
        return affectsCount > 0;
    }

    /**
     * @description MySQL delete操作
     * @author yida
     * @date 2023-04-26 11:07:01
     * @param sql
     * @param id
     * @param closeConnection
     *
     * @return boolean  delete是否成功
     */
    public static <P>boolean deleteById(String sql, P id, boolean closeConnection){
        int affectsCount = updateAndGetAffectsCount(sql, new Object[] {id}, closeConnection);
        return affectsCount > 0;
    }

    /**
     * @description MySQL delete操作
     * @author yida
     * @date 2023-04-26 11:07:01
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return boolean  delete是否成功
     */
    public static boolean delete(String sql, Object[] params, boolean closeConnection){
        int affectsCount = updateAndGetAffectsCount(sql, params, closeConnection);
        return affectsCount > 0;
    }

    /**
     * @description 批量插入
     * @author yida
     * @date 2023-04-26 12:02:07
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return boolean  批量插入是否成功
     */
    public static <T> boolean insertBatch(String sql, Object[][] params, boolean closeConnection) {
        List<T> result = insertBatchAndGetIds(sql, params, closeConnection);
        if(null == result || result.size() <= 0) {
            return false;
        }
        for(T t : result) {
            if(Long.valueOf(t.toString()) <= 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * @description 批量插入
     * @author yida
     * @date 2023-04-26 12:02:07
     * @param sql
     * @param params
     * @param closeConnection
     *
     * @return List<T>
     */
    public static <T> List<T> insertBatchAndGetIds(String sql, Object[][] params, boolean closeConnection){
        List<T> result = null;
        Connection connection = null;
        try {
            connection = getConnection();
            openTransaction(connection);
            result = queryRunner.insertBatch(connection, sql, new ColumnListHandler<>(), params);
            commitQuietly(connection);
        } catch (Exception e) {
            rollbackQuietly(connection);
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return result;
    }

    /**
     * @description 根据主键id列表进行查询
     * @author yida
     * @date 2023-04-26 11:05:32
     * @param idList
     * @param findByIdsSQL
     * @param resultSetHandler
     * @param closeConnection
     *
     * @return {@link T}
     */
    public static <P, T>List<T> findByIds(List<P> idList, String findByIdsSQL,
                                          ResultSetHandler<List<T>> resultSetHandler,
                                   boolean closeConnection) {
        List<T> entityList = null;
        Connection connection = null;
        try {
            connection = getConnection();
            entityList = queryRunner.query(connection, findByIdsSQL, resultSetHandler, idList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return entityList;
    }

    /**
     * @description 根据主键id进行查询
     * @author yida
     * @date 2023-04-26 11:05:32
     * @param id
     * @param findByIdSQL
     * @param resultSetHandler
     * @param closeConnection
     *
     * @return {@link T}
     */
    public static <P, T>T findById(P id, String findByIdSQL, ResultSetHandler<T> resultSetHandler,
                                   boolean closeConnection) {
        T entity = null;
        Connection connection = null;
        try {
            connection = getConnection();
            entity = queryRunner.query(connection, findByIdSQL, resultSetHandler, new Object[] {id});
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return entity;
    }

    /**
     * @description 根据主键id进行查询
     * @author yida
     * @date 2023-04-26 11:05:32
     * @param id
     * @param entityClass
     * @param resultSetHandler
     * @param closeConnection
     *
     * @return {@link T}
     */
    public static <P, T>T findById(P id, Class<T> entityClass, ResultSetHandler<T> resultSetHandler,
                                boolean closeConnection) {
        T entity = null;
        Connection connection = null;
        try {
            connection = getConnection();
            String findByIdSQL = SQLBuilder.buildFindByIdSQL(entityClass);
            entity = queryRunner.query(connection, findByIdSQL, resultSetHandler, new Object[] {id});
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(closeConnection) {
                closeQuietly(connection);
            }
        }
        return entity;
    }

    public static Connection getConnection() throws SQLException {
        // 从数据源中获取数据库连接
        Connection connection = dataSource.getConnection();
        // 设置事物是否自动提交
        connection.setAutoCommit(isAutoCommit());
        return connection;
    }

    /**
     * 开启事物
     *
     * @throws SQLException
     */
    public static void openTransaction(Connection connection) throws SQLException {
        if (null != connection) {
            // 设置事物是否自动提交
            connection.setAutoCommit(isAutoCommit());
        }
    }

    /**
     * 事物回滚
     *
     * @throws SQLException
     */
    public static void rollback(Connection connection) throws SQLException {
        if (null != connection) {
            connection.rollback();
        }
    }

    /**
     * 事物回滚，隐藏事物操作时发生的SQLException
     */
    public static void rollbackQuietly(Connection connection) {
        try {
            rollback(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 事物回滚并关闭<code>Connection</code>
     *
     * @throws SQLException
     */
    public static void rollbackAndClose(Connection connection) throws SQLException {
        try {
            // 事物回滚
            rollback(connection);
        } finally {
            // 关闭连接
            close(connection);
        }
    }

    /**
     * 事物回滚并关闭<code>Connection</code>，隐藏事物操作时发生的SQLException
     */
    public static void rollbackAndCloseQuietly(Connection connection) {
        try {
            rollbackAndClose(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 事物提交
     *
     * @throws SQLException
     */
    public static void commit(Connection connection) throws SQLException {
        if (null != connection) {
            connection.commit();
        }
    }

    /**
     * 事物提交，隐藏事物操作时发生的SQLException
     */
    public static void commitQuietly(Connection connection) {
        try {
            commit(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 事物提交并关闭<code>Connection</code>
     *
     * @throws SQLException
     */
    public static void commitAndClose(Connection connection) throws SQLException {
        try {
            // 事物提交
            commit(connection);
        } finally {
            // 关闭连接
            close(connection);
        }
    }

    /**
     * 事物提交并关闭<code>Connection</code>，隐藏事物操作时发生的SQLException
     */
    public static void commitAndCloseQuietly(Connection connection) {
        try {
            commitAndClose(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void close(Connection connection) throws SQLException {
        if (null != connection) {
            // 关闭连接
            connection.close();
        }
    }

    public static void releaseConnection(Connection connection) throws SQLException {
        close(connection);
    }

    /**
     * 关闭<code>Connection</code>，隐藏事物操作时发生的SQLException
     */
    public static void closeQuietly(Connection connection) {
        try {
            close(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        JDBCUtils.dataSource = dataSource;
    }

    public static boolean isAutoCommit() {
        return autoCommit;
    }

    public static void setAutoCommit(boolean autoCommit) {
        JDBCUtils.autoCommit = autoCommit;
    }

    public static QueryRunner getQueryRunner() {
        return queryRunner;
    }
}
