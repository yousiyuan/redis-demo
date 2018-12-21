package com.redis.jedis.demo.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * Redis数据类型
 * 
 * 字符串 String
 * 
 * jedis 函数介绍
 * 
 * String set(final String key, String value)
 *     设置Key-Value对，若Key已存在则覆盖Value，可用于更新数据
 * 
 * 
 * String setex(final String key, final int seconds, final String value)
 *     设置Key的有效期并存储数据，seconds秒之后，Key失效，只能获取null值
 * 
 * 
 * Long setnx(final String key, final String value)
 *     若Key不存在，则存储
 * 
 * 
 * String mset(final String... keysvalues)
 *     设置多个值
 * 例如，
 *     jedis.mset(new String[] { "productname", "笔记本电脑", "store", "1000" })
 * 
 * 
 * String get(final String key)
 *     获取Key的Value，若Key不存在返回null，存在则返回Value
 * 
 * 
 * List<String> mget(final String... keys)
 *     获取多个值
 * 例如，
 *     jedis.mget(new String[] { "name", "age", "gender" })
 * 
 * 
 * String getrange(String key, long startOffset, long endOffset)
 *     截取value的值
 * 
 * 
 * Long del(final String... keys)
 *     删除若干个Key
 * 例如，
 *     del(new String[] { "foo", "foo1", "foo3" })
 * 
 * 
 * Long del(String key)
 *     删除Key
 * 
 * 
 * Long append(final String key, final String value)
 *     追加数据
 *     1、Key不存在，就将value作为初始值创建一条记录，键是Key
 *     2、Key存在，就将value追加到原字符串的末尾
 * 
 * 
 * String getSet(final String key, final String value)
 *     获取并更改数据
 * 
 * 
 * Boolean exists(final String key)
 *     判断Key是否存在
 * 
 * 
 * Long strlen(final String key)
 *     获取key的value字符长度
 * 
 * 
 * String flushDB()
 *     清空数据
 * 
 * 
 * String flushAll()
 *     删除数据库中所有已存在的Key
 * 
 * 
 * String echo(final String string)
 *     打印消息
 * 
 */
public class RedisDemoTest2 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest2.class);

	@Before
	public void setUp() throws Exception {
		logger.debug("单元测试");
	}

	/**
	 * 追加字符串（Key存在）
	 */
	@Test
	public void appendValueTest1() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String key = "name";
		String value = jedis.get(key);
		logger.debug(value);

		// 追加字符串
		jedis.append(key, ", hello world");
		value = jedis.get(key);
		logger.debug("追加后的值：" + value);

		jedis.close();
	}

	/**
	 * 追加字符串（Key不存在）
	 */
	@Test
	public void appendValueTest2() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String key = UUID.randomUUID().toString();
		String value = jedis.get(key);
		logger.debug(value);

		// 追加字符串
		jedis.append(key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		value = jedis.get(key);
		logger.debug("追加后的值：" + value);

		jedis.close();
	}

	/**
	 * 获取值的长度
	 */
	@Test
	public void strlenTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String key = "name";
		Long len = jedis.strlen(key);
		logger.debug("值的长度：" + len);

		jedis.close();
	}

	/**
	 * 设置多个值
	 */
	@Test
	public void msetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.mset(new String[] { "productname", "笔记本电脑", "store", "1000" });
		String productname = jedis.get("productname");
		String store = jedis.get("store");
		logger.debug(productname);
		logger.debug(store);

		jedis.close();
	}

	/**
	 * 获取多个值
	 */
	@Test
	public void mgetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		List<String> values = jedis.mget(new String[] { "name", "age", "gender" });
		logger.debug("获取多个值：" + values);

		jedis.close();
	}

	/**
	 * 判断键Key是否存在
	 * 打印字符串
	 */
	@Test
	public void jedisTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		//判断键是否存在
		Boolean exists = jedis.exists("name");
		logger.debug(exists);

		//打印字符串
		String name = jedis.echo("打印字符串");
		logger.debug(name);

		jedis.close();
	}

	/**
	 * 删除所有的Key
	 */
	@Test
	public void flushAllTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		// 删除数据库中所有已存在的Key
		String result = jedis.flushAll();
		logger.debug("删除所有的Key：" + result);

		jedis.close();
	}

}
