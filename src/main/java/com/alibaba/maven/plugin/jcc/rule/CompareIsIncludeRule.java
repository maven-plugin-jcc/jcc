package com.alibaba.maven.plugin.jcc.rule;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.resolution.ArtifactRequest;
import com.alibaba.maven.plugin.jcc.component.checker.CompareIsIncludeChecker;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;



/**
 * https://github.com/maven-plugin-jcc/jcc/issues/1
 * @author owenludong.lud
 *
 */

public class CompareIsIncludeRule  extends Rule {
	
	
	private String path;
	private Collection<JccArtifact> projectJccArtifacts;
	private Collection<JccArtifact> targetJccArtifacts;
	

	@Override
	public void invoke(EnforcerRuleHelper helper) throws Exception 
	{	
		
		if (StringUtils.isBlank(path)) 
		{
			path = (String) helper.evaluate("${path}");
			if (StringUtils.isBlank(path))
			{
				log.info("Variable [path] is null");
				return; // 不做任何处理
			}
		}			
		
		
		//projectJccArtifacts =  ArtifactUtil.toJccArtifacts(project.getArtifacts());
		Map<String,JccArtifact> projectArtifactsMap = filter(projectResult.getDependencyGraph(), null, 0, "");
		projectJccArtifacts = projectArtifactsMap.values();
		targetJccArtifacts = queryArtifacts(path);
		
		
		if(projectJccArtifacts == null || projectJccArtifacts.size() == 0 ||
				targetJccArtifacts == null || targetJccArtifacts.size() == 0)
		{
			log.warn("no jar");
			return ;
		}	
		
		CompareIsIncludeChecker checker = new CompareIsIncludeChecker(this);
		checker.start();		
		
	}	
	
	private List<JccArtifact> queryArtifacts(String path) throws Exception{
		
		File jarDir = new File(path);
        if (!jarDir.exists()) {
            throw new EnforcerRuleException(path + " not found");
        }
        if (!jarDir.isDirectory()) {
            throw new EnforcerRuleException(path + " is not a directory");
        }         	
		
		File[] listFiles = jarDir.listFiles(new FileFilter() {
		       
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")) {
                    return true;
                }
                return false;
            }
        });
		
		List<JccArtifact> artifacts = new ArrayList<JccArtifact>();	
		
		if(listFiles == null || listFiles.length == 0){
			log.warn("folder path ["+ path +"]  has no jar ");
			return null; 
		}
		
	
		for(File file : listFiles)
		{
			JccArtifact artifact = new JccArtifact();
			artifact.setFile(file);
			artifact.setName(file.getName());
			artifacts.add(artifact);
		}	
			
		return artifacts;
		
	} 

	@Override
	public boolean isCacheable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isResultValid(EnforcerRule cachedRule) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCacheId() {
		// TODO Auto-generated method stub
		return null;
	}


	public Collection<JccArtifact> getProjectJccArtifacts() {
		return projectJccArtifacts;
	}

	public void setProjectJccArtifacts(Collection<JccArtifact> projectJccArtifacts) {
		this.projectJccArtifacts = projectJccArtifacts;
	}

	public Collection<JccArtifact> getTargetJccArtifacts() {
		return targetJccArtifacts;
	}

	public void setTargetJccArtifacts(Collection<JccArtifact> targetJccArtifacts) {
		this.targetJccArtifacts = targetJccArtifacts;
	}

	public MavenProject getProject() {
		return project;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public boolean ignoreConflict(String gid, String aid, int depth) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArtifactRequest createArtifactRequest() {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
