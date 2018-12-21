package com.redis.jedis.demo.test;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

/**
 * Redis数据类型
 * 
 * 列表 list
 * 
 * 函数介绍
 * 
 */
public class RedisDemoTest4 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest4.class);

	@Before
	public void setUp() throws Exception {
		logger.debug("单元测试");
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，
	 * 列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	@Test
	public void lpushTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.lpush("userlist", new String[] { "u001", "u002", "u003", "u004", "u005" });

		jedis.close();
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，
	 * 得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	@Test
	public void rpushTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		jedis.rpush("productlist", new String[] { "p001", "p002", "p003", "p004", "p005" });

		jedis.close();
	}

	/**
	 * 移除并返回列表 key 的尾元素。
	 */
	@Test
	public void rpopTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String item = jedis.rpop("productlist");
		logger.fatal(item);

		jedis.close();
	}

	/**
	 * 移除并返回列表 key 的头元素。
	 */
	@Test
	public void lpopTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String item = jedis.lpop("productlist");
		logger.fatal(item);

		jedis.close();
	}

	/**
	 * 返回列表 key 的长度。
	 */
	@Test
	public void llenTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long len = jedis.llen("productlist");
		logger.fatal(len);

		jedis.close();
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 */
	@Test
	public void lrangeTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		List<String> list = jedis.lrange("productlist", 0, -1);
		logger.fatal(list);

		jedis.close();
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 */
	@Test
	public void lremTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long count = jedis.lrem("userlist", 1, "u002");
		logger.fatal(count);

		jedis.close();
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 */
	@Test
	public void lindexTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String item = jedis.lindex("userlist", 0);
		logger.fatal(item);

		jedis.close();
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。
	 * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
	 */
	@Test
	public void lsetTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String lset = jedis.lset("userlist", 0, "头元素被修改");
		logger.fatal(lset);

		jedis.close();
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 */
	@Test
	public void ltrimTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String ltrim = jedis.ltrim("userlist", 2, 3);
		logger.fatal(ltrim);

		jedis.close();
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。
	 * 当 pivot 不存在于列表 key 时，不执行任何操作。
	 * 当 key 不存在时， key 被视为空列表，不执行任何操作。
	 */
	@Test
	public void linsertTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		Long linsert = jedis.linsert("userlist", LIST_POSITION.AFTER, "u001", "c009");
		logger.fatal(linsert);

		jedis.close();
	}

	/**
	 * 命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作：
	 *     1、将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。
	 *     2、将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。
	 * 
	 * 举个例子，你有两个列表 source 和 destination ， source 列表有元素 a, b, c ， destination 列表有元素 x, y, z ，
	 * 执行 RPOPLPUSH source destination 之后， source 列表包含元素 a, b ， destination 列表包含元素 c, x, y, z ，
	 * 并且元素 c 会被返回给客户端。
	 * 
	 * 如果 source 不存在，值 nil 被返回，并且不执行其他动作。
	 * 如果 source 和 destination 相同，则列表中的表尾元素被移动到表头，并返回该元素，可以把这种特殊情况视作列表的旋转(rotation)操作。
	 */
	@Test
	public void rpoplpushTest() {
		Jedis jedis = new Jedis("192.168.1.106", 6380);

		String rpoplpush = jedis.rpoplpush("productlist", "userlist");
		logger.fatal(rpoplpush);

		jedis.close();
	}

}
