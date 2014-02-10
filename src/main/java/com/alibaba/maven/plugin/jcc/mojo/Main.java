package com.alibaba.maven.plugin.jcc.mojo;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.maven.artifact.Artifact;

import com.alibaba.maven.plugin.jcc.pojo.WarLibArtifact;

public class Main {

	public static void main(String[] args) {
		/*File file = new File("D:\\alibaba\\aliProject\\dowjones\\bundle\\war\\target\\b2b.crm.dowjones.bundle.war-1.0-SNAPSHOT\\WEB-INF\\lib");
		
		 File[] listFiles = file.listFiles(new FileFilter() {
		       
	            public boolean accept(File pathname) {
	                if (pathname.getName().endsWith(".jar")) {
	                    return true;
	                }
	                return false;
	            }
	        });
	        Set<Artifact> artifacts = new HashSet<Artifact>();
	        for (File f : listFiles) {
	        	Artifact artifact = new WarLibArtifact(f.getName());
	        	artifact.setFile(f);
	        	artifacts.add(artifact);
	        }  
	        System.out.println(listFiles.length);
	        System.out.println(artifacts.size());*/
		
             Set set = new LinkedHashSet();
             
             set.add(new WarLibArtifact("a"));
             set.add(new WarLibArtifact("b"));
             System.out.println(set.size());
	}

}
