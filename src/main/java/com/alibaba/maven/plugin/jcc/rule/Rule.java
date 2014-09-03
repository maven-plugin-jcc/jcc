package com.alibaba.maven.plugin.jcc.rule;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;
import com.alibaba.maven.plugin.jcc.util.MavenHelper;

public abstract class Rule implements EnforcerRule {
	
	
	protected MavenProject project;
	
	protected MavenSession session;
	
	protected DependencyResolutionResult projectResult;
	
	protected ArtifactResolver artifactResolver;
	
	protected RepositorySystemSession repositorySystemSession;
	
	protected ProjectDependenciesResolver projectDependenciesResolver;
	
	protected Log log;

	@Override
	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		
		long startTime = System.currentTimeMillis();		
		try
		{			
			getComponent(helper);
			
			invoke(helper);
		}
		catch(Exception e)
		{
			throw new EnforcerRuleException("Unable to lookup a component "
					+ e.getLocalizedMessage(), e);
		}finally
		{
			long endTime = System.currentTimeMillis();
			System.out.print("\t**  end check jar conflict,consumer time ["
					+ (endTime - startTime)
					+ "ms]                                                **"
					+ "\n");
			System.out
					.print("\t**************************************************************************************************"
							+ "\n");
			System.out.println();
			System.out.println();
		}
		
	}
	
	public void getComponent(EnforcerRuleHelper helper) throws Exception{
		log = helper.getLog();
		project = (MavenProject) helper.evaluate("${project}");
		session = (MavenSession) helper.evaluate("${session}");
		// 下载自身的JAR
		artifactResolver = (ArtifactResolver) helper.getComponent(ArtifactResolver.class);
		
		projectDependenciesResolver = (ProjectDependenciesResolver) helper
				.getComponent(ProjectDependenciesResolver.class);
		// 获取工程的依赖树
		projectResult = MavenHelper.createResolutionResult(projectDependenciesResolver,
							project, session);
		repositorySystemSession = session.getRepositorySession();
	}
	
	
	
	public abstract void invoke(EnforcerRuleHelper helper) throws Exception;
	
	
	protected Map<String, JccArtifact> filter(DependencyNode root,
			JccArtifact parentJccArtifact, int depth, String type)
			throws ComponentLookupException, ArtifactResolutionException {

		Map<String, JccArtifact> jccArtifacts = new HashMap<String, JccArtifact>();

		if (root == null) {
			return jccArtifacts;
		}

		Dependency dependency = root.getDependency();
		JccArtifact jccArtifact = new JccArtifact();
		jccArtifact.setParentJccArtifact(parentJccArtifact);
		jccArtifact.setManagedBits(root.getManagedBits());

		String gid, aid, version;

		if (dependency == null) {

			ArtifactRequest request = null;
			if ("param".equals(type)) {
				request = createArtifactRequest();
			} else {
				request = new ArtifactRequest(projectResult
						.getDependencyGraph().getArtifact(),
						project.getRemoteProjectRepositories(), "project");
			}
			ArtifactResult artifactResult = artifactResolver.resolveArtifact(repositorySystemSession, request);

			gid = artifactResult.getArtifact().getGroupId();
			aid = artifactResult.getArtifact().getArtifactId();
			version = artifactResult.getArtifact().getVersion();

			jccArtifact.setFile(artifactResult.getArtifact().getFile());
			jccArtifact.setDepth(depth);
			jccArtifact.setGroupId(gid);
			jccArtifact.setArtifactId(aid);
			jccArtifact.setVersion(version);
			jccArtifact.setScope("");

		} else {
			String scope = dependency.getScope();
			if (isExcludeArtifact(scope)) {
				return null;
			}

			gid = dependency.getArtifact().getGroupId();
			aid = dependency.getArtifact().getArtifactId();
			version = dependency.getArtifact().getVersion();

			jccArtifact.setFile(dependency.getArtifact().getFile());
			jccArtifact.setDepth(depth);
			jccArtifact.setGroupId(gid);
			jccArtifact.setArtifactId(aid);
			jccArtifact.setVersion(version);
			jccArtifact.setScope(dependency.getScope());
		}

		if ("param".equals(type) && ignoreConflict(gid, aid, depth)) {
			return jccArtifacts;
		} 
		
		jccArtifacts.put(gid + "." + aid, jccArtifact);

		List<DependencyNode> childs = root.getChildren();
		if (childs == null || childs.size() == 0) {
			return jccArtifacts;
		}

		depth++;

		for (DependencyNode node : childs) {
			Map<String, JccArtifact> sub = filter(node, jccArtifact, depth,
					type);
			if (sub != null && sub.size() > 0) {
				jccArtifacts.putAll(sub);
			}
		}
		return jccArtifacts;

	}
	
	/**
	 * 如果引入的JAR在工程里存在并且深度大于存在了的JAR的深度，那么可以忽略这个JAR以及所有他依赖JAR的冲突 
	 * @param gid
	 * @param aid
	 * @param depth
	 * @return
	 */
	public abstract boolean ignoreConflict(String gid, String aid, int depth) ;
	
	public abstract  ArtifactRequest createArtifactRequest();
			
	

	/**
	 * 如果引入的JAR在工程里存在并且深度小于等于存在了的JAR的深度，那么不能忽略这个JAR的冲突，并且只需要检测这个JAR的冲突 不需要检测其他JAR 
	 * 因为考虑到如果排除的是工程里面相同的JAR，引入了一个新版本的JAR，新版本依赖的可能又会有新的JAR包引入，所以这里不对依赖包做排除
	 * @param gid
	 * @param aid
	 * @param depth
	 * @return
	 
	private boolean ignoreChild(String gid, String aid, int depth) {
		JccArtifact jccArtifact = projectArtifactsMap.get(gid + "." + aid);
		
	}*/

	private boolean isExcludeArtifact(String scope) {

		if (StringUtils.isBlank(scope)) {
			return false;
		}

		Collection<String> exludes = new HashSet<String>();
		Collections.addAll(exludes, "system", "provided", "test");
		if (exludes.contains(scope)) {
			return true;
		}

		return false;

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

	
}
