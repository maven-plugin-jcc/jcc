package com.alibaba.maven.plugin.jcc.pojo;

import java.io.File;
import java.util.List;

public class Jar {

	private File file;
	
	private String name;
	
	private String groupId;
	
	private String artifactId;
	
	private String version;	
	
	private List<String> classes;
	
	private int classSize;
	
	private String source; //来源  是工程WAR包的JAR还是参数输入的JAR 
	
	private String md5;
	
	private int depth;
	
	
	

	@Override
	public String toString() {
		//return "groupId ["+ groupId +"] artifactId ["+ artifactId +"] name ["+name +"] version ["+ version +"]" ;
		return name;
	}

	@Override
	public boolean equals(Object jar) {
		if(jar instanceof Jar && name.equals(((Jar) jar).getName())){
			return true;
		}		
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}	

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public int getClassSize() {
		return classSize;
	}

	public void setClassSize(int classSize) {
		this.classSize = classSize;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}			
}
