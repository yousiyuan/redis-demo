package com.redis.jedis.demo.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * redis中的其它命令
 * 
 * Long expire(final String key, final int seconds)
 *     设置Key的生存时间
 * 
 * 
 * Long ttl(final String key)
 *     以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
 * 
 * 
 * Set<String> keys(final String pattern)
 *     查找所有符合给定模式 pattern 的 key 。
 * 
 * 
 * Boolean exists(final String key)
 *     检查给定 key 是否存在。
 * 
 * 
 * Long del(String key)
 *     删除给定的一个或多个 key 。
 * 
 * String rename(final String oldkey, final String newkey)
 *     将 key 改名为 newkey 。
 * 
 * 
 * String type(final String key)
 *     返回 key 所储存的值的类型。
 * 
 */
public class RedisDemoTest7 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest7.class);

	Jedis jedis = null;

	@Before
	public void setUp() throws Exception {
		logger.info("单元测试===》开始");
		jedis = new Jedis("192.168.1.106", 6380);
		jedis.select(13);
	}

	@After
	public void tearDown() throws Exception {
		jedis.close();
		logger.info("单元测试===》结束");
	}

	/**
	 * 设置Key的生存时间
	 */
	@Test
	public void expireTest() throws InterruptedException {
		// 设置一个用于测试的key
		String result = jedis.set("name", "liunianson");
		logger.error(result);

		// 设置Key的生存时间
		Long expire = jedis.expire("name", 15);
		logger.error(expire);

		Long ttl = 0L;
		// 获取Key的生存时间
		while ((ttl = jedis.ttl("name")) != -2L) {
			logger.error(ttl);
			Thread.sleep(1000);
		}

		logger.error("key name is expire");

		result = jedis.get("name");
		logger.error(result);
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key 。
	 * 
	 * KEYS * 匹配数据库中所有 key 。
	 * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
	 * KEYS h*llo 匹配 hllo 和 heeeeello 等。
	 * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
	 * 特殊符号用 \ 隔开
	 */
	@Test
	public void keysTest() {
		Set<String> keys = jedis.keys("*");
		logger.error("获取所有的键：" + keys);
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * 返回值：
	 *     若 key 存在，返回 1 ，否则返回 0 。
	 */
	@Test
	public void existsTest() {
		// String
		jedis.set("timeout", "10000");// String
		// hash
		jedis.hmset("person", new HashMap<String, String>() {
			private static final long serialVersionUID = -8833344150763511686L;
			{
				put("name", "zxb");
				put("age", "29");
				put("gender", "1");
				put("password", "mjcy@1989");
			}
		});
		// list
		jedis.rpush("productlist", new String[] { "p001", "p002", "p003", "p004", "p005" });
		// set
		jedis.sadd("letters", new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
				"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" });
		// SortedSet
		jedis.zadd("students", new HashMap<String, Double>() {
			private static final long serialVersionUID = 3443581695946627251L;
			{
				put("stu001", 67.50d);
				put("stu005", 88.45);
				put("stu0017", 93d);
			}
		});
		Long exists = jedis.exists(new String[] { "timeout", "person", "productlist", "letters", "students" });
		logger.error(exists);

		Boolean exists2 = jedis.exists("productlist");
		logger.error(exists2);

		Boolean exists3 = jedis.exists("不存在的key");
		logger.error(exists3);
	}

	/**
	 * 删除给定的一个或多个 key 。
	 * 不存在的 key 会被忽略。
	 */
	@Test
	public void delTest() {
		Long del = jedis.del("productlist");
		logger.error(del);
		
		del = jedis.del("letters");
		logger.error(del);
		
		del = jedis.del("timeout");
		logger.error(del);
	}

	/**
	 * 将 key 改名为 newkey 。
	 * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。
	 * 当 newkey 已经存在时， RENAME 命令将覆盖旧值。【被重命名的key会覆盖已经存在的newkey】
	 */
	@Test
	public void renameTest() {
		// 打印students的值
		Set<Tuple> items = jedis.zrangeWithScores("students", 0, -1);
		Map<String, Double> map = new LinkedHashMap<String, Double>();
		items.forEach(tuple -> {
			map.put(tuple.getElement(), tuple.getScore());
		});
		logger.error(map);

		// 重命名key
		String rename = jedis.rename("students", "ren");
		logger.error(rename);

		// 打印重命名后的key
		items = jedis.zrangeWithScores("ren", 0, -1);
		Map<String, Double> renmap = new LinkedHashMap<String, Double>();
		items.forEach(tuple -> {
			renmap.put(tuple.getElement(), tuple.getScore());
		});
		logger.error(renmap);

		// ===========================================================

		// 先打印要被重命名的key
		Map<String, String> hgetAll = jedis.hgetAll("person");
		logger.error(hgetAll);

		// 重命名key
		rename = jedis.rename("person", "ren");
		logger.error(rename);

		// 打印重命名后的key
		hgetAll = jedis.hgetAll("ren");
		logger.error(hgetAll);
	}

	/**
	 * 返回 key 所储存的值的类型。
	 * none (key不存在)
	 * string (字符串)
	 * list (列表)
	 * set (集合)
	 * zset (有序集)
	 * hash (哈希表)
	 */
	@Test
	public void typeTest() {
		String type = jedis.type("ren");
		logger.error(type);

		type = jedis.type("letters");
		logger.error(type);

		type = jedis.type("timeout");
		logger.error(type);

		type = jedis.type("productlist");
		logger.error(type);

		type = jedis.type("students");
		logger.error(type);

		type = jedis.type("person");
		logger.error(type);
	}


}
