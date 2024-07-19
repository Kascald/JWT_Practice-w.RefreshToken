package com.bootpractice.jwtpractice.utils;

import com.bootpractice.jwtpractice.dto.UserRes;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ResponsePoolManager {
	private static GenericObjectPool<UserRes> pool;

	static {
		GenericObjectPoolConfig<UserRes> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(20);
		config.setMaxIdle(5);

		ResponsePoolFactory factory = new ResponsePoolFactory();

		pool = new GenericObjectPool<>(factory, config);
	}

	public static UserRes borrowObject() throws Exception {
		return pool.borrowObject();
	}

	public static void returnObject(UserRes userRes) {
		pool.returnObject(userRes);
	}
}
