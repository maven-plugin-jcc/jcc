package com.alibaba.maven.plugin.jcc.pojo;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;

public class WarLibArtifact implements Artifact {
	
	private String artifactId;
	private File file;
	
	public WarLibArtifact(String artifactId){
		this.artifactId = artifactId;
	}

	@Override
	public int compareTo(Artifact o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getGroupId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArtifactId() {
		return artifactId;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVersion(String version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasClassifier() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public void setFile(File destination) {
		this.file = destination;
	}

	@Override
	public String getBaseVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBaseVersion(String baseVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDependencyConflictId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMetadata(ArtifactMetadata metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ArtifactMetadata> getMetadataList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRepository(ArtifactRepository remoteRepository) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArtifactRepository getRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVersion(String version, ArtifactRepository localRepository) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDownloadUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDownloadUrl(String downloadUrl) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArtifactFilter getDependencyFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDependencyFilter(ArtifactFilter artifactFilter) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArtifactHandler getArtifactHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDependencyTrail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDependencyTrail(List<String> dependencyTrail) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setScope(String scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public VersionRange getVersionRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVersionRange(VersionRange newRange) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectVersion(String version) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupId(String groupId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	@Override
	public boolean isSnapshot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setResolved(boolean resolved) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isResolved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setResolvedVersion(String version) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setArtifactHandler(ArtifactHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRelease() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRelease(boolean release) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ArtifactVersion> getAvailableVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAvailableVersions(List<ArtifactVersion> versions) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOptional() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOptional(boolean optional) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArtifactVersion getSelectedVersion()
			throws OverConstrainedVersionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelectedVersionKnown()
			throws OverConstrainedVersionException {
		// TODO Auto-generated method stub
		return false;
	}


}
