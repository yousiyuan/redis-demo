package com.redis.cluster.demo.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 连接redis集群
 */
public class JedisClusterTest {

	private final static Logger logger = LogManager.getLogger();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 连接redis集群
	 */
	@Test
	public void test() throws IOException {
		// 集群结点
		Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6379));
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6380));
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6381));
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6382));
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6383));
		jedisClusterNode.add(new HostAndPort("192.168.1.108", 6384));

		// JedisPoolConfig config = new JedisPoolConfig();
		// 最大连接数
		// config.setMaxTotal(30);
		// 最大连接空闲数
		// config.setMaxIdle(2);
		// JedisCluster jc = new JedisCluster(jedisClusterNode, config);

		JedisCluster jcd = new JedisCluster(jedisClusterNode);
		String value = jcd.get("test");// 集群搭建成功时，已经设置了用于测试的数据：test
		logger.info(value);

		jcd.set("test", "我是流年公子");
		value = jcd.get("test");
		logger.info(value);

		/**
		 * 查询集群节点信息
		 */
		Map<String, JedisPool> clusterNodes = jcd.getClusterNodes();
		logger.info(clusterNodes);
		jcd.close();
	}

}
