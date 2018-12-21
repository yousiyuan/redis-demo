package com.redis.jedis.demo.test;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * Redis数据类型
 * 
 * 集合 set
 * 
 * 函数介绍
 * 
 */
public class RedisDemoTest5 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest5.class);

	@Before
	public void setUp() throws Exception {
		logger.debug("单元测试");
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 * 当 key 不是集合类型时，返回一个错误。
	 */
	@Test
	public void saddTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long size = jedis.sadd("letters", new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" });
		logger.fatal(size);

		jedis.close();
	}

	/**
	 * 返回集合 key 中的所有成员。
	 * 不存在的 key 被视为空集合。
	 */
	@Test
	public void smembersTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Set<String> members = jedis.smembers("letters");
		logger.fatal(members);

		jedis.close();
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * 当 key 不是集合类型，返回一个错误。
	 */
	@Test
	public void sremTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long size = jedis.srem("letters", new String[] { "a", "b", "c", "d", "e", "f", "g" });
		logger.fatal(size);

		jedis.close();
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 */
	@Test
	public void sismemberTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Boolean sismember = jedis.sismember("letters","f");
		logger.fatal(sismember);

		jedis.close();
	}

	/**
	 * 清空整个 Redis 服务器的数据(删除所有数据库的所有 key )。
	 * 此命令从不失败。
	 */
	@Test
	public void flushAllTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.flushAll();

		jedis.close();
	}

	/**
	 * 清空当前数据库中的所有 key。
	 * 此命令从不失败。
	 */
	@Test
	public void flushDBTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.select(0);
		jedis.flushDB();

		jedis.close();
	}

}
