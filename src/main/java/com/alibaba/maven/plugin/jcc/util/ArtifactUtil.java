package com.alibaba.maven.plugin.jcc.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;

public class ArtifactUtil {

	public static List<String> getClassFileName(JccArtifact artifact) {
		List<String> conflictClasses = new ArrayList<String>();
		try {
			File file = artifact.getFile();
			URL url = new URL("jar:file:" + file.getPath() + "!/");
			JarURLConnection conn = ((JarURLConnection) url.openConnection());
			conn.setUseCaches(false);
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String fileName = entry.getName();
				if (fileName.endsWith(".class")) {
					fileName = fileName.replace('/', '.');
					conflictClasses.add(fileName);
				}
			}
			jarFile.close();
		} catch (Exception e) {

		}

		return conflictClasses;
	}

	public static List<JccArtifact> getAllClassByArtifactsAsList(Collection<JccArtifact> artifacts, String source) throws Exception {
		if (artifacts == null || artifacts.size() == 0) {
			return null;
		}

		List<JccArtifact> list = new ArrayList<JccArtifact>();

		for (JccArtifact artifact : artifacts) {
			
			List<String> classes = ArtifactUtil.getClassFileName(artifact);
			artifact.setClasses(classes);
			artifact.setClassSize(classes.size());
			artifact.setSource(source);
			artifact.setMd5(DigestUtils.md5Hex(new FileInputStream(artifact.getFile())));
			if (StringUtils.isBlank(artifact.getGroupId())) {
				artifact.setName(artifact.getArtifactId());
			} else {
				artifact.setName(artifact.getGroupId() + "-"
						+ artifact.getArtifactId() + "-"
						+ artifact.getVersion());				
			}
			list.add(artifact);						
		}
		return list;
	}

	public static Map<String,List<JccArtifact>> getAllClassByArtifactsAsMap(Collection<JccArtifact> artifacts) throws Exception{
		if(artifacts == null || artifacts.size() == 0){
			return null;
		}		

		Map<String,List<JccArtifact>> map = new HashMap<String, List<JccArtifact>>();
		for(JccArtifact artifact : artifacts){
			List<String> classes = ArtifactUtil.getClassFileName(artifact);			
			artifact.setClassSize(classes.size());
			
			if(StringUtils.isBlank(artifact.getName())){
				if(StringUtils.isBlank(artifact.getGroupId())){
					artifact.setName(artifact.getArtifactId());
	            }else{
	            	artifact.setName(artifact.getGroupId()+"-"+artifact.getArtifactId()+"-"+artifact.getVersion());            	
	            }	
			}				
			
			artifact.setMd5(DigestUtils.md5Hex(new FileInputStream(artifact.getFile())));
			
			
			for(String str : classes){
				List<JccArtifact> jars = map.get(str);
				if(jars == null){
					jars = new ArrayList<JccArtifact>();
					jars.add(artifact);
					map.put(str, jars);
				}else{
					jars.add(artifact);					
				}
			}
			
		}		
		return map;		
		
	}
	
	public static JccArtifact toJccArtifact(Artifact artifact){
		if(artifact == null){
			return null;
		}
		JccArtifact jccArtifact = new JccArtifact();
		jccArtifact.setFile(artifact.getFile());
		jccArtifact.setGroupId(artifact.getGroupId());
		jccArtifact.setArtifactId(artifact.getArtifactId());
		jccArtifact.setVersion(artifact.getVersion());
		jccArtifact.setScope(jccArtifact.getScope());
		
		return jccArtifact;
	}
	
	public static List<JccArtifact> toJccArtifacts(Set<Artifact> artifacts) throws Exception{
		if(artifacts == null || artifacts.size() == 0){
			return null;
		}
		
		List<JccArtifact> jccArtifacts = new ArrayList<JccArtifact>();
		
		for(Artifact artifact : artifacts){
			jccArtifacts.add(toJccArtifact(artifact));
		}
		
		return 	jccArtifacts;
		
	}
	
	public static String mavenProject2Str(MavenProject mavenProject){
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
}
