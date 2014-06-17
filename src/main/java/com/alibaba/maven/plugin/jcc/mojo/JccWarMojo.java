/**
 *  for example: mvn jcc:check-war -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
 *//*
package com.alibaba.maven.plugin.jcc.mojo;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import com.alibaba.maven.plugin.jcc.pojo.WarLibArtifact;

*//**
 * @goal check-war
 * @requiresDirectInvocation true
 * @author owenludong.lud
 *//*
public class JccWarMojo extends JccMojo {

	@Override
	public Set<Artifact> queryComparedArtifacts() throws MojoExecutionException {		
        if (StringUtils.isBlank(warPath)) {
        	warPath = project.getBasedir().getPath() + "/target/"  + project.getBuild().getFinalName();
        }
        
        getLog().info("get warPath : " + warPath);        
        
        if (!project.getPackaging().equals("war")) {
			throw new MojoExecutionException("project packaging is not a war!");
		}      
      
        File warDir = new File(warPath);
        if (!warDir.exists()) {
            throw new MojoExecutionException(warPath + " not found");
        }
        if (!warDir.isDirectory()) {
            throw new MojoExecutionException(warPath + " is not a directory");
        }

        String jarDirPath = warPath + "/WEB-INF/lib";
        warDir = new File(jarDirPath);
        if (!warDir.exists()) {
            throw new MojoExecutionException(jarDirPath + " not found");
        }

        getLog().info("Search jarDirPath : " + jarDirPath);

        File jarDir = new File(jarDirPath);
        File[] listFiles = jarDir.listFiles(new FileFilter() {
       
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")) {
                    return true;
                }
                return false;
            }
        });
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        for (File file : listFiles) {
        	Artifact artifact = new WarLibArtifact(file.getName());
        	artifact.setFile(file);
        	artifacts.add(artifact);
        }       
        return artifacts;
	}

}
*/