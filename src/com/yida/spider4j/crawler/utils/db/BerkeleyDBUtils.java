package com.yida.spider4j.crawler.utils.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.yida.spider4j.crawler.config.Configurations;

/**
 * Berkeley Database工具类
 * @author  Lanxiaowei
 * @created 2013-09-08 17:20:18
 */
@SuppressWarnings({"unchecked","unused"})
public class BerkeleyDBUtils {
	/**数据库数据读写默认编码*/
	public static final String DEFAULT_ENCODING = Configurations.getStringProperty("db.default_encoding", "UTF-8");
	/**数据库日志文件最大字节数*/
	public static final long LOG_MAX_SIZE = Configurations.getLongProperty("db.log_max_size", 67108864);
	/**数据库缓存最大字节数*/
	public static final long CACHE_MAX_SIZE = Configurations.getLongProperty("db.cache_max_size", 4194304);
	/**数据库存储目录*/
	public static final String DATABASE_PATH = Configurations.getStringProperty("db.berkeley_basepath", "C:/berkekeyDB/");
	/**数据库名称*/
	public static final String DATABASE_NAME = Configurations.getStringProperty("db.database_name", "spider4j_db");
	/**是否开启事务*/
	public static final boolean ENABLE_TRANSACTION = Configurations.getBooleanProperty("db.enable_transaction", true);
	/**是否延迟数据写入*/
	public static final boolean ENABLE_DEFER_WRITE = Configurations.getBooleanProperty("db.enable_defer_write", false);
	/**是否自动创建数据库*/
	public static final boolean ALLOW_AUTO_CREATE_DB = Configurations.getBooleanProperty("db.allow_auto_create_database", true);
	/**是否启用数据库读写锁*/
	public static final boolean IS_LOCKING = Configurations.getBooleanProperty("db.is_locking", true);

	public static final String CLASS_DATABASE_NAME = "classDB";
	
	private Environment env;
	private EnvironmentConfig envConfig;
	private Database database;
	private Database classDB;
	private DatabaseConfig dbConfig;
	private ClassCatalog classCatalog;  

	static {
		// 初始化数据库目录,不存在则新建
		File file = new File(DATABASE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**无参构造函数私有化*/
	private BerkeleyDBUtils() {
		initialDB();
	}

	/** volatile确保对象被初始化为单例后,对象改变对所有线程可见 *//*
	private volatile static BerkeleyDBUtils dbUtils;

    // 这种单例实现模式不够线程安全,改成静态内部类的实现方式
	*//**
	 * 单例模式获取BerkeleyDBUtils工具类对象
	 * @return
	 *//*
	public static BerkeleyDBUtils getInstance() {
		if (null == dbUtils) {
			synchronized (BerkeleyDBUtils.class) {
				if (null == dbUtils) {
					dbUtils = new BerkeleyDBUtils();
				}
			}
		}
		return dbUtils;
	}*/
	
	/*******************静态内部类的单例模式begin*******************/
	private static class SingletonHolder {  
        private static final BerkeleyDBUtils INSTANCE = new BerkeleyDBUtils();  
    }  

    public static final BerkeleyDBUtils getInstance() {  
        return SingletonHolder.INSTANCE; 
    }
    /*******************静态内部类的单例模式end*******************/
    
    
	/**
	 * 数据库环境初始化<br/>
	 * 注意:此方法只需要应用程序启动时调用一次即可,不用每次操作数据库都调用
	 * 
	 * @throws EnvironmentLockedException
	 * @throws DatabaseException
	 */
	public void initialDB() {
		setEnvConfig(new EnvironmentConfig());
		getEnvConfig().setAllowCreate(ALLOW_AUTO_CREATE_DB);
		getEnvConfig().setTransactional(ENABLE_TRANSACTION);
		getEnvConfig().setLocking(IS_LOCKING);
		getEnvConfig().setCacheSize(CACHE_MAX_SIZE);
		getEnvConfig().setConfigParam(EnvironmentConfig.LOG_FILE_MAX, String.valueOf(LOG_MAX_SIZE));
		setEnv(new Environment(new File(DATABASE_PATH), getEnvConfig()));

		dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(ALLOW_AUTO_CREATE_DB);
		// 延迟写数据，减少IO操作次数
		dbConfig.setDeferredWrite(ENABLE_DEFER_WRITE);
		// 设置是否开启事务
		dbConfig.setTransactional(ENABLE_TRANSACTION);
		//是否允许重复key
		dbConfig.setSortedDuplicates(true);
	}

	/**
	 * 打开数据库<br/>
	 * 注意:每次进行数据库数据的增删改查操作时需要先调用此方法,有点类似于常规的JDBC操作中的建立数据库连接
	 */
	public void openDB() {
		setDatabase(getEnv().openDatabase(null, DATABASE_NAME, dbConfig));
		DatabaseConfig dc = new DatabaseConfig();
		dc.setAllowCreate(dbConfig.getAllowCreate());
		dc.setDeferredWrite(dbConfig.getDeferredWrite());
		dc.setTransactional(dbConfig.getTransactional());
		dc.setSortedDuplicates(false);
		setClassDB(getEnv().openDatabase(null, CLASS_DATABASE_NAME, dc));
		classCatalog = new StoredClassCatalog(getClassDB()); 
	}

	/**
	 * 关闭数据库<br/>
	 * 注意:每次进行数据库数据的增删改查操作完成后需要调用此方法,有点类似于常规的JDBC操作中的关闭数据库连接
	 */
	public void closeDB() {
		if (getDatabase() != null) {
			getDatabase().close();
		}
		if (getClassDB() != null) {
			getClassDB().close();
		}
	}

	/**
	 * 清理数据库资源,删除数据库环境<br/>
	 * 注意:此方法只需要应用程序退出前调用一次即可,不用每次操作数据库都调用
	 */
	public void cleanDB() {
		if (getEnv() != null) {
			getEnv().sync();
			//清理数据库日志文件
			getEnv().cleanLog();
			getEnv().close();
		}
	}

	/**
	 * 删除数据库
	 * 
	 * @throws DatabaseException
	 */
	public void cleanAllData() throws DatabaseException {
		closeDB();
		getEnv().removeDatabase(null, DATABASE_NAME);
	}

	/**
	 * 根据key保存一个对象[key用于后续查询参数] 
	 * <br/>注意:若key重复,则对象会被覆盖,等同于更新对象,可以把它理解成Map
	 * @param <T>
	 * 
	 * @param key
	 * @param t
	 * @return 返回是否保存成功
	 */
	public <T> boolean save(byte[] key, T t) {
		// 输入参数
		DatabaseEntry entryKey = new DatabaseEntry(key);
		// 输出参数
		DatabaseEntry entryValue = new DatabaseEntry();
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		boolean result = false;
		try {
			// 若用户配置了开启事务支持,则开始事务
			if (ENABLE_TRANSACTION) {
				txn = env.beginTransaction(null, null);
			}
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			EntryBinding<T> valueBinding = new SerialBinding<T>(getClassCatalog(), (Class<T>) t.getClass());
			valueBinding.objectToEntry(t, entryValue);
			// 保存对象
			operateStatus = cursor.put(entryKey, entryValue);
			// 若保存成功
			if (operateStatus == OperationStatus.SUCCESS) {
				result = true;
			}
		} catch (Exception e) {
			if (txn != null) {
				// 事务回滚
				txn.abort();
				txn = null;
			}
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
			// 提交事务
			if (txn != null) {
				txn.commit();
			}
		}
		return result;
	}

	/**
	 * 根据key查找[返回对象集合]
	 * @param <T>
	 * 
	 * @param key
	 * @param targetClass
	 * @return
	 */
	public <T> List<T> findByKey(byte[] key, Class<T> targetClass) {
		// 输入参数
		DatabaseEntry entryKey = new DatabaseEntry(key);
		// 输出参数
		DatabaseEntry entryValue = new DatabaseEntry();
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		List<T> list = null;
		try {
			//查询不需要开启事务
			
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			// 查找第一条记录
			operateStatus = cursor.getFirst(entryKey, entryValue, LockMode.DEFAULT);
			// 若找到该记录
			list = new ArrayList<T>();
			while (operateStatus == OperationStatus.SUCCESS) {
				EntryBinding<T> valueBinding = new SerialBinding<T>(getClassCatalog(), targetClass);
				T t = valueBinding.entryToObject(entryValue);
				list.add(t);
				// 游标往下移动,不停的next,直到游标指向记录为空
				operateStatus = cursor.getNext(entryKey, entryValue, LockMode.DEFAULT);
			}
		} catch (DatabaseException e) {
			//do nothing
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 根据key查找[返回单个对象]
	 * 
	 * @param key
	 * @param targetClass
	 * @return
	 */
	public <T> T getByKey(byte[] key, Class<T> targetClass) {
		T t = null;
		DatabaseEntry entryKey = new DatabaseEntry(key);
		DatabaseEntry entryValue = new DatabaseEntry();
		OperationStatus operateStatus = getDatabase().get(null, entryKey, entryValue, LockMode.DEFAULT);
		if (operateStatus == OperationStatus.SUCCESS) {
			EntryBinding<T> valueBinding = new SerialBinding<T>(getClassCatalog(), targetClass);
			t = (T)valueBinding.entryToObject(entryValue);
		}
		return t;
	}
	
	/**
	 * 根据key和value查询
	 * @param <T>
	 * @param key
	 * @param value
	 * @param targetClass
	 * @return
	 */
	public <T> T getByKeyAndValue(byte[] key,byte[] value, Class<T> targetClass) {
		DatabaseEntry entryKey = new DatabaseEntry(key);
		DatabaseEntry entryValue = new DatabaseEntry(value);
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		try {
			//查询不需要开启事务
			
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			// 查找第一条记录
			operateStatus = cursor.getSearchBoth(entryKey, entryValue, LockMode.DEFAULT);
			if (operateStatus == OperationStatus.NOTFOUND) {
				return null;
			}
			EntryBinding<T> valueBinding = new SerialBinding<T>(getClassCatalog(), targetClass);
			return (T)valueBinding.entryToObject(entryValue);
		} catch (DatabaseException e) {
			//do nothing
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * 查询所有
	 * @param <T>
	 * @param targetClass
	 * @return
	 */
	public <T> List<T> findAll(Class<T> targetClass) {
		// 输入参数
		DatabaseEntry entryKey = new DatabaseEntry();
		// 输出参数
		DatabaseEntry entryValue = new DatabaseEntry();
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		List<T> list = null;
		try {
			//查询不需要开启事务
			
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			// 查找第一条记录
			operateStatus = cursor.getFirst(entryKey, entryValue, LockMode.DEFAULT);
			// 若找到该记录
			list = new ArrayList<T>();
			while (operateStatus == OperationStatus.SUCCESS) {
				EntryBinding<T> valueBinding = new SerialBinding<T>(getClassCatalog(), targetClass);
				T t = valueBinding.entryToObject(entryValue);
				list.add(t);
				// 游标往下移动,不停的next,直到游标指向记录为空
				operateStatus = cursor.getNext(entryKey, entryValue, LockMode.DEFAULT);
			}
		} catch (DatabaseException e) {
			//do nothing
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	/**
	 * 根据key删除记录
	 * 
	 * @param key
	 * @return 返回是否删除成功
	 */
	public boolean deleteByKey(byte[] key) {
		// 输入参数
		DatabaseEntry entryKey = new DatabaseEntry(key);
		// 输出参数
		DatabaseEntry entryValue = new DatabaseEntry();
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		try {
			// 若用户配置了开启事务支持,则开始事务
			if (ENABLE_TRANSACTION) {
				txn = env.beginTransaction(null, null);
			}
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			// 根据key找到该条记录并使游标指向该记录
			operateStatus = cursor.getSearchKey(entryKey, entryValue, LockMode.DEFAULT);
			// 若找到该记录
			if (operateStatus == OperationStatus.SUCCESS) {
				// 删除当前游标指向的记录
				operateStatus = cursor.delete();
				if (operateStatus == OperationStatus.SUCCESS) {
					// 返回删除成功
					return true;
				}
			}
		} catch (DatabaseException e) {
			if (txn != null) {
				// 事务回滚
				txn.abort();
				txn = null;
			}
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
			// 提交事务
			if (txn != null) {
				txn.commit();
			}
		}
		return false;
	}
	
	/**
	 * 删除所有数据
	 * @param key
	 * @return
	 */
	public boolean deleteAll() {
		// 输入参数
		DatabaseEntry entryKey = new DatabaseEntry();
		// 输出参数
		DatabaseEntry entryValue = new DatabaseEntry();
		Transaction txn = null;
		OperationStatus operateStatus = null;
		Cursor cursor = null;
		try {
			// 若用户配置了开启事务支持,则开始事务
			if (ENABLE_TRANSACTION) {
				txn = env.beginTransaction(null, null);
			}
			// 打开游标
			cursor = getDatabase().openCursor(txn, null);
			// 根据key找到该条记录并使游标指向该记录
			operateStatus = cursor.getFirst(entryKey, entryValue, LockMode.DEFAULT);
			// 若找到该记录
			while (operateStatus == OperationStatus.SUCCESS) {
				// 删除当前游标指向的记录
				operateStatus = cursor.delete();
				// 游标往下移动,不停的next,指向下一条
				operateStatus = cursor.getNext(entryKey, entryValue, LockMode.DEFAULT);
			}
			return true;
		} catch (DatabaseException e) {
			if (txn != null) {
				// 事务回滚
				txn.abort();
				txn = null;
			}
		} finally {
			// 关闭游标
			if (cursor != null) {
				cursor.close();
			}
			// 提交事务
			if (txn != null) {
				txn.commit();
			}
		}
		return false;
	}

	/**************************** 下面这些方法都不支持事务 begin *******************************************/
	public void putString(String key, String value, String defaultEncoding) {
		try {
			String charsetName = defaultEncoding == null ? DEFAULT_ENCODING : defaultEncoding;
			byte[] bytes = key.getBytes(charsetName);
			byte[] bytes2 = value.getBytes(charsetName);
			putByte(bytes, bytes2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteString(String key, String defaultEncoding) {
		try {
			String charsetName = defaultEncoding == null ? DEFAULT_ENCODING : defaultEncoding;
			byte[] bytes = key.getBytes(charsetName);
			deleteByte(bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void putByte(byte[] bytesKey, byte[] bytesValue) throws DatabaseException, IOException {
		DatabaseEntry theKey = new DatabaseEntry(bytesKey);
		DatabaseEntry theValue = new DatabaseEntry(bytesValue);
		database.put(null, theKey, theValue);

	}

	public void deleteByte(byte[] bytesKey) throws DatabaseException {
		DatabaseEntry theKey = new DatabaseEntry(bytesKey);
		database.delete(null, theKey);
	}

	public String getString(String key, String defaultEncoding) {
		String foundData = null;
		try {
			String charsetName = defaultEncoding == null ? DEFAULT_ENCODING : defaultEncoding;
			byte[] bytes = key.getBytes(charsetName);
			byte[] byter = getByte(bytes);
			if (byter != null) {
				foundData = new String(byter, charsetName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return foundData;
	}

	public void putInputStream(String key, InputStream inputStream, String defaultEncoding) {
		try {
			byte[] bytes = new byte[16384]; // 16KB
			ByteArrayOutputStream by = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				by.write(bytes);
			}
			String charsetName = defaultEncoding == null ? DEFAULT_ENCODING : defaultEncoding;
			putByte(key.getBytes(charsetName), by.toByteArray());
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void putInputStream(String key, InputStream inputStream) {
		putInputStream(key, inputStream, null);
	}

	public byte[] getByte(byte[] bytesKey) throws DatabaseException, IOException {
		byte[] bytesValue = null;
		DatabaseEntry theKey = new DatabaseEntry(bytesKey);
		DatabaseEntry theData = new DatabaseEntry();
		if (database.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			bytesValue = theData.getData();
		}
		return bytesValue;
	}

	/**************************** 上面这些方法都不支持事务 end *******************************************/

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public EnvironmentConfig getEnvConfig() {
		return envConfig;
	}

	public void setEnvConfig(EnvironmentConfig envConfig) {
		this.envConfig = envConfig;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public DatabaseConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	public Database getClassDB() {
		return classDB;
	}

	public void setClassDB(Database classDB) {
		this.classDB = classDB;
	}

	public ClassCatalog getClassCatalog() {
		return classCatalog;
	}

	public void setClassCatalog(ClassCatalog classCatalog) {
		this.classCatalog = classCatalog;
	}

	public static void main(String[] args) throws Exception {
		test5();
		
	}
	
	/**
	 * 根据key删除
	 */
	private static void test5() {
		BerkeleyDBUtils berkeleyDBUtils = BerkeleyDBUtils.getInstance();
		//初始化数据库环境
		berkeleyDBUtils.initialDB();
		//打开数据库
		berkeleyDBUtils.openDB();
		
		//插入3条测试数据
		/*WebURL webURL1 = new WebURL(111, "http://www.baidu.com", 1, (short)1);
		boolean result1 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL1.getId()), webURL1);
		System.out.println(result1);
		
		WebURL webURL2 = new WebURL(222, "http://www.google.com", 1, (short)1);
		boolean result2 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL2.getId()), webURL2);
		System.out.println(result2);
		
		WebURL webURL3 = new WebURL(333, "http://www.taobao.com", 1, (short)1);
		boolean result3 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL3.getId()), webURL3);
		System.out.println(result3);*/
		
		//插入3条数据后查询所有
		/*List<WebURL> list1 = berkeleyDBUtils.findAll(WebURL.class);
		for(WebURL wu : list1) {
			System.out.println(wu.getId() + ":" + wu.getUrl());
		}
		
		//根据key删除一条
		boolean success = berkeleyDBUtils.deleteByKey(GerneralUtils.int2ByteArray(webURL1.getId()));
		System.out.println("delete success?" + success);
		
		//删除后再次查询所有数据
		List<WebURL> list2 = berkeleyDBUtils.findAll(WebURL.class);
		for(WebURL wu : list2) {
			System.out.println(wu.getId() + ":" + wu.getUrl());
		}
		
		//最后删除所有数据
		berkeleyDBUtils.deleteAll();
		
		//删除所有数据后再次查询所有数据
		List<WebURL> list3 = berkeleyDBUtils.findAll(WebURL.class);
		if(null == list3 || list3.size() == 0) {
			System.out.println("Data is empty.");
		}
		for(WebURL wu : list3) {
			System.out.println(wu.getId() + ":" + wu.getUrl());
		}*/
		
		//关闭数据库
		berkeleyDBUtils.closeDB();
		//清理资源
		berkeleyDBUtils.cleanDB();
	}
	
	/**
	 * 测试根据key和value查询
	 * @throws Exception 
	 */
	private static void test4() throws Exception {
		BerkeleyDBUtils berkeleyDBUtils = BerkeleyDBUtils.getInstance();
		//初始化数据库环境
		berkeleyDBUtils.initialDB();
		//打开数据库
		berkeleyDBUtils.openDB();
		
		/*WebURL webURL1 = new WebURL(111, "http://www.baidu.com", 1, (short)1);
		boolean result1 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL1.getId()), webURL1);
		System.out.println(result1);
		
		WebURL webURL2 = new WebURL(222, "http://www.google.com", 1, (short)1);
		boolean result2 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL2.getId()), webURL2);
		System.out.println(result2);
		
		WebURL webURL3 = new WebURL(333, "http://www.taobao.com", 1, (short)1);
		boolean result3 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL3.getId()), webURL3);
		System.out.println(result3);
		
		List<WebURL> list = berkeleyDBUtils.findAll(WebURL.class);
		for(WebURL wu : list) {
			System.out.println(wu.getId() + ":" + wu.getUrl());
		}*/
		
		//关闭数据库
		berkeleyDBUtils.closeDB();
		//清理资源
		berkeleyDBUtils.cleanDB();
	}
	
	/**
	 * 测试数据库删除
	 */
	public static void test3() {
		BerkeleyDBUtils berkeleyDBUtils = BerkeleyDBUtils.getInstance();
		//初始化数据库环境
		berkeleyDBUtils.initialDB();
		berkeleyDBUtils.cleanAllData();
	}
	
	/**
	 * 测试重复key的插入,以及根据key查询多个
	 */
	private static void test2() {
		BerkeleyDBUtils berkeleyDBUtils = BerkeleyDBUtils.getInstance();
		//初始化数据库环境
		berkeleyDBUtils.initialDB();
		//打开数据库
		berkeleyDBUtils.openDB();
		
		/*WebURL webURL1 = new WebURL(111, "http://www.google.com", 1, (short)1);
		boolean result1 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL1.getId()), webURL1);
		System.out.println(result1);
		
		WebURL webURL2 = new WebURL(111, "http://www.baidu.com", 1, (short)1);
		boolean result2 = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL2.getId()), webURL2);
		System.out.println(result2);
		
		List<WebURL> list = berkeleyDBUtils.findByKey(GerneralUtils.int2ByteArray(111), WebURL.class);
		for(WebURL wu : list) {
			System.out.println(wu.getId() + ":" + wu.getUrl());
		}*/
		
		//关闭数据库
		berkeleyDBUtils.closeDB();
		//清理资源
		berkeleyDBUtils.cleanDB();
	}
	
	/**
	 * 测试数据插入和根据key的单个查询
	 */
	private static void test1() {
		BerkeleyDBUtils berkeleyDBUtils = BerkeleyDBUtils.getInstance();
		//初始化数据库环境
		berkeleyDBUtils.initialDB();
		//打开数据库
		berkeleyDBUtils.openDB();
		
		/*WebURL webURL = new WebURL(111, "http://www.baidu.com", 1, (short)1);
		boolean result = berkeleyDBUtils.save(GerneralUtils.int2ByteArray(webURL.getId()), webURL);
		System.out.println(result);
		
		WebURL wu = berkeleyDBUtils.getByKey(GerneralUtils.int2ByteArray(111),WebURL.class);
		System.out.println(wu.getId() + " --> " + wu.getUrl());*/
		
		//关闭数据库
		berkeleyDBUtils.closeDB();
		//清理资源
		berkeleyDBUtils.cleanDB();
	}
}
