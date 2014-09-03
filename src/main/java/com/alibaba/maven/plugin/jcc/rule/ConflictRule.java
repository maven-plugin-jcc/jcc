package com.alibaba.maven.plugin.jcc.rule;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.repository.RepositorySystem;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.resolution.ArtifactRequest;
import com.alibaba.maven.plugin.jcc.component.checker.ConflictChecker;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;
import com.alibaba.maven.plugin.jcc.util.MavenHelper;

public class ConflictRule extends Rule {

	private String g;
	private String a;
	private String v;

	private Map<String, JccArtifact> projectArtifactsMap;

	private Map<String, JccArtifact> checkArtifactsMap;

	private MavenProject checkProject;

	private DependencyResolutionResult paramResult;
	
	
	@Override
	public void invoke(EnforcerRuleHelper helper) throws Exception {		
		
		if (StringUtils.isBlank(g)) {
			g = (String) helper.evaluate("${g}");
			if (StringUtils.isBlank(g)) {
				log.info("Variable [g] is null");
				return; // �����κδ���
			}
		}

		if (StringUtils.isBlank(a)) {
			a = (String) helper.evaluate("${a}");
			if (StringUtils.isBlank(a)) {
				log.info("Variable [a] is null");
				return; // �����κδ���
			}
		}

		if (StringUtils.isBlank(v)) {
			v = (String) helper.evaluate("${v}");
			if (StringUtils.isBlank(v)) {
				log.info("Variable [v] is null");
				return; // �����κδ���
			}
		}

		// ��ȡ��Ҫ�õ���maven����������ı���
		RepositorySystem repositorySystem = (RepositorySystem) helper
				.getComponent(RepositorySystem.class);
		ProjectBuilder projectBuilder = (ProjectBuilder) helper
				.getComponent(ProjectBuilder.class);
		

		// ��ȡ�������JAR��������
		org.apache.maven.artifact.Artifact artifact = repositorySystem
				.createProjectArtifact(g, a, v);
		ProjectBuildingResult result = projectBuilder.build(artifact,
				project.getProjectBuildingRequest());
		checkProject = result.getProject();
		paramResult = MavenHelper.createResolutionResult(projectDependenciesResolver,
				checkProject, session);

		analysis();			
		
	}

	private void analysis() throws Exception {	
		projectArtifactsMap = filter(projectResult.getDependencyGraph(), null, 0, "");
		checkArtifactsMap = filter(paramResult.getDependencyGraph(), null, 1, "param");
		
		ConflictChecker conflictChecker = new ConflictChecker(this);
		conflictChecker.start();
	}

	

	/**
	 * ��������JAR�ڹ�������ڲ�����ȴ��ڴ����˵�JAR����ȣ���ô���Ժ������JAR�Լ�����������JAR�ĳ�ͻ 
	 * @param gid
	 * @param aid
	 * @param depth
	 * @return
	 */
	@Override
	public boolean ignoreConflict(String gid, String aid, int depth) {
		JccArtifact jccArtifact = projectArtifactsMap.get(gid + "." + aid);		
		if(jccArtifact != null &&  
			(depth > jccArtifact.getDepth() ||
				(jccArtifact.getManagedBits() != 0  && 
				     (jccArtifact.getManagedBits()& DependencyNode.MANAGED_VERSION) == DependencyNode.MANAGED_VERSION))){
			return true;
		}
		return false;		
	}
	
	@Override
	public ArtifactRequest createArtifactRequest(){
		ArtifactRequest request = new ArtifactRequest(paramResult.getDependencyGraph()
				.getArtifact(),
				checkProject.getRemoteProjectRepositories(), "project");
		return request;
	}
	


	public Map<String, JccArtifact> getProjectArtifactsMap() {
		return projectArtifactsMap;
	}

	public Map<String, JccArtifact> getCheckArtifactsMap() {
		return checkArtifactsMap;
	}

	public MavenProject getProject() {
		return project;
	}

	public MavenProject getCheckProject() {
		return checkProject;
	}

}
