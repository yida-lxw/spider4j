package com.yida.spider4j.crawler.utils.jdbc;

import com.yida.spider4j.crawler.utils.common.GsonUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 08:37
 * @description SQL语句构建器
 */
public class SQLBuilder {
    /**默认主键字段的名称:id*/
    public static final String DEFAULT_ID_FIELD_NAME = "id";

    public static final String SQL_KEY = "sql";

    public static final String PARAMS_KEY = "params";

    public static <T> String buildInsertSQL(T entity) {
        return buildInsertSQL(entity, false);
    }

    /**
     * @description 构建查询所有总记录数的SQL
     * @author yida
     * @date 2023-04-27 11:30:27
     * @param entityClass
     *
     * @return String
     */
    public static <T>String buildSelectTotalCountSQL(Class<T> entityClass) {
        StringBuilder stringBuilder = new StringBuilder();
        String tableName = getTableName(entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        String idFieldName = getIdFieldName(fields);
        stringBuilder.append("SELECT COUNT(").append(idFieldName).append(") FROM ").append(tableName);
        return stringBuilder.toString();
    }

    /**
     * @description 构建查询记录数的SQL
     * @author yida
     * @date 2023-04-27 11:30:27
     * @param orignalSQL
     * @param entityClass
     *
     * @return String
     */
    public static <T>String buildSelectCountSQL(String orignalSQL, Class<T> entityClass) {
        StringBuilder stringBuilder = new StringBuilder();
        if(orignalSQL.indexOf(" FROM ") != -1) {
            orignalSQL = orignalSQL.substring(orignalSQL.indexOf(" FROM "));
        } else if(orignalSQL.indexOf(" from ") != -1) {
            orignalSQL = orignalSQL.substring(orignalSQL.indexOf(" from "));
        }
        if(orignalSQL.indexOf(" LIMIT ") != -1) {
            orignalSQL = orignalSQL.substring(0, orignalSQL.indexOf(" LIMIT "));
        } else if(orignalSQL.indexOf(" limit ") != -1) {
            orignalSQL = orignalSQL.substring(0, orignalSQL.indexOf(" limit "));
        }
        Field[] fields = entityClass.getDeclaredFields();
        String idFieldName = getIdFieldName(fields);
        stringBuilder.append("SELECT COUNT(").append(idFieldName).append(") ").append(orignalSQL);
        return stringBuilder.toString();
    }

    public static <T> String buildInsertSQL(T entity, boolean ignoreDuplicate) {
        Class<T> clazz = (Class<T>) entity.getClass();
        String tableName = getTableName(clazz);
        StringBuilder sqlBuilder = new StringBuilder();
        if(ignoreDuplicate) {
            sqlBuilder.append("INSERT ignore INTO ").append(tableName).append(" (");
        } else {
            sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
        }

        Field[] fields = clazz.getDeclaredFields();
        int fieldSize = 0;
        for (Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers())) {
                ID id = field.getAnnotation(ID.class);
                if(null != id) {
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                String fieldName = "";
                if (column != null && StringUtils.isNotEmpty(column.value())) {
                    fieldName = column.value();
                } else {
                    fieldName = StringUtils.toSnakeCase(field.getName());
                }
                if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                    //属性名与默认主键id名称相同，也会跳过
                    continue;
                }
                sqlBuilder.append(fieldName).append(", ");
                fieldSize++;
            }
        }

        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ' ') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < fieldSize; i++) {
            sqlBuilder.append("?, ");
        }

        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ' ') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    public static <T> Map<String, Object> buildInsertSQLAndParams(T entity) {
        return buildInsertSQLAndParams(entity, false);
    }

    public static <T> Map<String, Object> buildInsertSQLAndParams(T entity, boolean ignoreDuplicate) {
        Class<T> clazz = (Class<T>) entity.getClass();
        String tableName = getTableName(clazz);
        StringBuilder sqlBuilder = new StringBuilder();
        if(ignoreDuplicate) {
            sqlBuilder.append("INSERT ignore INTO ").append(tableName).append(" (");
        } else {
            sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
        }

        List<Object> params = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers())) {
                ID id = field.getAnnotation(ID.class);
                if(null != id) {
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                String fieldName = "";
                if (column != null && StringUtils.isNotEmpty(column.value())) {
                    fieldName = column.value();
                } else {
                    fieldName = StringUtils.toSnakeCase(field.getName());
                }
                if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                    //属性名与默认主键id名称相同，也会跳过
                    continue;
                }
                sqlBuilder.append(fieldName).append(", ");
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    params.add(value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
                }
            }
        }

        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ' ') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < params.size(); i++) {
            sqlBuilder.append("?, ");
        }

        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ' ') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(")");
        String sql = sqlBuilder.toString();
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put(SQL_KEY, sql);
        dataMap.put(PARAMS_KEY, params.toArray());
        return dataMap;
    }

    public static <T> Map<String, Object> buildUpdateByIdBatchSQL(List<T> entityList) {
        String sql = "";
        List<Object[]> parameterList = new ArrayList();
        T firstEntity = entityList.get(0);
        Class<T> clazz = (Class<T>) firstEntity.getClass();
        String tableName = getTableName(clazz);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ").append(tableName).append(" SET ");
        int index = 0;
        for(T entity : entityList) {
            List<Object> params = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            boolean hasId = false;

            for (Field field : fields) {
                if (field.isAnnotationPresent(ID.class)) {
                    hasId = true;
                    continue;
                }
                if(!Modifier.isStatic(field.getModifiers())) {
                    Column column = field.getAnnotation(Column.class);
                    String fieldName = "";
                    if (column != null && StringUtils.isNotEmpty(column.value())) {
                        fieldName = column.value();
                    } else {
                        fieldName = StringUtils.toSnakeCase(field.getName());
                    }
                    if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                        hasId = true;
                        continue;
                    }
                    Object value = null;
                    try {
                        field.setAccessible(true);
                        value = field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
                    }
                    if (value != null) {
                        if(index == 0) {
                            stringBuilder.append(fieldName).append("=?, ");
                        }
                        params.add(value);
                    }
                }
            }
            if (!hasId) {
                throw new IllegalArgumentException("Class " + clazz.getName() + " is missing @Id annotation");
            }
            if(index == 0) {
                if (stringBuilder.charAt(stringBuilder.length() - 1) == ' ') {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                stringBuilder.append(" WHERE ");
            }

            for (Field field : fields) {
                if (!field.isAnnotationPresent(ID.class) && !DEFAULT_ID_FIELD_NAME.equals(field.getName())) {
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                String idFieldName = "";
                if (column == null || StringUtils.isEmpty(column.value())) {
                    idFieldName = field.getName();
                } else {
                    idFieldName = column.value();
                }
                if(index == 0) {
                    stringBuilder.append(idFieldName).append("=?");
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    params.add(value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
                }
                break;
            }
            if(index == 0) {
                sql = stringBuilder.toString();
            }
            Object[] paramArray = params.toArray();
            parameterList.add(paramArray);
            index++;
        }
        Object[][] parameterArray = (Object[][])parameterList.toArray();
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put(SQL_KEY, sql);
        dataMap.put(PARAMS_KEY, parameterArray);
        return dataMap;
    }

    public static <T> Map<String, Object> buildUpdateByIdSQL(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        String tableName = getTableName(clazz);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ").append(tableName).append(" SET ");

        List<Object> params = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        boolean hasId = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(ID.class)) {
                hasId = true;
                continue;
            }
            if(!Modifier.isStatic(field.getModifiers())) {
                Column column = field.getAnnotation(Column.class);
                String fieldName = "";
                if (column != null && StringUtils.isNotEmpty(column.value())) {
                    fieldName = column.value();
                } else {
                    fieldName = StringUtils.toSnakeCase(field.getName());
                }
                if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                    hasId = true;
                    continue;
                }
                Object value = null;
                try {
                    field.setAccessible(true);
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
                }
                if (value != null) {
                    stringBuilder.append(fieldName).append("=?, ");
                    params.add(value);
                }
            }
        }
        if (!hasId) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is missing @Id annotation");
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ' ') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(" WHERE ");
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ID.class) && !DEFAULT_ID_FIELD_NAME.equals(field.getName())) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String idFieldName = "";
            if (column == null || StringUtils.isEmpty(column.value())) {
                idFieldName = field.getName();
            } else {
                idFieldName = column.value();
            }
            stringBuilder.append(idFieldName).append("=?");
            try {
                field.setAccessible(true);
                Object value = field.get(entity);
                params.add(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
            }
            break;
        }
        String sql = stringBuilder.toString();
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put(SQL_KEY, sql);
        dataMap.put(PARAMS_KEY, params.toArray());
        return dataMap;
    }

    /**
     * @description 构建根据id主键删除的SQL语句
     * @author yida
     * @date 2023-04-26 09:09:13
     * @param clazz
     *
     * @return String
     */
    public static String buildDeleteByIdSQL(Class<?> clazz) {
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ");
        String tableName = getTableName(clazz);
        sqlBuilder.append(tableName).append(" WHERE ");
        boolean hasId = false;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ID.class) && !DEFAULT_ID_FIELD_NAME.equals(field.getName())) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String idFieldName = "";
            if (column == null || StringUtils.isEmpty(column.value())) {
                idFieldName = field.getName();
            } else {
                idFieldName = column.value();
            }
            hasId = true;
            sqlBuilder.append(idFieldName).append("=?");
            break;
        }
        if (!hasId) {
            throw new IllegalArgumentException("The class " + clazz.getName() + " does not define a primary key with @Id annotation.");
        }
        return sqlBuilder.toString();
    }

    /**
     * @description 构建根据id主键查询对象的SQL语句
     * @author yida
     * @date 2023-04-26 09:13:12
     * @param clazz
     *
     * @return String
     */
    public static String buildFindByIdSQL(Class<?> clazz) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
        String tableName = getTableName(clazz);
        sqlBuilder.append(tableName).append(" WHERE ");
        boolean hasId = false;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ID.class) && !DEFAULT_ID_FIELD_NAME.equals(field.getName())) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String idFieldName = "";
            if (column == null || StringUtils.isEmpty(column.value())) {
                idFieldName = field.getName();
            } else {
                idFieldName = column.value();
            }
            hasId = true;
            sqlBuilder.append(idFieldName).append("=?");
            break;
        }
        if (!hasId) {
            throw new IllegalArgumentException("The class " + clazz.getName() + " does not define a primary key with @Id annotation.");
        }
        return sqlBuilder.toString();
    }

    /**
     * @description 构建根据id主键列表查询对象列表的SQL语句
     * @author yida
     * @date 2023-04-26 09:13:12
     * @param clazz
     *
     * @return String
     */
    public static String buildFindByIdListSQL(int idListSize, Class<?> clazz) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
        String tableName = getTableName(clazz);
        sqlBuilder.append(tableName).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        String idFieldName = getIdFieldName(fields);
        if (StringUtils.isEmpty(idFieldName)) {
            throw new IllegalArgumentException("The class " + clazz.getName() + " does not define a primary key with @Id annotation.");
        }
        sqlBuilder.append(idFieldName).append(" in (");
        for(int i=0; i < idListSize; i++) {
            sqlBuilder.append("?,");
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    /**
     * @description 构建根据id主键列表删除对象列表的SQL语句
     * @author yida
     * @date 2023-04-26 09:13:12
     * @param clazz
     *
     * @return String
     */
    public static String buildDeleteByIdListSQL(int idListSize, Class<?> clazz) {
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ");
        String tableName = getTableName(clazz);
        sqlBuilder.append(tableName).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        String idFieldName = getIdFieldName(fields);
        if (StringUtils.isEmpty(idFieldName)) {
            throw new IllegalArgumentException("The class " + clazz.getName() + " does not define a primary key with @Id annotation.");
        }
        sqlBuilder.append(idFieldName).append(" in (");
        for(int i=0; i < idListSize; i++) {
            sqlBuilder.append("?,");
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    /**
     * @description 构建批量插入SQL语句
     * @author yida
     * @date 2023-04-26 10:28:54
     * @param entityList
     *
     * @return {@link Map}
     */
    public static <T>Map<String, Object> buildBatchInsertSQL(List<T> entityList, boolean single) {
        return buildBatchInsertSQL(entityList, single, false);
    }

    /**
     * @description 构建批量插入SQL语句
     * @author yida
     * @date 2023-04-26 10:28:54
     * @param entityList
     *
     * @return {@link Map}
     */
    public static <T>Map<String, Object> buildBatchInsertSQL(List<T> entityList, boolean single, boolean ignoreDuplicate) {
        if (null == entityList || entityList.isEmpty()) {
            throw new IllegalArgumentException("Entity list is null or empty.");
        }
        T entity = entityList.get(0);
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);
        StringBuilder sqlBuilder = new StringBuilder();
        if(ignoreDuplicate) {
            sqlBuilder.append("INSERT ignore INTO ").append(tableName).append(" (");
        } else {
            sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
        }
        StringBuilder sqlValuesBuilder = new StringBuilder("(");
        List<Object[]> parameterList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            if(Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ID id = field.getAnnotation(ID.class);
            if(null != id) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String fieldName = "";
            if (column != null && StringUtils.isNotEmpty(column.value())) {
                fieldName = column.value();
            } else {
                fieldName = StringUtils.toSnakeCase(field.getName());
            }
            if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                //属性名与默认主键id名称相同，也会跳过
                continue;
            }
            sqlBuilder.append(fieldName).append(", ");
            sqlValuesBuilder.append("?,");
            count++;
        }

        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ' ') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }
        if (sqlValuesBuilder.charAt(sqlValuesBuilder.length() - 1) == ',') {
            sqlValuesBuilder.deleteCharAt(sqlValuesBuilder.length() - 1);
        }
        sqlBuilder.append(") VALUES ");
        sqlValuesBuilder.append("),");
        String sqlValuePart = sqlValuesBuilder.toString();
        int currentIndex = 0;
        for (T entityT : entityList) {
            if(!single || (single && currentIndex == 0)) {
                sqlBuilder.append(sqlValuePart);
            }
            Object[] params = new Object[count];
            int index = 0;
            for (Field field : fields) {
                if(Modifier.isStatic(field.getModifiers())) {
                    currentIndex++;
                    continue;
                }
                ID id = field.getAnnotation(ID.class);
                if(null != id) {
                    currentIndex++;
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                String fieldName = "";
                if (column != null && StringUtils.isNotEmpty(column.value())) {
                    fieldName = column.value();
                } else {
                    fieldName = StringUtils.toSnakeCase(field.getName());
                }
                if(DEFAULT_ID_FIELD_NAME.equals(fieldName)) {
                    //属性名与默认主键id名称相同，也会跳过
                    currentIndex++;
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object value = field.get(entityT);
                    params[index++] = value;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to retrieve field value: " + field.getName(), e);
                }
            }
            parameterList.add(params);
            currentIndex++;
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') {
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        }

        String sql = sqlBuilder.toString();
        Map<String, Object> result = new HashMap<>();
        result.put(SQL_KEY, sql);
        result.put(PARAMS_KEY, parameterList.toArray(new Object[0][0]));
        return result;
    }

    /**
     * @description 确定主键字段名称
     * @author yida
     * @date 2023-04-26 18:32:15
     * @param fields
     *
     * @return String
     */
    public static String getIdFieldName(Field[] fields) {
        String idFieldName = "";
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ID.class) && !DEFAULT_ID_FIELD_NAME.equals(field.getName())) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column == null || StringUtils.isEmpty(column.value())) {
                idFieldName = field.getName();
            } else {
                idFieldName = column.value();
            }
            break;
        }
        return idFieldName;
    }

    /**
     * @description 获取表名称
     * @author yida
     * @date 2023-04-27 11:19:22
     * @param clazz
     *
     * @return String
     */
    private static <T> String getTableName(Class<T> clazz) {
        String tableName = "";
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.value();
            if(StringUtils.isEmpty(tableName)) {
                tableName = StringUtils.toSnakeCase(clazz.getSimpleName());
            }
        } else {
            tableName = StringUtils.toSnakeCase(clazz.getSimpleName());
        }
        return tableName;
    }

    public static void main(String[] args) {
        Object[] params = null;
        User user = new User(1, "Jack", 35);
        Map<String, Object> dataMap = SQLBuilder.buildInsertSQLAndParams(user);
        String insertSQL = dataMap.get(SQL_KEY).toString();
        params = (Object[])dataMap.get(PARAMS_KEY);
        System.out.println("insertSQL:" + insertSQL);
        System.out.println("params:" + GsonUtils.getInstance().beanToString(params));

        dataMap = SQLBuilder.buildUpdateByIdSQL(user);
        String updateSQL = dataMap.get(SQL_KEY).toString();
        params = (Object[])dataMap.get(PARAMS_KEY);
        System.out.println("updateSQL:" + updateSQL);
        System.out.println("params:" + GsonUtils.getInstance().beanToString(params));

        String deleteSQL = SQLBuilder.buildDeleteByIdSQL(user.getClass());
        System.out.println("deleteSQL:" + deleteSQL);

        String findByIdSQL = SQLBuilder.buildFindByIdSQL(user.getClass());
        System.out.println("findByIdSQL:" + findByIdSQL);

        User user1 = new User(1, "Jack", 35);
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
        dataMap = SQLBuilder.buildBatchInsertSQL(userList, true);
        String batchInsertSQL = dataMap.get(SQL_KEY).toString();
        params = (Object[][])dataMap.get(PARAMS_KEY);
        System.out.println("batchInsertSQL:" + batchInsertSQL);
        System.out.println("params:" + GsonUtils.getInstance().beanToString(params));

    }
}