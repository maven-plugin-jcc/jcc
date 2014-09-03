package com.alibaba.maven.plugin.jcc.util;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.DefaultDependencyResolutionRequest;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.eclipse.aether.graph.DependencyFilter;

public class MavenHelper {

	@SuppressWarnings("unchecked")
	public static  DependencyResolutionResult createResolutionResult(
			ProjectDependenciesResolver resolver, MavenProject project,
			MavenSession session) throws Exception {
		if (resolver == null || project == null || session == null) {
			return null;
		}
		DefaultDependencyResolutionRequest request = new DefaultDependencyResolutionRequest(
				project, session.getRepositorySession());
		// 过滤掉test和provided的类型
		Collection<String> excludeColloection = new HashSet<String>();
		Collections.addAll(excludeColloection, "test", "provided");

		Class<DependencyFilter> filterClass = (Class<DependencyFilter>) project
				.getClass()
				.getClassLoader()
				.loadClass(
						"org.eclipse.aether.util.filter.ScopeDependencyFilter");
		Constructor<DependencyFilter> filterConstructor = filterClass
				.getDeclaredConstructor(new Class[] { Collection.class,
						Collection.class });
		DependencyFilter collectionFilter = (DependencyFilter) filterConstructor
				.newInstance(null, excludeColloection);

		request.setResolutionFilter(collectionFilter);
		return resolver.resolve(request);
	}
}
