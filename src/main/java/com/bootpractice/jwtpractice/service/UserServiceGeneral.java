package com.bootpractice.jwtpractice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UserServiceGeneral {

	default void loggingObject (Object object, Logger logger) throws NullPointerException {
		logger.info("Log : this Object {}  ->> {}" , object , object.toString());
	};
}
