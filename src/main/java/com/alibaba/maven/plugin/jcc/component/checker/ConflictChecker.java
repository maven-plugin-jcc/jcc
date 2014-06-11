package com.alibaba.maven.plugin.jcc.component.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.printer.ConflictPrinter;
import com.alibaba.maven.plugin.jcc.mojo.JccMojo;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.Jar;
import com.alibaba.maven.plugin.jcc.util.ArtifactUtil;

public class ConflictChecker extends AbstractLogEnabled implements LifeCycle {
	
	private JccMojo jccMojo;
	private List<ConflictResult> conflictResults;
	private LifeCycle conflictPrinter;
	
	public ConflictChecker(JccMojo jccMojo){
		this.jccMojo = jccMojo;
		conflictPrinter = new ConflictPrinter(this);
	}			
	
	@Override
	public void start() throws Exception {
		
		check(jccMojo.getCheckArtifacts(),jccMojo.getProjectArtifacts());	
		conflictPrinter.start();		
	}


	public void check(Set<Artifact> jarDependencyTree,Set<Artifact> projectDependencyTree) throws Exception {

		analysis(jarDependencyTree, projectDependencyTree);

		List<Jar> jarDependencyClass =  ArtifactUtil.getAllClassByArtifactsAsList(jarDependencyTree,"param");		
		Map<String,List<Jar>> projectDependencyClass = ArtifactUtil.getAllClassByArtifactsAsMap(projectDependencyTree);
		
		conflictResults =  transformConflictResult(jarDependencyClass,projectDependencyClass);			
	}

	public void analysis(Set<Artifact> jarDependencyTree,
			Set<Artifact> projectDependencyTree) {
		System.out.println("resolve begin..........");
		System.out.println("jar count of the -Djarpath method: " + jarDependencyTree.size());
		System.out.println("jar count of project : " + projectDependencyTree.size());
	}
	
	public  List<ConflictResult> transformConflictResult(List<Jar> jarDependencyClass,Map<String,List<Jar>> projectDependencyClass){
		if((jarDependencyClass == null || jarDependencyClass.size() == 0) || 
				(projectDependencyClass == null || projectDependencyClass.isEmpty())){
			System.out.println("no jar need to be check conflict");
			return null;
		}		
		
	    List<ConflictResult>  conflictResults = new ArrayList<ConflictResult>();
		
		for(Jar jar : jarDependencyClass){		
			List<String> classes = jar.getClasses();
			if(classes == null || classes.size() == 0){	
				System.out.println("jar ["+jar.getName()+"] has no class");
				continue;
			}
			ConflictResult conflictResult = new ConflictResult();
			conflictResult.setConflictParamJar(jar);
			for(String clazz : classes){
				if(!projectDependencyClass.keySet().contains(clazz)){
					continue;
				}		
				conflictResult.addConflictClass(clazz);
				List<Jar> jars = projectDependencyClass.get(clazz);				
				conflictResult.addConflictProjectJars(jars);			
			}
			if(conflictResult.getConflictProjectJars() != null && conflictResult.getConflictProjectJars().size() > 0){
				conflictResults.add(conflictResult);
			}			
		}
		
		return conflictResults;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub		
	}

	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public JccMojo getJccMojo() {
		return jccMojo;
	}


	public void setJccMojo(JccMojo jccMojo) {
		this.jccMojo = jccMojo;
	}


	public List<ConflictResult> getConflictResults() {
		return conflictResults;
	}


	public void setConflictResults(List<ConflictResult> conflictResults) {
		this.conflictResults = conflictResults;
	}	

}
