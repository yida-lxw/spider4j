package com.yida.spider4j.crawler.utils.jdbc;

import java.util.List;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 16:26
 * @description Type your description over here.
 */
public interface BaseDAO<P,T> {
    /**
     * @description 根据主键ID查询
     * @author yida
     * @date 2023-04-26 16:30:26
     * @param id
     *
     * @return {@link T}
     */
    T findById(P id);

    /**
     * @description 根据主键ID列表进行in查询
     * @author yida
     * @date 2023-04-27 09:25:15
     * @param idList
     *
     * @return {@link List}
     */
    List<T> findByIds(List<P> idList);

    /**
     * @description 根据自定义SQL语句进行查询
     * @author yida
     * @date 2023-04-27 09:27:19
     * @param sql
     * @param paramList
     *
     * @return {@link List}
     */
    List<T> findByCondition(String sql, List<?> paramList);

    /**
     * @description 分页查询
     * @author yida
     * @date 2023-04-27 09:52:02
     * @param sql
     * @param paramList
     *
     * @return {@link Page}
     */
    Page<T> pageQuery(String sql, List<?> paramList, int currentPage, int pageSize);

    /**
     * @description 根据主键ID更新
     * @author yida
     * @date 2023-04-26 16:30:11
     * @param entity
     *
     * @return boolean
     */
    boolean updateById(T entity);

    /**
     * @description 根据主键ID删除
     * @author yida
     * @date 2023-04-26 16:29:55
     * @param id
     *
     * @return boolean
     */
    boolean deleteById(P id);

    /**
     * @description 根据主键ID列表批量删除
     * @author yida
     * @date 2023-04-27 10:22:43
     * @param idList
     *
     * @return boolean
     */
    boolean deleteByIds(List<P> idList);

    /**
     * @description 插入指定对象(返回插入记录的主键ID值)
     * @author yida
     * @date 2023-04-26 16:32:24
     * @param entity
     *
     * @return {@link P}
     */
    P insertAndGetId(T entity);

    /**
     * @description 新增对象
     * @author yida
     * @date 2023-04-26 17:25:49
     * @param entity
     *
     * @return boolean
     */
    boolean insert(T entity);

    /**
     * @description 更新对象
     * @author yida
     * @date 2023-04-26 16:30:11
     * @param entity
     *
     * @return boolean
     */
    boolean update(T entity);

    /**
     * @description 删除对象
     * @author yida
     * @date 2023-04-26 18:26:41
     * @param entity
     *
     * @return boolean
     */
    boolean delete(T entity);

    /**
     * @description 根据主键ID查询，若已存在，则更新，否则执行insert
     * @author yida
     * @date 2023-04-27 09:54:53
     * @param entity
     *
     * @return boolean
     */
    boolean insertOrUpdate(T entity);

    /**
     * @description 保存一个对象
     * @author yida
     * @date 2023-04-27 09:55:44
     * @param entity
     *
     * @return boolean
     */
    boolean save(T entity);

    /**
     * @description 批量插入
     * @author yida
     * @date 2023-04-27 09:56:46
     * @param insertBatch
     *
     * @return boolean
     */
    boolean insertBatch(List<T> insertBatch);

    /**
     * @description 批量更新
     * @author yida
     * @date 2023-04-27 10:22:43
     * @param entityList
     *
     * @return boolean
     */
    boolean updateBatch(List<T> entityList);

    /**
     * @description 批量插入或更新(根据每个对象的主键ID来自动判断是insert/update)
     * @author yida
     * @date 2023-04-27 10:21:18
     * @param entityList
     *
     * @return boolean
     */
    boolean insertOrUpdateBatch(List<T> entityList);

    /**
     * @description 批量保存(根据每个对象的主键ID来自动判断是insert/update)
     * @author yida
     * @date 2023-04-27 10:21:18
     * @param entityList
     *
     * @return boolean
     */
    boolean saveBatch(List<T> entityList);
}
