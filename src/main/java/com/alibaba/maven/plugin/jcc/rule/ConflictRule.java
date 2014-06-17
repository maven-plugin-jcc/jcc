package com.alibaba.maven.plugin.jcc.rule;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultDependencyResolutionRequest;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import com.alibaba.maven.plugin.jcc.component.checker.ConflictChecker;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;


public class ConflictRule implements EnforcerRule {		
	
	private String g;
	private String a;
	private String v;
	
    private Set<JccArtifact> projectArtifacts;
	
	private Set<JccArtifact> checkArtifacts ;	
		
	private MavenProject project;
	
	private MavenProject checkProject;
	
	private MavenSession session;
	
	private DependencyResolutionResult projectResult;
	
	private DependencyResolutionResult paramResult;
	
	private ArtifactResolver artifactResolver;
	
		
	@Override
	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {	       
	
		Log log = helper.getLog();		
		long startTime = System.currentTimeMillis();
        try
        {
        	if(StringUtils.isBlank(g)){
        		g =  (String)helper.evaluate("${g}");
        		if(StringUtils.isBlank(g)){
        			log.info("Variable [g] is null");
        			return; //不做任何处理
        		}
        	}
        	
        	if(StringUtils.isBlank(a)){
        		a =  (String)helper.evaluate("${a}");
        		if(StringUtils.isBlank(a)){
        			log.info("Variable [a] is null");
        			return; //不做任何处理
        		}
        	}
        	
        	if(StringUtils.isBlank(v)){
        		v =  (String)helper.evaluate("${v}");
        		if(StringUtils.isBlank(v)){
        			log.info("Variable [v] is null");
        			return; //不做任何处理
        		}
        	}
        	
        	//获取需要用到的maven组件和上下文变量
        	RepositorySystem repositorySystem = (RepositorySystem)helper.getComponent(RepositorySystem.class);
        	ProjectBuilder projectBuilder = (ProjectBuilder)helper.getComponent(ProjectBuilder.class);
        	project = (MavenProject) helper.evaluate( "${project}" );
        	session = (MavenSession) helper.evaluate( "${session}" );
        	//下载自身的JAR        		
        	artifactResolver = (ArtifactResolver)helper.getComponent(ArtifactResolver.class);
        	ProjectDependenciesResolver projectDependenciesResolver = (ProjectDependenciesResolver)helper.getComponent(ProjectDependenciesResolver.class);
        	
        	
        	//获取工程的依赖树
    		projectResult = createResolutionResult(projectDependenciesResolver, project, session);
        	
    		//获取检验参数JAR的依赖树
        	org.apache.maven.artifact.Artifact artifact = repositorySystem.createProjectArtifact(g, a, v);          	
        	ProjectBuildingResult result = projectBuilder.build(artifact, project.getProjectBuildingRequest());
        	checkProject = result.getProject();
        	paramResult = createResolutionResult(projectDependenciesResolver, checkProject, session);        	
            
        	
        	analysis();        	
        	
        	long endTime = System.currentTimeMillis();
    		System.out.print("\t**  end check jar conflict,consumer time ["+ (endTime - startTime)  +"ms]                                                **"+"\n");
    		System.out.print("\t**************************************************************************************************" + "\n");
            System.out.println();
            System.out.println();       	
        } 
        catch ( ComponentLookupException e )
        {
        	throw new EnforcerRuleException( "Unable to lookup a component " + e.getLocalizedMessage(), e );
        } 
        catch ( Exception e )
        {
        	throw new EnforcerRuleException( "Unable to lookup a component " + e.getLocalizedMessage(), e );
        }       
	}
	
	
	private void analysis() throws Exception{
		projectArtifacts = filter(projectResult.getDependencyGraph(),null,0,"");
		checkArtifacts  = filter(paramResult.getDependencyGraph(),null,0,"param");		
		
		ConflictChecker conflictChecker  = new ConflictChecker(this);
		conflictChecker.start(); 
	}
	
	private Set<JccArtifact> filter(DependencyNode root,JccArtifact parentJccArtifact,int depth,String type) throws ComponentLookupException, ArtifactResolutionException{		
		if(root == null){
			return null;
		}
		
		Set<JccArtifact>  jccArtifacts = new HashSet<JccArtifact>();
		
		Dependency dependency = root.getDependency();
		JccArtifact jccArtifact = new JccArtifact();
		jccArtifact.setParentJccArtifact(parentJccArtifact);
		if(dependency == null){		
			
			
			ArtifactRequest request  =  null;
			if("param".equals(type)){
				request = new ArtifactRequest(paramResult.getDependencyGraph().getArtifact(),checkProject.getRemoteProjectRepositories(),"project");
			}else{
				request = new ArtifactRequest(projectResult.getDependencyGraph().getArtifact(),project.getRemoteProjectRepositories(),"project");
			}
			
			ArtifactResult artifactResult = artifactResolver.resolveArtifact(session.getRepositorySession(), request);
			
			jccArtifact.setFile(artifactResult.getArtifact().getFile());
			jccArtifact.setDepth(depth);			
			jccArtifact.setGroupId(artifactResult.getArtifact().getGroupId());
			jccArtifact.setArtifactId(artifactResult.getArtifact().getArtifactId());
			jccArtifact.setVersion(artifactResult.getArtifact().getVersion());
			jccArtifact.setScope("");
			jccArtifacts.add(jccArtifact);
			
		}else{
		
			String scope = dependency.getScope();
			if(isExcludeArtifact(scope)){
				return null;
			}	
			jccArtifact.setFile(dependency.getArtifact().getFile());
			jccArtifact.setDepth(depth);			
			jccArtifact.setGroupId(dependency.getArtifact().getGroupId());
			jccArtifact.setArtifactId(dependency.getArtifact().getArtifactId());
			jccArtifact.setVersion(dependency.getArtifact().getVersion());
			jccArtifact.setScope(dependency.getScope());
			jccArtifacts.add(jccArtifact);
		}			
		
		List<DependencyNode> childs = root.getChildren();
		if(childs == null || childs.size() == 0){
			return jccArtifacts;
		}
		
		depth++;
		
		for(DependencyNode node : childs){
			Set<JccArtifact>  sub = filter(node,jccArtifact,depth,type);
			if(sub != null && sub.size() > 0){
				jccArtifacts.addAll(sub);
			}
		}
		return jccArtifacts;
		
	}	
	
	private boolean isExcludeArtifact(String scope){
		
		if(StringUtils.isBlank(scope)){
			return false;
		}
		
		Collection<String> exludes = new HashSet<String>();
        Collections.addAll( exludes, "system", "provided", "test" );
        if(exludes.contains(scope)) {
        	return true;
        }
        
        return false;

	}
	
	
	@SuppressWarnings("unchecked")
	private DependencyResolutionResult  createResolutionResult(ProjectDependenciesResolver resolver,MavenProject project,MavenSession session) throws Exception{
		if(resolver == null || project == null || session == null){
			return null;
		}
		DefaultDependencyResolutionRequest request =
	            new DefaultDependencyResolutionRequest( project, session.getRepositorySession() );
		 //过滤掉test和provided的类型              
		Collection<String> excludeColloection = new HashSet<String>();
		Collections.addAll( excludeColloection, "test", "provided");
        
        Class<DependencyFilter> filterClass = (Class<DependencyFilter>) project.getClass().getClassLoader().loadClass("org.eclipse.aether.util.filter.ScopeDependencyFilter");
        Constructor<DependencyFilter>  filterConstructor = filterClass.getDeclaredConstructor(new Class[]{Collection.class,Collection.class});
        DependencyFilter collectionFilter = (DependencyFilter)filterConstructor.newInstance(null,excludeColloection);
      
        request.setResolutionFilter(collectionFilter);
		return resolver.resolve(request);
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


	public Set<JccArtifact> getProjectArtifacts() {
		return projectArtifacts;
	}


	public Set<JccArtifact> getCheckArtifacts() {
		return checkArtifacts;
	}


	public MavenProject getProject() {
		return project;
	}


	public MavenProject getCheckProject() {
		return checkProject;
	}
	
}
