package com.alibaba.maven.plugin.jcc.component;

import java.io.File;
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

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.Jar;

public class ConflictChecker extends AbstractLogEnabled {	
	
	public void compare(Set<Artifact> jarDependencyTree,Set<Artifact> projectDependencyTree) throws Exception{
		System.out.println("正在解析..........");
		System.out.println("参数中依赖的jar个数 : "+jarDependencyTree.size());
		System.out.println("工程中的jar个数 : " + projectDependencyTree.size());
		List<Jar> jarDependencyClass =  getAllClassByArtifactsAsList(jarDependencyTree,"param");		
		Map<String,List<Jar>> projectDependencyClass = getAllClassByArtifactsAsMap(projectDependencyTree);	
		
		if((jarDependencyClass == null || jarDependencyClass.size() == 0) || 
				(projectDependencyClass == null || projectDependencyClass.isEmpty())){
			System.out.println("no jar need to be check conflict");
			return;
		}		
		
	    List<ConflictResult>  conflictResults = new ArrayList<ConflictResult>();
		
		for(Jar jar : jarDependencyClass){		
			List<String> classes = jar.getClasses();
			if(classes == null || classes.size() == 0){	
				System.out.println("jar ["+jar.getName()+"] has no class");
				continue;
			}
			ConflictResult conflictResult = new ConflictResult();
			for(String clazz : classes){
				if(!projectDependencyClass.keySet().contains(clazz)){
					continue;
				}		
				conflictResult.addConflictClass(clazz);
				List<Jar> jars = projectDependencyClass.get(clazz);
				conflictResult.addConflictJar(jar);
				conflictResult.addConflictJars(jars);				
			}
			if(conflictResult.getConflictJars() != null && conflictResult.getConflictJars().size() > 1){
				conflictResults.add(conflictResult);
			}			
		}
		
		printConflict(conflictResults);
		
	}
	
	private void printConflict(List<ConflictResult>  conflictResults){
		System.out.println("");
		System.out.println("==========start print conflict list=============");
		if(conflictResults == null || conflictResults.size() == 0){
			System.out.println("");
			System.out.println("no conflict");
			System.out.println("");
		}
		
		for(ConflictResult conflictResult : conflictResults){
			if(conflictResult.getConflictJars().size() == 0 || conflictResult.getConflictClasses().size() == 0){
				continue;
			}
			System.out.println("");
			List<Jar> conflictJars = conflictResult.getConflictJars();					
			System.out.print(">>>> 冲突jar:");		
			for(Jar jar : conflictJars){
				if(StringUtils.isNotBlank(jar.getSource()) & "param".equals(jar.getSource())){
					System.out.print(" "+jar.getName() + "[P]("+jar.getClassSize()+") ");
				}else{
					System.out.print(" "+jar.getName()+"("+jar.getClassSize()+") ");
				}					
			}		
			System.out.println();
			if(checkIsSameJarDifferentVersion(conflictJars)){				
				System.out.print(">>>> 冲突原因：");
				System.out.println("相同GID和AID,高低版本");
			}
			System.out.println(">>>> 冲突类个数:"+conflictResult.getConflictClasses().size());
			System.out.println("");
			System.out.println("");
		}	
		
		System.out.println("==========冲突的JAR总数："+conflictResults.size());
		System.out.println("==========end print conflict list=============");
		System.out.println("");
	}
	
	private boolean checkIsSameJarDifferentVersion(List<Jar> jars){
		if(jars == null || jars.size() ==0){
			return false;
		}
		String name="",gid="",aid="";
		for(int index = 0; index < jars.size(); index ++){
			Jar jar = jars.get(index);
			if(jar.getName().equals(name)){
				return true;
			}else if(StringUtils.isNotBlank(gid) && StringUtils.isNotBlank(jar.getGroupId())
					&& jar.getGroupId().equals(gid) && jar.getArtifactId().equals(aid)){
				return true;
			}else{
				name = jar.getName();
				gid = jar.getGroupId();
				aid = jar.getArtifactId();
			}			
		}
		return false;
	}
	
	private Map<String,List<Jar>> getAllClassByArtifactsAsMap(Set<Artifact> artifacts) throws Exception{
		if(artifacts == null || artifacts.size() == 0){
			return null;
		}		

		Map<String,List<Jar>> map = new HashMap<String, List<Jar>>();
		for(Artifact artifact : artifacts){
			List<String> classes = getClassFileName(artifact);
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
	
	
	private List<Jar> getAllClassByArtifactsAsList(Set<Artifact> artifacts,String source) throws Exception{
		
		if(artifacts == null || artifacts.size() == 0){
			return null;
		}		

		List<Jar> jarList = new ArrayList<Jar>();
		
		for(Artifact artifact : artifacts){
			List<String> classes = getClassFileName(artifact);
			Jar jar = new Jar();	
			jar.setClasses(classes);
			jar.setClassSize(classes.size());
			jar.setSource(source);
			jar.setFile(artifact.getFile());	
			if(StringUtils.isBlank(artifact.getGroupId())){
				jar.setName(artifact.getArtifactId());
            }else{
            	jar.setName(artifact.getGroupId()+"-"+artifact.getArtifactId()+"-"+artifact.getVersion());
            	jar.setGroupId(artifact.getGroupId());
            	jar.setArtifactId(artifact.getArtifactId());
            	jar.setVersion(artifact.getVersion());
            }
			jarList.add(jar);
		}		
		return jarList;		
	}
	
	private List<String> getClassFileName(Artifact artifact) {	
		List<String> conflictClasses = new ArrayList<String>();
		try{
			File file  = artifact.getFile();					
			URL url = new URL("jar:file:" + file.getPath() + "!/");
			JarURLConnection conn = ((JarURLConnection) url.openConnection());
	        conn.setUseCaches(false);
	        JarFile jarFile = conn.getJarFile();
	        Enumeration<JarEntry> entries = jarFile.entries();
	        while(entries.hasMoreElements()){
	        	JarEntry entry = entries.nextElement();
	        	String fileName = entry.getName();
	            if (fileName.endsWith(".class")) {
	                fileName = fileName.replace('/', '.');                  
	                conflictClasses.add(fileName);
	            }
	        }
	        jarFile.close();
		}catch(Exception e){
			
		}
		
        return conflictClasses;
	}
	
}
