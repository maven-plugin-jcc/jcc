package com.alibaba.maven.plugin.jcc.pojo;

import java.util.ArrayList;
import java.util.List;

public class ConflictResult {
	
	
	private String desc;
	
	private List<Jar> conflictProjectJars;  //工程中的jar
	
	private Jar conflictParamJar; //需要被检测的jar 
	
	private List<String> conflictClasses;
	
	
	public ConflictResult(){
		conflictProjectJars = new ArrayList<Jar>();	
		conflictClasses = new ArrayList<String>();
	}
	
	public void addConflictProjectJar(Jar jar){
		if(conflictProjectJars.contains(jar)){
			return;
		}
		conflictProjectJars.add(jar);
	}
	
	
	public void addConflictProjectJars(List<Jar> jars){
		if(jars == null || jars.size() == 0){
			return;
		}
		
		for(Jar jar : jars){
			addConflictProjectJar(jar);
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
	

	public List<Jar> getConflictProjectJars() {
		return conflictProjectJars;
	}

	public void setConflictProjectJars(List<Jar> conflictProjectJars) {
		this.conflictProjectJars = conflictProjectJars;
	}

	public Jar getConflictParamJar() {
		return conflictParamJar;
	}

	public void setConflictParamJar(Jar conflictParamJar) {
		this.conflictParamJar = conflictParamJar;
	}

	public List<String> getConflictClasses() {
		return conflictClasses;
	}

	public void setConflictClasses(List<String> conflictClasses) {
		this.conflictClasses = conflictClasses;
	}
	
}
