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
 * Redis数据类型
 * 
 * 有序集合 SortedSet
 * 
 * 函数介绍
 * 
 */
public class RedisDemoTest6 {

	private final static Logger logger = LogManager.getLogger(RedisDemoTest6.class);

	Jedis jedis = null;

	@Before
	public void setUp() throws Exception {
		logger.info("单元测试===》开始");
		jedis = new Jedis("192.168.1.106", 6380);
		jedis.select(15);
	}

	@After
	public void tearDown() throws Exception {
		jedis.close();
		logger.info("单元测试===》结束");
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 * score 值可以是整数值或双精度浮点数。
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 */
	@Test
	public void zaddTest() {
		jedis.zadd("xscj", 5.05d, "a");
		jedis.zadd("xscj", 10.55d, "b");
		jedis.zadd("xscj", 15.05d, "c");
		jedis.zadd("xscj", 20.55d, "d");
		jedis.zadd("xscj", 25.05d, "e");

		Map<String, Double> scoreMembers = new HashMap<String, Double>();
		scoreMembers.put("f", 30.55d);
		scoreMembers.put("g", 35.05d);
		scoreMembers.put("h", 40.55d);
		scoreMembers.put("i", 45.05d);
		scoreMembers.put("j", 50.55d);
		scoreMembers.put("k", 55.05d);
		scoreMembers.put("l", 60.55d);
		scoreMembers.put("m", 65.05d);
		scoreMembers.put("n", 70.55d);
		scoreMembers.put("o", 75.05d);
		scoreMembers.put("p", 80.55d);
		scoreMembers.put("q", 85.05d);
		scoreMembers.put("r", 90.55d);
		scoreMembers.put("s", 95.05d);
		scoreMembers.put("t", 100.55d);
		scoreMembers.put("u", 105.05d);
		scoreMembers.put("v", 110.55d);
		scoreMembers.put("w", 115.05d);
		scoreMembers.put("x", 120.55d);
		scoreMembers.put("y", 125.05d);
		scoreMembers.put("z", 130.55d);

		Long size = jedis.zadd("xscj", scoreMembers);
		logger.fatal(size);
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
	 */
	@Test
	public void zscoreTest() {
		Double score = jedis.zscore("xscj", "k");
		logger.fatal(score);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 超出范围的下标并不会引起错误。
	 */
	@Test
	public void zrangeTest() {
		Set<String> members = jedis.zrange("xscj", 0, -1);
		logger.fatal(members);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 超出范围的下标并不会引起错误。
	 */
	@Test
	public void zrevrangeTest() {
		Set<String> members = jedis.zrevrange("xscj", 0, -1);
		logger.fatal(members);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员和它的 score 值。
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 超出范围的下标并不会引起错误。
	 */
	@Test
	public void zrangeWithScoresTest() {
		Map<String, Double> map = new LinkedHashMap<String, Double>();

		Set<Tuple> members = jedis.zrangeWithScores("xscj", 0, -1);
		for (Tuple tuple : members) {
			map.put(tuple.getElement(), tuple.getScore());
		}

		logger.fatal(map);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员和它的 score 值。
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 超出范围的下标并不会引起错误。
	 */
	@Test
	public void zrevrangeWithScoresTest() {
		Map<String, Double> map = new LinkedHashMap<String, Double>();

		Set<Tuple> members = jedis.zrevrangeWithScores("xscj", 0, -1);
		for (Tuple tuple : members) {
			map.put(tuple.getElement(), tuple.getScore());
		}

		logger.fatal(map);
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 有序集成员按 score 值递增(从小到大)次序排列。
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 
	 * 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。（除特殊情况外不要使用）
	 * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。
	 */
	@Test
	public void zrangeByScoreTest() {
		Set<String> members = jedis.zrangeByScore("xscj", "55", "95");
		logger.fatal(members);

		members = jedis.zrangeByScore("xscj", 55d, 95d);
		logger.fatal(members);

		Set<Tuple> items = jedis.zrangeByScoreWithScores("xscj", "55", "95");
		Map<String, Double> map = new LinkedHashMap<String, Double>();
		items.forEach(tuple -> {
			map.put(tuple.getElement(), tuple.getScore());
		});
		logger.fatal(map);
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 有序集成员按 score 值递减(从大到小)次序排列。
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 
	 * 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。（除特殊情况外不要使用）
	 * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。
	 */
	@Test
	public void zrevrangeByScoreTest() {
		Set<String> members = jedis.zrevrangeByScore("xscj", "95", "55");
		logger.fatal(members);

		members = jedis.zrevrangeByScore("xscj", 95d, 55d);
		logger.fatal(members);

		Set<Tuple> items = jedis.zrevrangeByScoreWithScores("xscj", "95", "55");
		Map<String, Double> map = new LinkedHashMap<String, Double>();
		items.forEach(tuple -> {
			map.put(tuple.getElement(), tuple.getScore());
		});
		logger.fatal(map);
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
	 * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member。 
	 * 当 key 不是有序集类型时，返回一个错误。
	 * score 值可以是整数值或双精度浮点数。
	 */
	@Test
	public void zincrbyTest() {
		Double score = jedis.zincrby("xscj", 66.39d, "z");
		logger.fatal(score);
	}

	/**
	 * 返回有序集 key 的基数。
	 * 当 key 存在且是有序集类型时，返回有序集的基数。
	 * 当 key 不存在时，返回 0 。
	 */
	@Test
	public void zcardTest() {
		Long zcard = jedis.zcard("xscj");
		logger.fatal(zcard);// 26
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 zrangeByScore 这类命令。
	 */
	@Test
	public void zcountTest() {
		Long zcount = jedis.zcount("xscj", 55d, 95d);
		logger.fatal(zcount);
		
		zcount = jedis.zcount("xscj", "-inf", "+inf");
		logger.fatal(zcount);// 26
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 * 返回值：被成功移除的成员的数量，不包括被忽略的成员。
	 */
	@Test
	public void zremTest() {
		Long delCount = jedis.zrem("xscj", new String[] { "a", "b", "c", "d", "e", "f", "g" });
		logger.fatal("删除成员的数量：" + delCount);
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 */
	@Test
	public void zremrangeByRankTest() {
		Long size = jedis.zremrangeByRank("xscj", -2, -1);
		logger.fatal(size);// 删除 y、z最后两个成员

		size = jedis.zremrangeByRank("xscj", 0, 1);
		logger.fatal(size);// 删除h、i开头两个成员
	}

	/**
	 * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 */
	@Test
	public void zremrangeByScoreTest() {
		Long size = jedis.zremrangeByScore("xscj", "(60.55", "(70.55");// 判断条件：60.55 < score < 70.55
		logger.fatal(size);// 删除m，不删除l、n

		size = jedis.zremrangeByScore("xscj", 105.05d, 115.05d);// 判断条件：105.05d <= score <= 115.05d
		logger.fatal(size);// 删除u、v、w
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * 
	 * 返回值：
	 *     如果 member 是有序集 key 的成员，返回 member 的排名。
	 *     如果 member 不是有序集 key 的成员，返回 nil 。
	 */
	@Test
	public void zrankTest() {
		Long orderIndex = jedis.zrank("xscj", "p");
		logger.fatal(orderIndex); // 5
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 
	 * 返回值：
	 *     如果 member 是有序集 key 的成员，返回 member 的排名。
	 *     如果 member 不是有序集 key 的成员，返回 nil 。
	 */
	@Test
	public void zrevRankTest() {
		Long orderIndex = jedis.zrevrank("xscj", "k");
		logger.fatal(orderIndex); // 9
	}

}
