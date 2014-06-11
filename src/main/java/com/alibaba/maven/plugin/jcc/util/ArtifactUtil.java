package com.alibaba.maven.plugin.jcc.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

import com.alibaba.maven.plugin.jcc.pojo.Jar;

public class ArtifactUtil {

	public static List<String> getClassFileName(Artifact artifact) {
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

	public static List<Jar> getAllClassByArtifactsAsList(
			Set<Artifact> artifacts, String source) throws Exception {
		if (artifacts == null || artifacts.size() == 0) {
			return null;
		}

		List<Jar> jarList = new ArrayList<Jar>();

		for (Artifact artifact : artifacts) {
			List<String> classes = ArtifactUtil.getClassFileName(artifact);
			Jar jar = new Jar();
			jar.setClasses(classes);
			jar.setClassSize(classes.size());
			jar.setSource(source);
			jar.setFile(artifact.getFile());
			jar.setMd5(DigestUtils.md5Hex(new FileInputStream(artifact.getFile())));
			if (StringUtils.isBlank(artifact.getGroupId())) {
				jar.setName(artifact.getArtifactId());
			} else {
				jar.setName(artifact.getGroupId() + "-"
						+ artifact.getArtifactId() + "-"
						+ artifact.getVersion());
				jar.setGroupId(artifact.getGroupId());
				jar.setArtifactId(artifact.getArtifactId());
				jar.setVersion(artifact.getVersion());
			}
			jarList.add(jar);
		}
		return jarList;
	}

	public static Map<String,List<Jar>> getAllClassByArtifactsAsMap(Set<Artifact> artifacts) throws Exception{
		if(artifacts == null || artifacts.size() == 0){
			return null;
		}		

		Map<String,List<Jar>> map = new HashMap<String, List<Jar>>();
		for(Artifact artifact : artifacts){
			List<String> classes = ArtifactUtil.getClassFileName(artifact);
			Jar jar = new Jar();	
			jar.setClassSize(classes.size());
			jar.setFile(artifact.getFile());			
			if(StringUtils.isBlank(artifact.getGroupId())){
				jar.setName(artifact.getArtifactId());
            }else{
            	jar.setName(artifact.getGroupId()+"-"+artifact.getArtifactId()+"-"+artifact.getVersion());
            	jar.setGroupId(artifact.getGroupId());
            	jar.setArtifactId(artifact.getArtifactId());
            	jar.setVersion(artifact.getVersion());
            }	
			jar.setMd5(DigestUtils.md5Hex(new FileInputStream(artifact.getFile())));
			
			for(String str : classes){
				List<Jar> jars = map.get(str);
				if(jars == null){
					jars = new ArrayList<Jar>();
					jars.add(jar);
					map.put(str, jars);
				}else{
					jars.add(jar);					
				}
			}
			
		}		
		return map;		
		
	}
}
