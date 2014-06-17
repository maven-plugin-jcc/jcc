package com.alibaba.maven.plugin.jcc.pojo;

import java.util.ArrayList;
import java.util.List;

public class ConflictResult {
	
	
	private String desc;
	
	private List<JccArtifact> conflictProjectJars;  //工程中的jar
	
	private JccArtifact conflictParamJar; //需要被检测的jar 
	
	private List<String> conflictClasses;
	
	
	/**
	 * 这里主要判断jar在maven里依赖的深度
	 * @return
	 */
	public boolean willbeHappen(JccArtifact projectJar){
		if(conflictParamJar == null || projectJar == null ){
			return false;
		}
		
		if(projectJar.getGroupId().equals(conflictParamJar.getGroupId()) &&
		   projectJar.getArtifactId().equals(conflictParamJar.getArtifactId())	&&
		   conflictParamJar.getDepth() > projectJar.getDepth()){
			return false;
		}
		return true;
	}
	
	
	public ConflictResult(){
		conflictProjectJars = new ArrayList<JccArtifact>();	
		conflictClasses = new ArrayList<String>();
	}
	
	public void addConflictProjectJar(JccArtifact jar){
		if(conflictProjectJars.contains(jar)){
			return;
		}
		conflictProjectJars.add(jar);
	}
	
	
	public void addConflictProjectJars(List<JccArtifact> jars){
		if(jars == null || jars.size() == 0){
			return;
		}
		
		for(JccArtifact jar : jars){
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
	

	public List<JccArtifact> getConflictProjectJars() {
		return conflictProjectJars;
	}

	public void setConflictProjectJars(List<JccArtifact> conflictProjectJars) {
		this.conflictProjectJars = conflictProjectJars;
	}

	public JccArtifact getConflictParamJar() {
		return conflictParamJar;
	}

	public void setConflictParamJar(JccArtifact conflictParamJar) {
		this.conflictParamJar = conflictParamJar;
	}

	public List<String> getConflictClasses() {
		return conflictClasses;
	}

	public void setConflictClasses(List<String> conflictClasses) {
		this.conflictClasses = conflictClasses;
	}
	
}
