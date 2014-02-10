package com.alibaba.maven.plugin.jcc.mojo;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.internal.LifecycleDependencyResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;

import com.alibaba.maven.plugin.jcc.component.ConflictChecker;

/**
 * @author owenludong.lud
 */
public abstract class JccMojo extends AbstractMojo {

	/**
	 * @parameter expression="${jarPath}"
	 * @required
	 */
	protected String jarPath;
  

	/**
	 * @parameter expression="${warPath}"
	 */
	protected String warPath;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;	
	
	/**
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	private MavenSession session;
	
	/**
	 * @component role="org.apache.maven.lifecycle.internal.LifecycleDependencyResolver" roleHint="default"
	 */
	private LifecycleDependencyResolver lifeCycleDependencyResolver;
	
	/**
	 * @component role="org.apache.maven.project.ProjectBuilder" roleHint="default"
	 */
	private ProjectBuilder projectBuilder;
	


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("jarpath : " + jarPath);				
		
		if(!jarPath.endsWith(".jar")){
			throw new MojoExecutionException("the file is not a jar");
		}
		
		String pomPath = jarPath.replace(".jar", ".pom");		
		File pomFile = new File(pomPath);
		if(!pomFile.exists()){
			throw new MojoExecutionException("jarPath ["+ jarPath +"] not exists the pom file");
		}	
			
		ProjectBuildingRequest configuration =  project.getProjectBuildingRequest();
		ProjectBuildingResult result;
		try {
			result = projectBuilder.build(pomFile, configuration);
		} catch (ProjectBuildingException projectBuildingException) {
			throw new MojoExecutionException(projectBuildingException.getMessage());
		}
		MavenProject mavenProject = result.getProject();
		
		
		Set<Artifact> artifacts = null;
		try {
			artifacts = getDependencyJarByJarPath(mavenProject);
		} catch (LifecycleExecutionException e) {
			throw new MojoExecutionException(e.getMessage());
		}			
		
		if(artifacts == null){
			getLog().info("can not find artifact jar");
			return;
		}		
		
		//Ìí¼Ó×Ô¼º
		Artifact artifact = mavenProject.getArtifact();
		artifact.setFile(new File(jarPath));
		artifacts.add(artifact);		
		
		Set<Artifact> projectArtifacts = queryComparedArtifacts();
		
		ConflictChecker conflictChecker  = new ConflictChecker();
		try {
			conflictChecker.compare(artifacts, projectArtifacts);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage());
		}
		
		getLog().info("finish jcc conflict check");

	}
	
	
	public abstract Set<Artifact>  queryComparedArtifacts() throws MojoExecutionException;
	
	
	private Set<Artifact>  getDependencyJarByJarPath(MavenProject mavenProject) throws LifecycleExecutionException{
		Collection<String> scopesToResolve = new TreeSet<String>();
		Collections.addAll( scopesToResolve, "system", "compile", "provided", "runtime", "test" );			
		
		lifeCycleDependencyResolver.resolveProjectDependencies( mavenProject, Collections.<String> emptySet() , scopesToResolve, session,
		         false, Collections.<Artifact> emptySet() );		
		
		ScopeArtifactFilter filter = new ScopeArtifactFilter(Artifact.SCOPE_RUNTIME);
		mavenProject.setArtifactFilter(filter);
		Set<Artifact> artifacts =  mavenProject.getArtifacts();	
		return artifacts;
	}
}
