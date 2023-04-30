package com.yida.spider4j.crawler.utils.jdbc;

import com.yida.spider4j.crawler.utils.common.StringUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 11:15
 * @description ResultSet转换成JavaBean
 */
public abstract class AbstractBeanHandler<T> implements ResultSetHandler<T> {
    private static Logger logger = LoggerFactory.getLogger(BeanListHandler.class);

    protected Class<T> type;

    public AbstractBeanHandler(Class<T> type) {
        this.type = type;
    }


    @Override
    public T handle(ResultSet rs) throws SQLException {
        T beanInstance = null;
        if (rs.next()) {
            try {
                beanInstance = type.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create an instance of [" + type.getName() +
                        "], Perhaps the class:[{" + type.getName() + "}] does not provide a parameterless constructor",
                        ex);
            }
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                Field currentField = null;
                Field[] fields = type.getDeclaredFields();
                for (Field field : fields) {
                    if(Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    String fieldName = "";
                    if (column == null || StringUtils.isEmpty(column.value())) {
                        fieldName = StringUtils.toSnakeCase(field.getName());
                    } else {
                        fieldName = column.value();
                    }
                    if(columnName.equals(fieldName)) {
                        currentField = field;
                        break;
                    }
                }
                if(null == currentField) {
                    logger.warn("Can't match the field with cloumnName:[{}] in class:[{}]", columnName, type.getName());
                    continue;
                }
                Object columnValue = rs.getObject(i);
                try {
                    String setMethodStuffix = StringUtils.convertFirstLetterToUpperCase(currentField.getName());
                    type.getMethod("set" + setMethodStuffix, columnValue.getClass()).invoke(beanInstance, columnValue);
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to set property " + columnName + " of " + type, ex);
                }
            }
        }
        return beanInstance;
    }
}
