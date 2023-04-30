package com.yida.spider4j.crawler.utils.jdbc;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 17:48
 * @description 返回insert操作后的记录主键ID值
 */
public class GeneratedKeysHandler<P> implements ResultSetHandler<P> {
    private String columnName;

    private Class<P> primaryFieldType;

    public GeneratedKeysHandler(String columnName, Class<P> primaryFieldType) {
        this.columnName = columnName;
        this.primaryFieldType = primaryFieldType;
    }

    @Override
    public P handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            // 获得自增主键
            return rs.getObject(columnName, primaryFieldType);
        }
        return null;
    }
}
