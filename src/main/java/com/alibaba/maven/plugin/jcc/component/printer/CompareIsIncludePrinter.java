package com.alibaba.maven.plugin.jcc.component.printer;

import java.util.List;

import com.alibaba.maven.plugin.jcc.component.checker.AbstractChecker;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.JccArtifact;

public class CompareIsIncludePrinter extends AbstractPrinter {

	public CompareIsIncludePrinter(AbstractChecker checker) {
		super(checker);		
	}


	@Override
	public void printer() {
	
		System.out.print("\t**                                                                                              **"+"\r\n");
		if(checker.getConflictResults() == null || checker.getConflictResults().size() == 0){
			System.out.println("");
			System.out.println("\t\t no conflict");
			System.out.println("");
			return;
		}
		//System.out.print("\t**  conflict jar count : ��"+ conflictChecker.getConflictResults().size() +"��                                                                  **"  + "\n" );
		//System.out.print("\t**                                                                                              **"+"\r\n");
		System.out.print("\t**  difference detail:                                                                            **"+ "\n");	
	
		for(Object o : checker.getConflictResults()){
			System.out.println();
			ConflictResult conflictResult = (ConflictResult)o;
			/*if(conflictResult.getConflictProjectJars().size() == 0 || conflictResult.getConflictClasses().size() == 0){
				continue;
			}	*/	
			
			//��ӡ��ͻ��jar��Ϣ
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
		if(conflictProjectJars == null || conflictProjectJars.size() == 0)
		{
			System.out.print("\t\t\t@");
			System.out.println(" conflict class size:\t" +  0 +"");			
			System.out.println();
			printDependencyTree(conflictResult,conflictResult.getConflictParamJar(),4,true,true);
		}
		else
		{
			boolean hasPrinterParamJar = false;
			for(JccArtifact  jccArtifact : conflictProjectJars){
					
				if(!hasPrinterParamJar){
					System.out.print("\t\t\t@");				
					//��ӡ��ͻ�˶�����
					System.out.println(" conflict class size:\t" +  conflictResult.getConflictClasses().size() +"");
					System.out.println();
					printDependencyTree(conflictResult,conflictResult.getConflictParamJar(),4,true,true);
					hasPrinterParamJar = true;
				}
				System.out.println();
				printDependencyTree(conflictResult,jccArtifact,4,true,false);			
			}
		}	
		
	}	
	
	
	/**
	 * 
	 * @param jccArtifact
	 * @param blankCount  �ո������
	 * @param isSelf  �Ƿ��ǳ�ͻ��JAR �����Ǹ�JAR
	 * @param isParam �Ƿ��Ǳ�����ͻ��JAR�����
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
				str.append("��P��("+jccArtifact.getClassSize()+") ");				
			}else{
				str.append("("+jccArtifact.getClassSize()+") ");
				if(conflictResult.getConflictClasses().size() == jccArtifact.getClassSize() &&
						conflictResult.getConflictClasses().size() ==	conflictResult.getConflictParamJar().getClassSize()){
					// �����඼��ͻ
					if(jccArtifact.getMd5().equals(conflictResult.getConflictParamJar().getMd5())){
						//str.append("======��warn��"); //����
					}else{
						str.append("======��md5-error�� ");
					}
				}else if(conflictResult.getConflictClasses().size() != jccArtifact.getClassSize() &&
						conflictResult.getConflictClasses().size() !=	conflictResult.getConflictParamJar().getClassSize()){
					str.append("======��different conflict classes in 2 jars��");
				}else if(conflictResult.getConflictClasses().size() == jccArtifact.getClassSize() ||
						conflictResult.getConflictClasses().size() ==	conflictResult.getConflictParamJar().getClassSize()){
					str.append("======��same conflict classes in 1 jar��");
				}else{
					str.append("======��unkown��");
				}
			}			
		}else{
			str.append(" "+jccArtifact.getName());
		}
		System.out.println(str);
		
		printDependencyTree(conflictResult,jccArtifact.getParentJccArtifact(),blankCount,false,isParam);
		
	}


}
