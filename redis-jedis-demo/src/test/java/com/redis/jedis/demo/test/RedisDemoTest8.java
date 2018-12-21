package com.redis.jedis.demo.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * redis 服务器命令
 * 
 * String ping()
 *     使用客户端向 Redis 服务器发送一个 PING ，如果服务器运作正常的话，会返回一个 PONG 。
 * 
 * 
 * String echo(final String string)
 *     打印一个特定的信息 message ，测试时使用。
 * 
 * 
 * String select(final int index)
 *     切换到指定的数据库，数据库索引号 index 用数字值指定，以 0 作为起始索引值。
 * 
 * 
 * String quit()
 *     请求服务器关闭与当前客户端的连接。
 * 
 * 
 * Long dbSize()
 *     返回当前数据库的 key 的数量。
 * 
 * 
 * String info()
 *     以一种易于解释（parse）且易于阅读的格式，返回关于 Redis 服务器的各种信息和统计数值。
 * 
 * 
 * String flushAll()
 *     清空整个 Redis 服务器的数据(删除所有数据库的所有 key )。
 * 
 * 
 * String flushDB()
 *     清空当前数据库中的所有 key。
 * 
 */
public class RedisDemoTest8 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest8.class);

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
	 * 使用客户端向 Redis 服务器发送一个 PING ，如果服务器运作正常的话，会返回一个 PONG 。
	 * 通常用于测试与服务器的连接是否仍然生效，或者用于测量延迟值。
	 * 
	 * 返回值：
	 *     如果连接正常就返回一个 PONG ，否则返回一个连接错误。
	 */
	@Test
	public void pingTest() {
		String ping = jedis.ping();
		logger.info(ping);
	}

	/**
	 * 打印一个特定的信息 message ，测试时使用。
	 */
	@Test
	public void echoTest() {
		String result = jedis.echo("打印字符串");
		logger.info(result);
	}

	/**
	 * 切换到指定的数据库，数据库索引号 index 用数字值指定，以 0 作为起始索引值。
	 * 默认使用 0 号数据库。
	 */
	@Test
	public void selectTest() {
		String select = jedis.select(13);
		logger.info(select);
	}

	/**
	 * 请求服务器关闭与当前客户端的连接。
	 * 一旦所有等待中的回复(如果有的话)顺利写入到客户端，连接就会被关闭。
	 * 
	 * 返回值：
	 *     总是返回 OK (但是不会被打印显示，因为当时 Redis-cli 已经退出)。
	 */
	@Test
	public void quitTest() {
		String quit = jedis.quit();
		logger.info(quit);
	}

	/**
	 * 返回当前数据库的 key 的数量。
	 */
	@Test
	public void dbsizeTest() {
		Long dbSize = jedis.dbSize();
		logger.info(dbSize);
	}

	/**
	 * 以一种易于解释（parse）且易于阅读的格式，返回关于 Redis 服务器的各种信息和统计数值。
	 * 
	 * 通过给定可选的参数 section ，可以让命令只返回某一部分的信息：
	 * 1、server 部分记录了 Redis 服务器的信息
	 * 2、clients 部分记录了已连接客户端的信息
	 * 3、memory 部分记录了服务器的内存信息
	 * 4、persistence 部分记录了跟 RDB 持久化和 AOF 持久化有关的信息
	 * 5、stats 部分记录了一般统计信息
	 * 6、replication : 主/从复制信息
	 * 7、cpu 部分记录了 CPU 的计算量统计信息
	 * 8、commandstats 部分记录了各种不同类型的命令的执行统计信息，比如命令执行的次数、命令耗费的 CPU 时间、执行每个命令耗费的平均 CPU 时间等等。
	 * 9、cluster 部分记录了和集群有关的信息
	 * 10、keyspace 部分记录了数据库相关的统计信息，比如数据库的键数量、数据库已经被删除的过期键数量等。
	 * 11、all : 返回所有信息
	 * 12、default : 返回默认选择的信息
	 * 
	 * 当不带参数直接调用 INFO 命令时，使用 default 作为默认参数。
	 */
	@Test
	public void infoTest() {
		String info = jedis.info();
		logger.info(info);

		String cpu = jedis.info("cpu");
		logger.info(cpu);
	}

	/**
	 * 清空整个 Redis 服务器的数据(删除所有数据库的所有 key )。
	 * 此命令从不失败。
	 */
	@Test
	public void flushAllTest() {
		jedis.flushAll();
	}

	/**
	 * 清空当前数据库中的所有 key。
	 * 此命令从不失败。
	 */
	@Test
	public void flushDBTest() {
		jedis.flushDB();
	}

}
