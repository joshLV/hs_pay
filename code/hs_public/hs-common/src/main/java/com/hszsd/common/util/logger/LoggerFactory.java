package com.hszsd.common.util.logger;

/**
 * 日志输出器工厂
 * 
 * @author 艾伍
 */
public class LoggerFactory implements Logger {

	private Class<?> cla;
	private static org.slf4j.Logger logger;

	public LoggerFactory(Class<?> cla) {
		this.cla = cla;
		logger = org.slf4j.LoggerFactory.getLogger(cla);
		
	}

	public static Logger getLogger(Class<?> cla) {
		return new LoggerFactory(cla);
	}

	@Override
	public void trace(String msg) {
		logger.trace(msg);

	}

	@Override
	public void trace(Throwable e) {
		logger.trace("", e);
	}

	@Override
	public void trace(String msg, Throwable e) {
		logger.trace(msg, e);
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(Throwable e) {
		logger.debug("", e);

	}

	@Override
	public void debug(String msg, Throwable e) {
		logger.debug(msg, e);

	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(Throwable e) {
		logger.info("", e);
	}

	@Override
	public void info(String msg, Throwable e) {
		logger.info(msg, e);

	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void warn(Throwable e) {
		logger.warn("", e);

	}

	@Override
	public void warn(String msg, Throwable e) {
		logger.warn(msg, e);

	}

	@Override
	public void error(String msg) {
		logger.error(msg);

	}

	@Override
	public void error(Throwable e) {
		logger.error("", e);

	}

	@Override
	public void error(String msg, Throwable e) {
		logger.error(msg, e);

	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

}