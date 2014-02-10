package com.alibaba.maven.plugin.jcc.pojo;

import java.util.ArrayList;
import java.util.List;

public class ConflictResult {
	
	
	private String desc;
	
	private List<Jar> conflictJars; 
	
	private List<String> conflictClasses;
	
	
	public ConflictResult(){
		conflictJars = new ArrayList<Jar>();	
		conflictClasses = new ArrayList<String>();
	}
	
	public void addConflictJar(Jar jar){
		if(conflictJars.contains(jar)){
			return;
		}
		conflictJars.add(jar);
	}
	
	
	public void addConflictJars(List<Jar> jars){
		if(jars == null || jars.size() == 0){
			return;
		}
		
		for(Jar jar : jars){
			addConflictJar(jar);
		}
	}
	
	public void addConflictClass(String str){
		conflictClasses.add(str);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}	

	

	public List<Jar> getConflictJars() {
		return conflictJars;
	}

	public void setConflictJars(List<Jar> conflictJars) {
		this.conflictJars = conflictJars;
	}

	public List<String> getConflictClasses() {
		return conflictClasses;
	}

	public void setConflictClasses(List<String> conflictClasses) {
		this.conflictClasses = conflictClasses;
	}
	
}
