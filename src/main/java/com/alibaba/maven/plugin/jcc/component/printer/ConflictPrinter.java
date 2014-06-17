package com.alibaba.maven.plugin.jcc.component.printer;

import java.util.List;

import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.checker.ConflictChecker;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;

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
		
		System.out.print("\t**                                                                                              **"+"\r\n");
		if(conflictChecker.getConflictResults() == null || conflictChecker.getConflictResults().size() == 0){
			System.out.println("");
			System.out.println("no conflict");
			System.out.println("");
			return;
		}
		//System.out.print("\t**  conflict jar count : 【"+ conflictChecker.getConflictResults().size() +"】                                                                  **"  + "\n" );
		//System.out.print("\t**                                                                                              **"+"\r\n");
		System.out.print("\t**  conflict detail:                                                                            **"+ "\n");	
		for(ConflictResult conflictResult : conflictChecker.getConflictResults()){
			System.out.println();
			if(conflictResult.getConflictProjectJars().size() == 0 || conflictResult.getConflictClasses().size() == 0){
				continue;
			}		
			
			//打印冲突的jar信息
			printDependencyTree(conflictResult);			
		}				
		System.out.print("\t**                                                                                              **"+"\r\n");
		System.out.print("\t**                                                                                              **"+"\r\n");
	}
	
	
	private void printDependencyTree(ConflictResult conflictResult){
		if(conflictResult == null){
			return;
		}
		
		List<JccArtifact> conflictProjectJars =  conflictResult.getConflictProjectJars();
		if(conflictProjectJars == null || conflictProjectJars.size() == 0){
			return;
		}
		
		boolean hasPrinterParamJar = false;
		for(JccArtifact  jccArtifact : conflictProjectJars){
			if(!conflictResult.willbeHappen(jccArtifact)){
				//System.out.println("jar ["+ jar.toString() +"] will not conflict with  ["+conflictResult.getConflictParamJar().toString()+"]");
				continue;
			}			
			if(!hasPrinterParamJar){
				System.out.print("\t\t\t@");				
				//打印冲突了多少类
				System.out.println(" conflict class size:【" +  conflictResult.getConflictClasses().size() +"】");
				System.out.println();
				printDependencyTree(conflictResult,conflictResult.getConflictParamJar(),4,true,true);
				hasPrinterParamJar = true;
			}
			System.out.println();
			printDependencyTree(conflictResult,jccArtifact,4,true,false);			
		}
	}	
	
	
	/**
	 * 
	 * @param jccArtifact
	 * @param blankCount  空格的数量
	 * @param isSelf  是否是冲突的JAR 而不是父JAR
	 * @param isParam 是否是被检查冲突的JAR引入的
	 */
	private void printDependencyTree(ConflictResult conflictResult,JccArtifact jccArtifact,int blankCount,boolean isSelf,boolean isParam){		
		if(jccArtifact == null){
			return;
		}
		
		blankCount++;
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < blankCount; i++){
			str.append("\t");
		}
		
		
		
		if(isSelf){
			str.append(" "+jccArtifact.getName() );
			if(isParam){
				str.append("[P]("+jccArtifact.getClassSize()+") ");				
			}else{
				str.append("("+jccArtifact.getClassSize()+") ");
				if(conflictResult.getConflictClasses().size() == jccArtifact.getClassSize() &&
						conflictResult.getConflictClasses().size() ==	conflictResult.getConflictParamJar().getClassSize()){
					// 所有类都冲突
					if(jccArtifact.getMd5().equals(conflictResult.getConflictParamJar().getMd5())){
						str.append("======>>>warn");
					}else{
						str.append("======>>>md5-error ");
					}
				}else if(conflictResult.getConflictClasses().size() != jccArtifact.getClassSize() &&
						conflictResult.getConflictClasses().size() !=	conflictResult.getConflictParamJar().getClassSize()){
					str.append("======>>>different conflict class in 2 jars");
				}else if(conflictResult.getConflictClasses().size() == jccArtifact.getClassSize() ||
						conflictResult.getConflictClasses().size() ==	conflictResult.getConflictParamJar().getClassSize()){
					str.append("======>>>same conflict class in one jar");
				}else{
					str.append("======>>>unkown");
				}
			}			
		}else{
			str.append(" "+jccArtifact.getName());
		}
		System.out.println(str);
		
		printDependencyTree(conflictResult,jccArtifact.getParentJccArtifact(),blankCount,false,isParam);
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}	
	
}
