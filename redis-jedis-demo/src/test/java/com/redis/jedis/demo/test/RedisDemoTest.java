package com.redis.jedis.demo.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * java通过jedis连接redis
 */
public class RedisDemoTest {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest.class);

	@Before
	public void setUp() throws Exception {
		logger.debug("单元测试");
	}

	/**
	 * 存储值
	 */
	@Test
	public void setValueTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6379);
		jedis.set("address", "北京");
		jedis.close();
	}

	/**
	 * 获取值
	 */
	@Test
	public void getValueTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6379);
		String address = jedis.get("address");
		logger.debug("address:" + address);
		jedis.close();
	}

	/**
	 * 使用连接池获得redis数据库的连接
	 */
	@Test
	public void dbPoolTest() {
		// 创建redis连接池配置对象
		JedisPoolConfig jpc = new JedisPoolConfig();
		jpc.setMaxIdle(20);
		jpc.setMaxTotal(50);

		// 创建连接池对象
		JedisPool jp = new JedisPool(jpc, "192.168.1.106", 6380);
		// 从连接池中获取一个连接
		Jedis jedis = jp.getResource();
		String name = jedis.get("name");
		logger.debug(name);

		// 归还连接到连接池
		jedis.close();

		// 释放连接池对象
		jp.close();
	}
}
