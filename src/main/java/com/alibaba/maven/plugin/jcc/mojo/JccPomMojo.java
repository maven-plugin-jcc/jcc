/**
 * mvn jcc:check -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
 */
package com.alibaba.maven.plugin.jcc.mojo;

import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal check
 * @requiresProject true
 * @requiresDependencyResolution test
 * @requiresDirectInvocation true
 * @author owenludong.lud
 */
public class JccPomMojo  extends JccMojo {

	@Override
	public Set<Artifact> queryComparedArtifacts() throws MojoExecutionException {		
        Set<Artifact> artifacts = this.project.getArtifacts();          
        return artifacts;
	}

}
