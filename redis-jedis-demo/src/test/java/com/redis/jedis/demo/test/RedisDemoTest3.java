package com.redis.jedis.demo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * Redis数据类型
 * 
 * 哈希表 hash
 * 
 * 函数介绍
 * 
 * Long hset(final String key, final String field, final String value)
 *     存放hash类型的数据
 * 
 * 
 * String hmset(final String key, final Map<String, String> hash)
 *     存放hash类型的数据
 * 
 * 
 * Long hsetnx(final String key, final String field, final String value)
 *     当字段不存在时赋值
 * 
 * 
 * String hget(final String key, final String field)
 *     查询hash类型的数据
 * 
 * 
 * List<String> hmget(final String key, final String... fields)
 *     查询hash类型的数据
 * 
 * 
 * Map<String, String> hgetAll(final String key)
 *     获取散列类型hash的值
 * 
 * 
 * Set<String> keys(final String pattern)
 *     获取所有的键
 * 例如，
 * 	jedis.keys("*")
 * 
 * 
 * Set<String> hkeys(final String key)
 *     获取散列类型hash中Map的所有key
 * 
 * 
 * List<String> hvals(final String key)
 *     获取散列类型hash中Map的所有value
 * 
 * 
 * Long hdel(final String key, final String... fields)
 *     删除字段
 * 
 * 
 * Boolean hexists(final String key, final String field)
 *     判断字段是否存在
 * 
 * 
 * Long hlen(final String key)
 *     获取散列类型hash中Map的键值对数量（map的长度）
 * 
 * 
 * Long hincrBy(final String key, final String field, final long value)
 *     为哈希表 key 中的域 field 的值加上增量 increment
 * 
 */
public class RedisDemoTest3 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest3.class);

	@Before
	public void setUp() throws Exception {
		logger.debug("单元测试");
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 */
	@Test
	public void hsetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.hset("employee", "name", "zxb");
		jedis.hset("employee", "age", "29");
		jedis.hset("employee", "gender", "1");
		jedis.hset("employee", "password", "mjcy@1989");

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 */
	@Test
	public void hgetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String empName = jedis.hget("employee", "name");
		String empAge = jedis.hget("employee", "age");
		String empGender = jedis.hget("employee", "gender");
		String empPassword = jedis.hget("employee", "password");

		logger.debug("name：" + empName);
		logger.debug("age：" + empAge);
		logger.debug("gender：" + empGender);
		logger.debug("password：" + empPassword);

		jedis.close();
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 */
	@Test
	public void hmsetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Map<String, String> hash = new HashMap<String, String>();
		hash.put("name", "zxb");
		hash.put("age", "29");
		hash.put("gender", "1");
		hash.put("password", "mjcy@1989");

		jedis.hmset("person", hash);
		logger.debug("存放的数据：" + hash);

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 */
	@Test
	public void hmgetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		List<String> list = jedis.hmget("person", new String[] { "name", "age", "gender", "password" });
		logger.debug("person：" + list);

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。
	 * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 */
	@Test
	public void hgetAllTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Map<String, String> map = jedis.hgetAll("employee");
		logger.debug("employee：" + map);

		map = jedis.hgetAll("person");
		logger.debug("person：" + map);

		jedis.close();
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 */
	@Test
	public void hexistsTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Boolean hexists = jedis.hexists("employee", "qitiandasheng");
		logger.debug("employee-name是否存在：" + hexists);

		jedis.close();
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
	 * 若域 field 已经存在，该操作无效。
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 */
	@Test
	public void hsetnxTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.hsetnx("employee", "birthday", "1989-01-01");

		jedis.close();
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key 。
	 * KEYS * 匹配数据库中所有 key 。
	 * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
	 * KEYS h*llo 匹配 hllo 和 heeeeello 等。
	 * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
	 * 特殊符号用 \ 隔开
	 */
	@Test
	public void keysTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Set<String> keys = jedis.keys("*");
		logger.debug("获取所有的键：" + keys);

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 */
	@Test
	public void hkeysTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Set<String> keys = jedis.hkeys("person");
		logger.debug("获取散列类型hash中Map的所有key：" + keys);

		keys = jedis.hkeys("employee");
		logger.debug("获取散列类型hash中Map的所有key：" + keys);

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 */
	@Test
	public void hvalsTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		List<String> vals = jedis.hvals("person");
		logger.debug("获取散列类型hash中Map的所有value：" + vals);

		jedis.close();
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 */
	@Test
	public void hdelTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		// Long hdel = jedis.hdel("person", "gender");//删除单个
		Long hdel = jedis.hdel("person", new String[] { "name", "gender" });// 删除多个
		logger.debug("删除字段：" + hdel);

		jedis.close();
	}

	/**
	 * 返回哈希表 key 中域的数量。
	 */
	@Test
	public void hlenTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long len = jedis.hlen("employee");
		logger.debug("employee中的元素数量：" + len);

		len = jedis.hlen("person");
		logger.debug("person中的元素数量：" + len);

		jedis.close();
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * 规律：每次执行，值递增1
	 * 场景：网站访问量统计
	 */
	@Test
	public void hincrByTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.hincrBy("numeric", "money", 1);
		Map<String, String> map = jedis.hgetAll("numeric");
		logger.debug(map);

		jedis.close();
	}

}
