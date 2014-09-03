package com.alibaba.maven.plugin.jcc.component.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.alibaba.maven.plugin.jcc.component.printer.CompareIsIncludePrinter;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.Header;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;
import com.alibaba.maven.plugin.jcc.rule.CompareIsIncludeRule;
import com.alibaba.maven.plugin.jcc.util.ArtifactUtil;

public class CompareIsIncludeChecker extends AbstractChecker<CompareIsIncludeRule> {
	
	
	public CompareIsIncludeChecker(CompareIsIncludeRule rule){		
		super(rule);			
		this.headers = construnctHeader();
		this.printer = new CompareIsIncludePrinter(this);
	}
	
	@Override
	public  List<Header> construnctHeader(){
		List<Header> headers = new ArrayList<Header>();
		
		Header header1 = new Header();
		header1.setName(ArtifactUtil.mavenProject2Str(rule.getProject()));		
		header1.setJarSize(rule.getProjectJccArtifacts().size());
		headers.add(header1);
		
		Header header2 = new Header();
		header2.setName("path ");
		header2.setDesc(rule.getPath());
		header2.setJarSize(rule.getTargetJccArtifacts().size());
		headers.add(header2);
		return headers;
	}

	@Override
	public void transformConflictResult() throws Exception {
		List<JccArtifact> jarDependencyClass =  ArtifactUtil.getAllClassByArtifactsAsList(rule.getProjectJccArtifacts(),"compare");		
		Map<String,List<JccArtifact>> projectDependencyClass = ArtifactUtil.getAllClassByArtifactsAsMap(rule.getTargetJccArtifacts());
		
		conflictResults =  transformConflictResult(jarDependencyClass,projectDependencyClass);
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
			
			//过滤掉类名字一样 个数一样 MD5一样的冲突
			if(!filterSameJar(conflictResult)){
				conflictResults.add(conflictResult);
			}
					
		}
		
		return conflictResults;
	}
	
	private boolean filterSameJar(ConflictResult conflictResult){
		JccArtifact at = conflictResult.getConflictParamJar();
		if(at.getClassSize() == conflictResult.getConflictClasses().size()){
			
			if(conflictResult.getConflictProjectJars() != null && conflictResult.getConflictProjectJars().size() > 0)
			{
				for(JccArtifact jccArtifact : conflictResult.getConflictProjectJars())
				{
					if(jccArtifact.getMd5().equals(at.getMd5()))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
