package com.alibaba.maven.plugin.jcc.component;

public interface LifeCycle {

	void init()  throws Exception;

	void start()  throws Exception;

	void stop()  throws Exception;
}
