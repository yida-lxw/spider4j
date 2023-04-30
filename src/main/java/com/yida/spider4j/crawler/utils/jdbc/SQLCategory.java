package com.yida.spider4j.crawler.utils.jdbc;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-27 11:50
 * @description SQL语句分类
 */
public enum SQLCategory {
    FIND_BY_ID("findById"),
    FIND_BY_IDS("findByIds"),
    UPDATE_BY_ID("updateById"),
    DELETE_BY_ID("deleteById"),
    DELETE_BY_IDS("deleteByIds"),
    INSERT_AND_GET_ID("insertAndGetId"),
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    INSERT_BATCH("insertBatch"),
    UPDATE_BATCH("updateBatch");


    public static final String FIND_BY_ID_KEY = "findById";
    public static final String FIND_BY_IDS_KEY = "findByIds";
    public static final String UPDATE_BY_ID_KEY = "updateById";
    public static final String DELETE_BY_ID_KEY = "deleteById";
    public static final String DELETE_BY_IDS_KEY = "deleteByIds";
    public static final String INSERT_AND_GET_ID_KEY = "insertAndGetId";
    public static final String INSERT_KEY = "insert";
    public static final String UPDATE_KEY = "update";
    public static final String DELETE_KEY = "delete";
    public static final String INSERT_BATCH_KEY = "insertBatch";
    public static final String UPDATE_BATCH_KEY = "updateBatch";

    public static final SQLCategory FIND_BY_ID_SQL = FIND_BY_ID;
    public static final SQLCategory FIND_BY_IDS_SQL = FIND_BY_IDS;
    public static final SQLCategory UPDATE_BY_ID_SQL = UPDATE_BY_ID;
    public static final SQLCategory DELETE_BY_ID_SQL = DELETE_BY_ID;
    public static final SQLCategory DELETE_BY_IDS_SQL = DELETE_BY_IDS;
    public static final SQLCategory INSERT_AND_GET_ID_SQL = INSERT_AND_GET_ID;
    public static final SQLCategory INSERT_SQL = INSERT;
    public static final SQLCategory UPDATE_SQL = UPDATE;
    public static final SQLCategory DELETE_SQL = DELETE;
    public static final SQLCategory INSERT_BATCH_SQL = INSERT_BATCH;
    public static final SQLCategory UPDATE_BATCH_SQL = UPDATE_BATCH;

    public SQLCategory of(String key) {
        if(FIND_BY_ID_KEY.equals(key)) {
            return FIND_BY_ID_SQL;
        }
        if(FIND_BY_IDS_KEY.equals(key)) {
            return FIND_BY_IDS_SQL;
        }
        if(UPDATE_BY_ID_KEY.equals(key)) {
            return UPDATE_BY_ID_SQL;
        }
        if(DELETE_BY_ID_KEY.equals(key)) {
            return DELETE_BY_ID_SQL;
        }
        if(DELETE_BY_IDS_KEY.equals(key)) {
            return DELETE_BY_IDS_SQL;
        }
        if(INSERT_AND_GET_ID_KEY.equals(key)) {
            return INSERT_AND_GET_ID_SQL;
        }
        if(INSERT_KEY.equals(key)) {
            return INSERT_SQL;
        }
        if(UPDATE_KEY.equals(key)) {
            return UPDATE_SQL;
        }
        if(DELETE_KEY.equals(key)) {
            return DELETE_SQL;
        }
        if(INSERT_BATCH_KEY.equals(key)) {
            return INSERT_BATCH_SQL;
        }
        if(UPDATE_BATCH_KEY.equals(key)) {
            return UPDATE_BATCH_SQL;
        }
        throw new IllegalArgumentException("UnSupported SQLCategory key:" + key);
    }

    private String key;

    SQLCategory(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
