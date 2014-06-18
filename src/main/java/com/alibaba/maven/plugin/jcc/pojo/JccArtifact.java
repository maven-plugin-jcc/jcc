package com.alibaba.maven.plugin.jcc.pojo;

import java.io.File;
import java.util.List;

public class JccArtifact {

	private int depth;
	private String groupId;
	private String artifactId;
	private File file;
	private String version;
	private String scope;
	private JccArtifact parentJccArtifact;
	private String name;
	private List<String> classes;
	private String source; // 来源 是工程WAR包的JAR还是参数输入的JAR
	private int classSize;
	private String md5;
	private int managedBits; //参考DependencyNode


	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public JccArtifact getParentJccArtifact() {
		return parentJccArtifact;
	}

	public void setParentJccArtifact(JccArtifact parentJccArtifact) {
		this.parentJccArtifact = parentJccArtifact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getClassSize() {
		return classSize;
	}

	public void setClassSize(int classSize) {
		this.classSize = classSize;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getManagedBits() {
		return managedBits;
	}

	public void setManagedBits(int managedBits) {
		this.managedBits = managedBits;
	}

	@Override
	public String toString() {
		return groupId + "|" + artifactId + "|" + version + "|" + depth;
	}

}
