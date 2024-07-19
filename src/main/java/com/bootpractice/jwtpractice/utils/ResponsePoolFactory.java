package com.bootpractice.jwtpractice.utils;

import com.bootpractice.jwtpractice.dto.UserRes;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ResponsePoolFactory extends BasePooledObjectFactory<UserRes> {

	@Override
	public UserRes create() throws Exception {
		return new UserRes();
	}

	@Override
	public PooledObject<UserRes> wrap(UserRes userRes) {
		return new DefaultPooledObject<>(userRes);
	}
}
