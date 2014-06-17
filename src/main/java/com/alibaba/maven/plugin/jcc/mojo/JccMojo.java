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
import org.apache.maven.project.DefaultDependencyResolutionRequest;
import org.apache.maven.project.DependencyResolutionException;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.eclipse.aether.graph.DependencyNode;

import com.alibaba.maven.plugin.jcc.component.checker.ConflictChecker;


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
	 * @component role="org.apache.maven.project.ProjectDependenciesResolver"
	 */
	private ProjectDependenciesResolver projectDependenciesResolver ;
	
	
	/**
	 * @component role="org.apache.maven.project.ProjectBuilder" roleHint="default"
	 */
	private ProjectBuilder projectBuilder;
	
	private Set<Artifact> projectArtifacts;
	
	private Set<Artifact> checkArtifacts ;
	


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("please see the class of ConflictRule");
		
		/*getLog().info("jarpath : " + jarPath);				
		
		if(!jarPath.endsWith(".jar")){
			throw new MojoExecutionException("the file is not a jar");
		}
		
		String pomPath = jarPath.replace(".jar", ".pom");		
		File pomFile = new File(pomPath);
		if(!pomFile.exists()){
			throw new MojoExecutionException("jarPath ["+ jarPath +"] not exists the pom file");
		}	
			
		ProjectBuildingRequest configuration =  project.getProjectBuildingRequest();
		ProjectBuildingResult result = null;
		try {
			result = projectBuilder.build(pomFile, configuration);
		} catch (ProjectBuildingException projectBuildingException) {
			throw new MojoExecutionException(projectBuildingException.getMessage());
		}
	*/
		
		/*DefaultDependencyResolutionRequest request =
	            new DefaultDependencyResolutionRequest( result.getProject(), session.getRepositorySession() );
		DependencyResolutionResult  dependencyResolutionResult  = null;	       
		try {
			dependencyResolutionResult  = projectDependenciesResolver.resolve(request);
		} catch (DependencyResolutionException e) {
			throw new MojoExecutionException(e.getMessage());	
		}
		
	    if(dependencyResolutionResult == null){
	    	return;
	    }
	    
	    DependencyNode dependencyNode = dependencyResolutionResult.getDependencyGraph();
	    getLog().info("finish jcc conflict check");*/
		
		/*MavenProject mavenProject = result.getProject();	
	
		try {
			checkArtifacts = getDependencyJarByJarPath(mavenProject);
		} catch (LifecycleExecutionException e) {
			throw new MojoExecutionException(e.getMessage());
		} catch(Exception e){
			throw new MojoExecutionException(e.getMessage());	
		}
		
		if(checkArtifacts == null){
			getLog().info("can not find artifact jar");
			return;
		}		
		
		//添加自己
		Artifact artifact = mavenProject.getArtifact();
		artifact.setFile(new File(jarPath));
		checkArtifacts.add(artifact);		
		
		projectArtifacts = queryComparedArtifacts();
		
		ConflictChecker conflictChecker  = new ConflictChecker(this);
		try {
			conflictChecker.start();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage());
		}
		
		getLog().info("finish jcc conflict check");*/
	}
	
	
	public abstract Set<Artifact>  queryComparedArtifacts() throws MojoExecutionException;
	
	
	/*private Set<Artifact>  getDependencyJarByJarPath(MavenProject mavenProject) throws LifecycleExecutionException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DependencyResolutionException{
		Collection<String> scopesToResolve = new TreeSet<String>();
		Collections.addAll( scopesToResolve, "system", "compile", "runtime");	 //"这里去掉test、	provided	
		
		
		
		lifeCycleDependencyResolver.resolveProjectDependencies( mavenProject, Collections.<String> emptySet() , scopesToResolve, session,
		         false, Collections.<Artifact> emptySet() );		
		
		ScopeArtifactFilter filter = new ScopeArtifactFilter(Artifact.SCOPE_TEST);
		mavenProject.setArtifactFilter(filter);
		Set<Artifact> artifacts =  mavenProject.getArtifacts();	
		return artifacts;
		
	}


	public Set<Artifact> getProjectArtifacts() {
		return projectArtifacts;
	}


	public void setProjectArtifacts(Set<Artifact> projectArtifacts) {
		this.projectArtifacts = projectArtifacts;
	}


	public Set<Artifact> getCheckArtifacts() {
		return checkArtifacts;
	}


	public void setCheckArtifacts(Set<Artifact> checkArtifacts) {
		this.checkArtifacts = checkArtifacts;
	}
		*/
}
