package com.alibaba.maven.plugin.jcc.component.printer;

import java.util.List;
import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.checker.ConflictChecker;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.Jar;

public class ConflictPrinter implements LifeCycle {

	private ConflictChecker conflictChecker;
	
	public ConflictPrinter(ConflictChecker conflictChecker){
		this.conflictChecker = conflictChecker;
	}	

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() throws Exception {
		System.out.println("");
		System.out.println("==========start print conflict list=============");
		if(conflictChecker.getConflictResults() == null || conflictChecker.getConflictResults().size() == 0){
			System.out.println("");
			System.out.println("no conflict");
			System.out.println("");
		}
		
		for(ConflictResult conflictResult : conflictChecker.getConflictResults()){
			if(conflictResult.getConflictProjectJars().size() == 0 || conflictResult.getConflictClasses().size() == 0){
				continue;
			}
			System.out.println("");
			List<Jar> conflictProjectJars = conflictResult.getConflictProjectJars();	
			Jar conflictParamJar = conflictResult.getConflictParamJar();
			System.out.print(">>>> conflict jar: ");		
			//来自参数的JAR
			System.out.print(" "+conflictParamJar.getName() + "[P]("+conflictParamJar.getClassSize()+") ");
			for(Jar jar : conflictProjectJars){
				System.out.print(" "+jar.getName()+"("+jar.getClassSize()+") ");					
			}	
			System.out.print("conflict class count:"+conflictResult.getConflictClasses().size());
			System.out.println("");
			System.out.print(">>>> conflict detail：");	
			for(Jar jar : conflictProjectJars){
				if(conflictResult.getConflictClasses().size() == jar.getClassSize() &&
						conflictResult.getConflictClasses().size() ==	conflictParamJar.getClassSize()){
					// 所有类都冲突
					if(jar.getMd5().equals(conflictParamJar.getMd5())){
						System.out.print(" "+jar.getName()+"(warn) ");
					}else{
						System.out.print(" "+jar.getName()+"(md5-error) ");
					}
				}else if(conflictResult.getConflictClasses().size() != jar.getClassSize() &&
						conflictResult.getConflictClasses().size() !=	conflictParamJar.getClassSize()){
					System.out.print(" "+jar.getName()+"(different-error) ");
				}else if(conflictResult.getConflictClasses().size() == jar.getClassSize() ||
						conflictResult.getConflictClasses().size() ==	conflictParamJar.getClassSize()){
					System.out.print(" "+jar.getName()+"(same-error) ");
				}else{
					System.out.print(" "+jar.getName()+"(unkown) ");
				}										
			}					
			System.out.println("");
			System.out.println("");
		}	
		
		System.out.println("==========conflict jar count："+conflictChecker.getConflictResults().size());
		System.out.println("==========end print conflict list=============");
		System.out.println("");		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}	
	
}
