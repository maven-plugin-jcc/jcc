package com.alibaba.maven.plugin.jcc.component.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.printer.ConflictPrinter;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;
import com.alibaba.maven.plugin.jcc.rule.ConflictRule;
import com.alibaba.maven.plugin.jcc.util.ArtifactUtil;

public class ConflictChecker extends AbstractLogEnabled implements LifeCycle {
	
	private ConflictRule conflictRule;
	private List<ConflictResult> conflictResults;
	private LifeCycle conflictPrinter;
	
	public ConflictChecker(ConflictRule conflictRule){
		this.conflictRule = conflictRule;
		conflictPrinter = new ConflictPrinter(this);
	}			
	
	@Override
	public void start() throws Exception {
		
		check(conflictRule.getCheckArtifacts(),conflictRule.getProjectArtifacts());	
		conflictPrinter.start();		
	}


	public void check(Set<JccArtifact> jarDependencyTree,Set<JccArtifact> projectDependencyTree) throws Exception {

		analysis(jarDependencyTree, projectDependencyTree);

		List<JccArtifact> jarDependencyClass =  ArtifactUtil.getAllClassByArtifactsAsList(jarDependencyTree,"param");		
		Map<String,List<JccArtifact>> projectDependencyClass = ArtifactUtil.getAllClassByArtifactsAsMap(projectDependencyTree);
		
		conflictResults =  transformConflictResult(jarDependencyClass,projectDependencyClass);			
	}

	public void analysis(Set<JccArtifact> jarDependencyTree, Set<JccArtifact> projectDependencyTree) {
		System.out.println();
		System.out.println();
		System.out.print("\t**************************************************************************************************" + "\n" );
		System.out.print(( "\t**                                                                                              **" + "\n" ));
		System.out.print("\t**\t                           JCC MAVEN PLUGIN                                                 **" + "\n" );
		System.out.print("\t**\t                                                                                            **" + "\n" );
		System.out.print("\t\t"+ mavenProject2Str(conflictRule.getCheckProject()) +"¡¾P¡¿ has ["+jarDependencyTree.size()+"]  dependency jar                  "  + "\n" );
		System.out.print("\t\t"+ mavenProject2Str(conflictRule.getProject())      +" has ["+projectDependencyTree.size()+"]  dependency jar            "  + "\n" );		
	}
	
	private String mavenProject2Str(MavenProject mavenProject){
		if(mavenProject == null){
			return null;
		}
		
		StringBuilder sb = new StringBuilder( 128 );
        sb.append( "MavenProject: " );
        sb.append( mavenProject.getGroupId() );
        sb.append( ":" );
        sb.append( mavenProject.getArtifactId() );
        sb.append( ":" );
        sb.append( mavenProject.getVersion());
        return sb.toString();
	}
	
	public  List<ConflictResult> transformConflictResult(List<JccArtifact> jarDependencyClass,Map<String,List<JccArtifact>> projectDependencyClass){
		if((jarDependencyClass == null || jarDependencyClass.size() == 0) || 
				(projectDependencyClass == null || projectDependencyClass.isEmpty())){
			System.out.println("no jar need to be check conflict");
			return null;
		}		
		
	    List<ConflictResult>  conflictResults = new ArrayList<ConflictResult>();
		
		for(JccArtifact jar : jarDependencyClass){		
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
				List<JccArtifact> jars = projectDependencyClass.get(clazz);				
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

	


	public List<ConflictResult> getConflictResults() {
		return conflictResults;
	}


	public void setConflictResults(List<ConflictResult> conflictResults) {
		this.conflictResults = conflictResults;
	}	

}
