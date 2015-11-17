package com.yida.spider4j.crawler.pipeline;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sun.misc.Unsafe;

import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.ArrayUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.db.DBUtil;
/**
 * @ClassName: DataBasePipeline
 * @Description: 存储PageResultItem数据至数据库里
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午10:33:23
 *
 */
@SuppressWarnings({"rawtypes","unused"})
public class DataBasePipeline implements Pipeline {
	/**插入到哪个数据库[数据库名称]*/
	private String databaseName;
	
	/**插入到哪个表[表名称]*/
	private String tableName;
	
	/**字段映射[key-value:data-key --> cloumn-name]*/
	private LinkedHashMap<String,String> columnMapping;
	
	/**执行SQL语句*/
	private final String sql;
	
	/**多值情况下,多值的拼接分隔符,默认为*/
	private String multiSeparator = "#$&#";
	
	/**主键字段名称[主要用来判断是否数据是否已经存在,若存在则执行更新操作]*/
	//若不指定主键字段名称,则只能执行insert操作了,无法执行update操作
	private String uniqueKey;
	
	private DBUtil dbUtil = DBUtil.getInstance();

	public DataBasePipeline(String databaseName, String tableName,String uniqueKey,
			LinkedHashMap<String, String> columnMapping, String sql, String multiSeparator) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		this.columnMapping = columnMapping;
		this.sql = sql;
		this.multiSeparator = multiSeparator;
	}

	public DataBasePipeline(String databaseName, String tableName,String uniqueKey,
			LinkedHashMap<String, String> columnMapping, String sql) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		this.columnMapping = columnMapping;
		this.sql = sql;
	}

	public DataBasePipeline(String databaseName, String tableName, String uniqueKey,String sql) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		this.sql = sql;
	}

	public DataBasePipeline(String databaseName, String tableName,String uniqueKey) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		this.sql = null;
	}
	
	public DataBasePipeline(String tableName,String uniqueKey) {
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		this.sql = null;
	}
	
	public DataBasePipeline(String tableName) {
		this.tableName = tableName;
		this.sql = null;
	}
	
	@Override
	public void process(PageResultItem pageResultItem, Task task) {
		if(pageResultItem == null || (
			(pageResultItem.getDataMap() == null || 
					pageResultItem.getDataMap().isEmpty()) 
			&& 
			(pageResultItem.getDataMapList() == null || 
					pageResultItem.getDataMapList().size() <= 0))) {
			return;
		}
		String sql = this.sql;
		
		Object[] params = dataToArray(pageResultItem);
		if(params == null || params.length <= 0) {
			return;
		}
		//若没有设置主键,则生成insert SQL
		if(StringUtils.isEmpty(this.uniqueKey)) {
			//若用户没传入待执行的SQL语句
			if(StringUtils.isEmpty(sql)) {
				sql = createInsertSQL(pageResultItem.getAll());
			}
			if(StringUtils.isEmpty(sql)) {
				return;
			}
			Connection con = null;
			try {
				if(StringUtils.isEmpty(databaseName)) {
					con = dbUtil.openConn();
				} else {
					con = dbUtil.openConn(databaseName);
				}
				dbUtil.update(con, sql, params);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					dbUtil.closeConn(con);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} 
		else {
			//若用户没有指定SQL语句,则需要自动生成SQL
			if(StringUtils.isEmpty(sql)) {
				String fieldName = null;
				if(this.columnMapping != null && !this.columnMapping.isEmpty()) {
					for(Map.Entry<String,String> entry : this.columnMapping.entrySet()) {
						String val = entry.getValue();
						if(StringUtils.isEmpty(val)) {
							continue;
						}
						if(fieldName == null && val.equals(uniqueKey)) {
							fieldName = entry.getKey();
						}
					}
				}
				
				if(StringUtils.isEmpty(fieldName)) {
					fieldName = this.uniqueKey;
				}
				//若有设置主键,则先根据主键查询,若查询到数据,则表明需要更新,否则需要插入
				String selectSql = "select " + this.uniqueKey + 
					" from " + this.tableName + " where " + this.uniqueKey + "=?";
				Object uniqueKeyVal = pageResultItem.getDataMap().get(fieldName);
				
				//如果主键值为空,则直接跳过不处理
				if(null == uniqueKeyVal) {
					return;
				}
				Connection con = null;
				try {
					if(StringUtils.isEmpty(databaseName)) {
						con = dbUtil.openConn();
					} else {
						con = dbUtil.openConn(databaseName);
					}
					Object keyObj = dbUtil.queryColumn(this.uniqueKey, con, 
						selectSql, new Object[] {uniqueKeyVal});
					//若根据主键找到了记录,说明需要执行更新操作
					if(null != keyObj) {
						if(StringUtils.isEmpty(fieldName)) {
							return;
						}
						sql = createUpdateSQL(pageResultItem.getDataMap());
						Object[] paramsNew = ArrayUtils.copyArrayGrowN(params, 1);
						paramsNew[params.length] = uniqueKeyVal;
						dbUtil.update(con, sql, paramsNew);
					} else {
						sql = createInsertSQL(pageResultItem.getDataMap()); 
						//insert into database
						dbUtil.update(con, sql, params);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						dbUtil.closeConn(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else {
				
			}	
		}
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: createInsertSQL
	 * @Description: 构建Insert SQL语句
	 * @param @param dataMap
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String createInsertSQL(Map<String,Object> dataMap) {
		if(StringUtils.isEmpty(this.tableName)) {
			return null;
		}
		if(dataMap == null || dataMap.isEmpty()) {
			return null;
		}
		if(null != this.columnMapping && this.columnMapping.size() > 0) {
			StringBuilder builder = new StringBuilder("insert into ")
				.append(this.tableName).append("(");
			String questionMark = "";
			for(Map.Entry<String,String> entry : this.columnMapping.entrySet()) {
				String val = entry.getValue();
				if(StringUtils.isEmpty(val)) {
					continue;
				}
				builder.append(val).append(",");
				questionMark += "?,";
			}
			builder.append(") values (").append(questionMark).append(")");
			String sql = builder.toString().replace(",)", ")");
			return sql;
		}
		
		StringBuilder builder = new StringBuilder("insert into ")
		.append(this.tableName).append("(");
		String questionMark = "";
		for(Map.Entry<String,Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			builder.append(key).append(",");
			questionMark += "?,";
		}
		builder.append(") values (").append(questionMark).append(")");
		String sql = builder.toString().replace(",)", ")");
		return sql;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: createUpdateSQL
	 * @Description: 构建Update SQL语句
	 * @param @param dataMap
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String createUpdateSQL(Map<String,Object> dataMap) {
		//若主键名称或表名称未指定,则直接跳过return null.
		if(StringUtils.isEmpty(this.uniqueKey) || 
			StringUtils.isEmpty(this.tableName)) {
			return null;
		}
		
		if(null != this.columnMapping && this.columnMapping.size() > 0) {
			StringBuilder builder = new StringBuilder("update ")
				.append(this.tableName).append(" set ");
			String fieldName = null;
			for(Map.Entry<String,String> entry : this.columnMapping.entrySet()) {
				String val = entry.getValue();
				if(StringUtils.isEmpty(val)) {
					continue;
				}
				if(fieldName == null && val.equals(uniqueKey)) {
					fieldName = entry.getKey();
				}
				builder.append(val).append("=?,");
			}
			Object keyObjectVal = dataMap.get(fieldName);
			//若主键字段的值为空,则就没必要生成update SQL语句了,没有主键值,也没更新
			if(null == keyObjectVal || keyObjectVal.toString().equals("")) {
				return null;
			}
			builder.append(" where ").append(this.uniqueKey).append("=?");
			String sql = builder.toString().replace("=?, where", "=? where");
			return sql;
		}
		if(dataMap == null || dataMap.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder("update ")
		.append(this.tableName).append(" set ");
		for(Map.Entry<String,Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			builder.append(key).append("=?,");
		}
		builder.append(" where ").append(this.uniqueKey).append("=?");
		String sql = builder.toString().replace("=?, where", "=? where");
		return sql;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: dataToArrayList
	 * @Description: 将收集到的数据转换成二维数组,便于批量插入
	 * @param @param pageResultItem
	 * @param @return
	 * @return Object[][]
	 * @throws
	 */
	private List<Object[]> dataToArrayList(PageResultItem pageResultItem) {
		List<Object[]> list = new ArrayList<Object[]>();
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, Object> entry : pageResultItem.getAll().entrySet()) {
			Object[] data = new Object[2];
			data[0] = entry.getKey();
			if (entry.getValue() instanceof Iterable) {
				Iterable value = (Iterable) entry.getValue();
                for (Object o : value) {
                	builder.append(o.toString()).append(multiSeparator);
                }
                String okay = builder.toString().replaceAll("#\\$&#$", "");
                data[1] = okay;
            } else {
            	data[1] = entry.getValue();
            }
			list.add(data);
        }
		return list;
	}

	private Object[] dataToArray(PageResultItem pageResultItem) {
		if(pageResultItem == null || pageResultItem.getAll() == null ||
				pageResultItem.getAll().isEmpty()) {
			return null;
		}
		Object[] data = new Object[pageResultItem.getAll().size()];
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (Map.Entry<String, Object> entry : pageResultItem.getAll().entrySet()) {
			if (entry.getValue() instanceof Iterable) {
                Iterable value = (Iterable) entry.getValue();
                for (Object o : value) {
                	builder.append(o.toString()).append(multiSeparator);
                }
                String separa = StringUtils.escapeRegex(multiSeparator);
                String okay = builder.toString().replaceAll(separa, "");
                data[index++] = okay;
            } else {
            	data[index++] = entry.getValue();
            }
        }
		return data;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public DataBasePipeline setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}

	public String getTableName() {
		return tableName;
	}

	public DataBasePipeline setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public LinkedHashMap<String, String> getColumnMapping() {
		return columnMapping;
	}

	public DataBasePipeline setColumnMapping(LinkedHashMap<String, String> columnMapping) {
		this.columnMapping = columnMapping;
		return this;
	}

	public String getSql() {
		
		return sql;
	}

	public String getMultiSeparator() {
		return multiSeparator;
	}

	public DataBasePipeline setMultiSeparator(String multiSeparator) {
		this.multiSeparator = multiSeparator;
		return this;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public DataBasePipeline setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
		return this;
	}
}
