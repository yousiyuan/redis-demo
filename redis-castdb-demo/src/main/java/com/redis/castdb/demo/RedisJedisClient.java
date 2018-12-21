package com.redis.castdb.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/* -- 数据库表之间的关系：
 * -- 1、一对多
 * -- 2、多对一
 * -- 3、多对多
 * -- 4、一对一
 * 
 * -- 如何在redis中存储关系型数据库表中的数据：
 * -- 1、针对每张表的数据，使用list存储所有的主键。列表list的key命名规范：表名:[主键字段名]
 * -- 2、每张表的每条记录使用hash存储。哈希hash的key命名规范：表名:[主键字段值]
 * -- 3、建立一对多的关系。Key的命名规范：[主键表名称]:[外键字段的值]:[外键表名称]
 */
public class RedisJedisClient {

	private Jedis jedis;
	private JedisPool jedisPool;

	protected RedisJedisClient(Builder builder) {
		this.jedis = builder.jedis;
		this.jedisPool = builder.jedisPool;
	}

	protected RedisJedisClient(Jedis jedis) {
		this.jedis = jedis;
	}

	public synchronized RedisJedisClient createNewInstance() {
		RedisJedisClient redisJedisClient = new RedisJedisClient(this.jedisPool.getResource());
		redisJedisClient.jedisPool = this.jedisPool;
		return redisJedisClient;
	}

	/**
	 * 获得Jedis对象
	 */
	public Jedis getJedis() {
		return this.jedis;
	}

	/**
	 * 归还jedis对象到连接池
	 */
	public synchronized void close() {
		if (this.jedis != null)
			this.jedis.close();
	}

	/**
	 * 关闭Jedis连接池
	 */
	public void closeJedisPool() {
		if (this.jedisPool != null)
			this.jedisPool.close();
	}

	/**
	 * 保存表主键
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldName
	 *            主键字段名
	 * @param pkFieldValueList
	 *            主键字段值集合
	 */
	public void savePrimaryKey(String tableName, String pkFieldName, String... pkFieldValueList) {
		jedis.rpush(MessageFormat.format("{0}:{1}", tableName, pkFieldName), pkFieldValueList);
	}

	/**
	 * 保存表主键
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldName
	 *            主键字段名
	 * @param pkFieldValueList
	 *            主键字段值集合
	 */
	public void savePrimaryKey(String tableName, String pkFieldName, List<String> pkFieldValueList) {
		jedis.rpush(MessageFormat.format("{0}:{1}", tableName, pkFieldName),
				pkFieldValueList.toArray(new String[pkFieldValueList.size()]));
	}

	/**
	 * 主键表(tableNamePK)的主键字段fkValue在外键表(tableNameFK)中做外键，外键表(tableNameFK)有一个依赖表(tableNamePK)的字段fkValue。
	 * 
	 * @param pkTableName
	 *            主键表的名称
	 * @param fkTableName
	 *            外键表的名称
	 * @param fkValue
	 *            外键字段值
	 * @param fkTableIdList
	 *            外键表中主键标识列值的集合
	 */
	public void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue,
			String... fkTableIdList) {
		String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);
		jedis.rpush(key, fkTableIdList);
	}

	/**
	 * 主键表(tableNamePK)的主键字段fkValue在外键表(tableNameFK)中做外键，外键表(tableNameFK)有一个依赖表(tableNamePK)的字段fkValue。
	 * 
	 * @param pkTableName
	 *            主键表的名称
	 * @param fkTableName
	 *            外键表的名称
	 * @param fkValue
	 *            外键字段值
	 * @param fkTableIdList
	 *            外键表中主键标识列值的集合
	 */
	public void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue,
			List<String> fkTableIdList) {
		String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);
		jedis.rpush(key, fkTableIdList.toArray(new String[fkTableIdList.size()]));
	}

	/**
	 * 保存记录
	 * 
	 * @param entity
	 *            数据实体
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldValue
	 *            数据库表主键字段值
	 */
	public void saveObject(final Object entity, String tableName, String pkFieldValue) throws Exception {
		String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);
		Class<? extends Object> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			String getMethodName = MessageFormat.format("get{0}{1}", Character.toUpperCase(name.charAt(0)),
					name.substring(1));
			Method method = clazz.getDeclaredMethod(getMethodName, new Class<?>[] {});
			Object value = method.invoke(entity, new Object[] {});
			jedis.hset(key, name, value.toString());
		}
	}

	/**
	 * 保存记录
	 * 
	 * @param entity
	 *            数据实体
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldValue
	 *            数据库表主键字段值
	 */
	public void saveObject(final Map<String, String> entity, String tableName, String pkFieldValue) {
		String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);

		Set<String> fields = entity.keySet();
		for (String field : fields) {
			String value = entity.get(field);
			jedis.hset(key, field, value);
		}
	}

	/**
	 * 获取主键集合
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldName
	 *            主键字段名
	 * @return 数据库表主键值列表
	 */
	public List<String> queryPrimaryKeyFieldValueList(String tableName, String pkFieldName) {
		String key = MessageFormat.format("{0}:{1}", tableName, pkFieldName);
		List<String> primaryKeyList = jedis.lrange(key, 0, -1);
		return primaryKeyList;
	}

	/**
	 * 获得一条记录
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldValue
	 *            数据库表主键字段值
	 * @return 返回 Field-Value 对集合
	 */
	public Map<String, String> queryForObject(String tableName, String pkFieldValue) {
		String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);
		Map<String, String> fieldValue = jedis.hgetAll(key);
		return fieldValue;
	}

	/**
	 * 获得一条记录
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param pkFieldValue
	 *            数据库表主键字段值
	 * @param clazz
	 *            POJO类型
	 */
	public <T> T queryForObject(String tableName, String pkFieldValue, Class<? extends T> clazz) throws Exception {
		Map<String, String> objMap = this.queryForObject(tableName, pkFieldValue);

		Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[] {});
		Object instance = constructor.newInstance(new Object[] {});
		Field[] fieldArray = clazz.getDeclaredFields();
		for (Field field : fieldArray) {
			Class<?> fieldType = field.getType();
			String name = field.getName();
			String value = objMap.get(name);
			String setMethodName = MessageFormat.format("set{0}{1}", Character.toUpperCase(name.charAt(0)),
					name.substring(1));
			Method method = clazz.getDeclaredMethod(setMethodName, new Class<?>[] { fieldType });

			if (fieldType.equals(Short.class))
				method.invoke(instance, Short.valueOf(value.toString()));
			else if (fieldType.equals(Integer.class))
				method.invoke(instance, Integer.valueOf(value.toString()));
			else if (fieldType.equals(Long.class))
				method.invoke(instance, Long.valueOf(value.toString()));
			else if (fieldType.equals(Float.class))
				method.invoke(instance, Float.valueOf(value.toString()));
			else if (fieldType.equals(Double.class))
				method.invoke(instance, Double.valueOf(value.toString()));
			else if (fieldType.equals(Boolean.class))
				method.invoke(instance, Boolean.valueOf(value.toString()));
			else if (fieldType.equals(String.class))
				method.invoke(instance, value.toString());
			else if (fieldType.equals(Date.class))
				method.invoke(instance,
						new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(value.toString()));
		}

		T entity = clazz.cast(instance);
		return entity;
	}

	/**
	 * 查询数据库表所有记录的列表
	 * 
	 * @param tableName
	 *            数据库表名称
	 * @param pkFieldName
	 *            数据库表主键字段名
	 */
	public List<Map<String, String>> queryForList(String tableName, String pkFieldName) {
		List<String> pkFieldValueList = this.queryPrimaryKeyFieldValueList(tableName, pkFieldName);
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		for (String pkValue : pkFieldValueList) {
			Map<String, String> objectMap = this.queryForObject(tableName, pkValue);
			list.add(objectMap);
		}
		return list;
	}

	/**
	 * 查询数据库表所有记录的列表
	 * 
	 * @param tableName
	 *            数据库表名称
	 * @param pkFieldName
	 *            数据库表主键字段名称
	 * @param clazz
	 *            POJO类型
	 */
	public <T> List<T> queryForList(String tableName, String pkFieldName, Class<? extends T> clazz) throws Exception {
		List<String> pkFieldValueList = this.queryPrimaryKeyFieldValueList(tableName, pkFieldName);
		List<T> list = new LinkedList<T>();
		for (String pkValue : pkFieldValueList) {
			T object = queryForObject(tableName, pkValue, clazz);
			list.add(object);
		}
		return list;
	}

	/**
	 * 根据外键查询列表
	 * 
	 * @param pkTableName
	 *            主键表名称
	 * @param fkTableName
	 *            外键表名称
	 * @param fkValue
	 *            外键字段的值
	 */
	public List<Map<String, String>> queryForList(String pkTableName, String fkTableName, String fkValue) {
		String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);

		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		List<String> fkTableIdList = jedis.lrange(key, 0, -1);
		for (String fkTableId : fkTableIdList) {
			Map<String, String> objMap = this.queryForObject(fkTableName, fkTableId);
			list.add(objMap);
		}
		return list;
	}

	/**
	 * 根据外键查询列表
	 * 
	 * @param pkTableName
	 *            主键表名称
	 * @param fkTableName
	 *            外键表名称
	 * @param fkValue
	 *            外键字段的值
	 * @param clazz
	 *            外键表在java中对应的数据实体类型
	 */
	public <T> List<T> queryForList(String pkTableName, String fkTableName, String fkValue, Class<? extends T> clazz)
			throws Exception {
		String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);

		List<T> list = new LinkedList<T>();
		List<String> fkTableIdList = jedis.lrange(key, 0, -1);
		for (String fkTableId : fkTableIdList) {
			T object = this.queryForObject(fkTableName, fkTableId, clazz);
			list.add(object);
		}
		return list;
	}

	/**
	 * Redis数据库客户端Jedis的构造器
	 */
	public static class Builder {

		protected Jedis jedis;
		private JedisPoolConfig jedisPoolConfig;
		protected JedisPool jedisPool;

		private String host;
		private Integer port;
		private Integer db = 0;

		/**
		 * Redis数据库客户端Jedis的构造器
		 * 
		 * @param host
		 *            Redis服务器IP
		 * @param port
		 *            Redis服务实例端口
		 */
		public Builder(String host, Integer port) {
			this.host = host;
			this.port = port;

			this.jedisPoolConfig = new JedisPoolConfig();
		}

		/**
		 * 创建Redis数据库的客户端对象Jedis
		 */
		public synchronized RedisJedisClient build() {
			this.jedisPool = new JedisPool(jedisPoolConfig, this.host, this.port);
			this.jedis = this.jedisPool.getResource();
			this.jedis.select(this.db);

			return new RedisJedisClient(this);
		}

		/**
		 * 选择使用哪个数据库
		 */
		public Builder selectDataBase(Integer db) {
			this.db = db;
			return this;
		}

		/**
		 * 最大连接数, 默认8个
		 */
		public Builder setMaxTotal(Integer maxTotal) {
			this.jedisPoolConfig.setMaxTotal(maxTotal);
			return this;
		}

		/**
		 * 最大空闲连接数, 默认8个
		 */
		public Builder setMaxIdle(Integer maxIdle) {
			this.jedisPoolConfig.setMaxIdle(maxIdle);
			return this;
		}

		/**
		 * 最小空闲连接数, 默认0
		 */
		public Builder setMinIdle(Integer minIdle) {
			this.jedisPoolConfig.setMinIdle(minIdle);
			return this;
		}

	}

}
