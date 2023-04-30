package com.yida.spider4j.crawler.utils.jdbc;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.yida.spider4j.crawler.utils.common.ReflectionUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-27 10:25
 * @description BaseDAO接口的抽象实现
 */
public abstract class AbstractDAO<P,T> implements BaseDAO<P, T> {
    /**实体类型*/
    protected Class<T> entityClass;
    /**主键ID类型*/
    protected Class<P> primaryFieldClass;

    /**SQL语句缓存*/
    protected Table<String, String, String> sqlTable = HashBasedTable.create();

    public AbstractDAO() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        if (types[0] instanceof ParameterizedType) {
            primaryFieldClass = (Class<P>) ((ParameterizedType) types[0]).getRawType();
        } else {
            primaryFieldClass = (Class<P>) types[0];
        }
        if (types[1] instanceof ParameterizedType) {
            entityClass = (Class<T>) ((ParameterizedType) types[1]).getRawType();
        } else {
            entityClass = (Class<T>) types[1];
        }
    }

    /**
     * @description 根据主键ID查询
     * @author yida
     * @date 2023-04-26 16:30:26
     * @param id
     *
     * @return {@link T}
     */
    public T findById(P id) {
        String findByIdKey = "findById";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, findByIdKey);
        String findByIdSQL = "";
        if (StringUtils.isEmpty(cacheSql)) {
            findByIdSQL = SQLBuilder.buildFindByIdSQL(entityClass);
            sqlTable.put(entityClassName, findByIdKey, findByIdSQL);
        } else {
            findByIdSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(findByIdSQL)) {
            return JDBCUtils.findById(id, findByIdSQL, new BeanHandler<>(entityClass), true);
        }
        return null;
    }

    /**
     * @description 根据主键ID列表进行in查询
     * @author yida
     * @date 2023-04-27 09:25:15
     * @param idList
     *
     * @return {@link List}
     */
    public List<T> findByIds(List<P> idList) {
        String findByIdsKey = "findByIds";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, findByIdsKey);
        String findByIdsSQL = "";
        if (StringUtils.isEmpty(cacheSql)) {
            int listSize = (null == idList)? 0 : idList.size();
            findByIdsSQL = SQLBuilder.buildFindByIdListSQL(listSize, entityClass);
            sqlTable.put(entityClassName, findByIdsKey, findByIdsSQL);
        } else {
            findByIdsSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(findByIdsSQL)) {
            return JDBCUtils.findByIds(idList, findByIdsSQL, new BeanListHandler<>(entityClass), true);
        }
        return null;
    }


    /**
     * @description 根据自定义SQL语句进行查询
     * @author yida
     * @date 2023-04-27 09:27:19
     * @param sql
     * @param paramList
     *
     * @return {@link List}
     */
    public List<T> findByCondition(String sql, List<?> paramList) {
        return JDBCUtils.queryList(sql, new BeanListHandler<>(entityClass), paramList.toArray(), true);
    }

    /**
     * @description 分页查询
     * @author yida
     * @date 2023-04-27 09:52:02
     * @param sql
     * @param paramList
     *
     * @return {@link Page}
     */
    public Page<T> pageQuery(String sql, List<?> paramList, int currentPage, int pageSize) {
        if(pageSize <= 0) {
            pageSize = Page.DEFAULT_PAGE_SIZE;
        } if(pageSize > Page.DEFAULT_MAX_PAGE_SIZE) {
            pageSize = Page.DEFAULT_MAX_PAGE_SIZE;
        }
        int start = (currentPage - 1) * pageSize;
        List<Object> parameterList = new ArrayList<>(paramList);
        parameterList.add(start);
        parameterList.add(pageSize);
        List<T> dataList = JDBCUtils.queryList(sql, new BeanListHandler<>(entityClass), parameterList.toArray(), true);
        String selectCountSQL = SQLBuilder.buildSelectCountSQL(sql, entityClass);
        Long totalRecordCount = JDBCUtils.queryCount(selectCountSQL, paramList.toArray(), true);
        return new Page<T>(currentPage, totalRecordCount, pageSize, dataList);
    }

    /**
     * @description 根据主键ID更新
     * @author yida
     * @date 2023-04-26 16:30:11
     * @param entity
     *
     * @return boolean
     */
    public boolean updateById(T entity) {
        String updateByIdSQLKey = "updateById";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, updateByIdSQLKey);
        String updateByIdSQL = "";
        Object[] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> updateByIdSQLMap = SQLBuilder.buildUpdateByIdSQL(entity);
            updateByIdSQL = updateByIdSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[])updateByIdSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, updateByIdSQLKey, updateByIdSQL);
        } else {
            updateByIdSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(updateByIdSQL)) {
            return JDBCUtils.update(updateByIdSQL, params, true);
        }
        return false;
    }

    /**
     * @description 根据主键ID删除
     * @author yida
     * @date 2023-04-26 16:29:55
     * @param id
     *
     * @return boolean
     */
    public boolean deleteById(P id) {
        String deleteByIdSQLKey = "deleteById";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, deleteByIdSQLKey);
        String deleteByIdSQL = "";
        if (StringUtils.isEmpty(cacheSql)) {
            deleteByIdSQL = SQLBuilder.buildDeleteByIdSQL(entityClass);
            sqlTable.put(entityClassName, deleteByIdSQLKey, deleteByIdSQL);
        } else {
            deleteByIdSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(deleteByIdSQL)) {
            return JDBCUtils.deleteById(deleteByIdSQL, id, true);
        }
        return false;
    }

    /**
     * @description 根据主键ID列表批量删除
     * @author yida
     * @date 2023-04-27 10:22:43
     * @param idList
     *
     * @return boolean
     */
    public boolean deleteByIds(List<P> idList) {
        String deleteByIdsSQLKey = "deleteByIds";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, deleteByIdsSQLKey);
        String deleteByIdsSQL = "";
        if (StringUtils.isEmpty(cacheSql)) {
            int idListSize = (null == idList)? 0 : idList.size();
            deleteByIdsSQL = SQLBuilder.buildDeleteByIdListSQL(idListSize, entityClass);
            sqlTable.put(entityClassName, deleteByIdsSQLKey, deleteByIdsSQL);
        } else {
            deleteByIdsSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(deleteByIdsSQL)) {
            return JDBCUtils.deleteByIds(deleteByIdsSQL, idList, true);
        }
        return false;
    }

    /**
     * @description 插入指定对象(返回插入记录的主键ID值)
     * @author yida
     * @date 2023-04-26 16:32:24
     * @param entity
     *
     * @return {@link P}
     */
    public P insertAndGetId(T entity) {
        String insertSQLKey = "insertAndGetId";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, insertSQLKey);
        String insertSQL = "";
        Object[] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> insertSQLMap = SQLBuilder.buildInsertSQLAndParams(entity);
            insertSQL = insertSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[])insertSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, insertSQLKey, insertSQL);
        } else {
            insertSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(insertSQL)) {
            Field[] fields = entityClass.getDeclaredFields();
            String idFieldName = SQLBuilder.getIdFieldName(fields);
            return JDBCUtils.insertAndGetId(insertSQL, params, idFieldName, primaryFieldClass, true);
        }
        return null;
    }

    /**
     * @description 新增对象
     * @author yida
     * @date 2023-04-26 17:25:49
     * @param entity
     *
     * @return boolean
     */
    public boolean insert(T entity) {
        String insertSQLKey = "insert";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, insertSQLKey);
        String insertSQL = "";
        Object[] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> insertSQLMap = SQLBuilder.buildInsertSQLAndParams(entity);
            insertSQL = insertSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[])insertSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, insertSQLKey, insertSQL);
        } else {
            insertSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(insertSQL)) {
            return JDBCUtils.insert(insertSQL, params, true);
        }
        return false;
    }

    /**
     * @description 更新对象
     * @author yida
     * @date 2023-04-26 16:30:11
     * @param entity
     *
     * @return boolean
     */
    public boolean update(T entity) {
        String updateSQLKey = "update";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, updateSQLKey);
        String updateSQL = "";
        Object[] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> updateSQLMap = SQLBuilder.buildUpdateByIdSQL(entity);
            updateSQL = updateSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[])updateSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, updateSQLKey, updateSQL);
        } else {
            updateSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(updateSQL)) {
            return JDBCUtils.update(updateSQL, params, true);
        }
        return false;
    }

    /**
     * @description 删除对象
     * @author yida
     * @date 2023-04-26 18:26:41
     * @param entity
     *
     * @return boolean
     */
    public boolean delete(T entity) {
        String deleteSQLKey = "delete";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, deleteSQLKey);
        String deleteSQL = "";
        if (StringUtils.isEmpty(cacheSql)) {
            deleteSQL = SQLBuilder.buildDeleteByIdSQL(entityClass);
            sqlTable.put(entityClassName, deleteSQLKey, deleteSQL);
        } else {
            deleteSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(deleteSQL)) {
            Field[] fields = entityClass.getDeclaredFields();
            String idFieldName = SQLBuilder.getIdFieldName(fields);
            P id = ReflectionUtils.loadFieldValue(entity, idFieldName);
            return JDBCUtils.deleteById(deleteSQL, id, true);
        }
        return false;
    }

    /**
     * @description 根据主键ID查询，若已存在，则更新，否则执行insert
     * @author yida
     * @date 2023-04-27 09:54:53
     * @param entity
     *
     * @return boolean
     */
    public boolean insertOrUpdate(T entity) {
        Field[] fields = entityClass.getDeclaredFields();
        String idFieldName = SQLBuilder.getIdFieldName(fields);
        P id = ReflectionUtils.loadFieldValue(entity, idFieldName);
        T oldEntity = findById(id);
        boolean result;
        if(null != oldEntity) {
            result = update(entity);
        } else {
            result = insert(entity);
        }
        return result;
    }

    /**
     * @description 保存一个对象
     * @author yida
     * @date 2023-04-27 09:55:44
     * @param entity
     *
     * @return boolean
     */
    public boolean save(T entity) {
        return insertOrUpdate(entity);
    }

    /**
     * @description 批量插入
     * @author yida
     * @date 2023-04-27 09:56:46
     * @param entityList
     *
     * @return boolean
     */
    public boolean insertBatch(List<T> entityList) {
        String insertBatchSQLKey = "insertBatch";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, insertBatchSQLKey);
        String insertBatchSQL = "";
        Object[][] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> insertBatchSQLMap = SQLBuilder.buildBatchInsertSQL(entityList, true, false);
            insertBatchSQL = insertBatchSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[][])insertBatchSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, insertBatchSQLKey, insertBatchSQL);
        } else {
            insertBatchSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(insertBatchSQL)) {
            return JDBCUtils.insertBatch(insertBatchSQL, params, true);
        }
        return false;
    }

    /**
     * @description 批量更新
     * @author yida
     * @date 2023-04-27 10:22:43
     * @param entityList
     *
     * @return boolean
     */
    public boolean updateBatch(List<T> entityList) {
        String updateBatchSQLKey = "updateBatch";
        String entityClassName = entityClass.getName();
        String cacheSql = sqlTable.get(entityClassName, updateBatchSQLKey);
        String updateBatchSQL = "";
        Object[][] params = null;
        if (StringUtils.isEmpty(cacheSql)) {
            Map<String, Object> updateBatchSQLMap = SQLBuilder.buildUpdateByIdBatchSQL(entityList);
            updateBatchSQL = updateBatchSQLMap.get(SQLBuilder.SQL_KEY).toString();
            params = (Object[][])updateBatchSQLMap.get(SQLBuilder.PARAMS_KEY);
            sqlTable.put(entityClassName, updateBatchSQLKey, updateBatchSQL);
        } else {
            updateBatchSQL = cacheSql;
        }
        if(StringUtils.isNotEmpty(updateBatchSQL)) {
            return JDBCUtils.updateBatch(updateBatchSQL, params, true);
        }
        return false;
    }

    /**
     * @description 批量插入或更新(根据每个对象的主键ID来自动判断是insert/update)
     * @author yida
     * @date 2023-04-27 10:21:18
     * @param entityList
     *
     * @return boolean
     */
    public boolean insertOrUpdateBatch(List<T> entityList) {
        T entity = entityList.get(0);
        Field[] fields = entity.getClass().getDeclaredFields();
        String idFieldName = SQLBuilder.getIdFieldName(fields);
        List<P> idList =
                entityList.stream().filter(t->ReflectionUtils.<P, T>loadFieldValue(t, idFieldName) != null)
                        .map(t -> ReflectionUtils.<P, T>loadFieldValue(t, idFieldName)).collect(Collectors.toList());
        if(idList.size() <= 0) {
            return insertBatch(entityList);
        }
        List<T> oldEntityList = findByIds(idList);
        boolean success = true;
        for(T currentEntity : entityList) {
            if(null != oldEntityList && oldEntityList.contains(currentEntity)) {
                //update
                if(!update(currentEntity)) {
                    success = false;
                }
            } else {
                if(!insert(currentEntity)) {
                    success = false;
                }
            }
        }
        return success;
    }

    /**
     * @description 批量保存(根据每个对象的主键ID来自动判断是insert/update)
     * @author yida
     * @date 2023-04-27 10:21:18
     * @param entityList
     *
     * @return boolean
     */
    public boolean saveBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList);
    }
}
