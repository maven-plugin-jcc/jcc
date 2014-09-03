package com.alibaba.maven.plugin.jcc.component.printer;

import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.checker.AbstractChecker;

public abstract class AbstractPrinter implements LifeCycle {
	
	protected AbstractChecker checker;
	
	protected AbstractPrinter(AbstractChecker checker){
		this.checker = checker;
	}
	
	
	@Override
	public void init() throws Exception {
		printer();		
	}

	@Override
	public void start() throws Exception {
		printer();
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}	
	
	public abstract void printer();

	public void setChecker(AbstractChecker checker) {
		this.checker = checker;
	}

	public AbstractChecker getChecker() {
		return checker;
	}		
	
}
