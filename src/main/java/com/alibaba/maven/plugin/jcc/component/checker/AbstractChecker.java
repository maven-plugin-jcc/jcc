package com.alibaba.maven.plugin.jcc.component.checker;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import com.alibaba.maven.plugin.jcc.component.LifeCycle;
import com.alibaba.maven.plugin.jcc.component.printer.AbstractPrinter;
import com.alibaba.maven.plugin.jcc.pojo.ConflictResult;
import com.alibaba.maven.plugin.jcc.pojo.Header;

public abstract class AbstractChecker<R extends EnforcerRule> extends AbstractLogEnabled implements LifeCycle {
	
	protected AbstractPrinter printer;
	protected List<ConflictResult> conflictResults;
	protected List<Header> headers;
	protected R rule;
	
	
	
	protected AbstractChecker(R r){
		this.rule = r;				
	}	

	@Override
	public void start() throws Exception {
		printHeader();
		transformConflictResult();
		printer.start();		
	}
	
	@Override
	public void init() throws Exception {		
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}	
	
	public void printHeader(){
		System.out.println();
		System.out.println();
		System.out.print("\t**************************************************************************************************" + "\n" );
		System.out.print(( "\t**                                                                                              **" + "\n" ));
		System.out.print("\t**\t                           JCC MAVEN PLUGIN                                                 **" + "\n" );
		System.out.print("\t**\t                                                                                            **" + "\n" );
		
		for(Header header : this.headers){
			StringBuffer headerStr = new StringBuffer();
			headerStr.append("\t\t"+ header.getName());
			if(StringUtils.isNotBlank(header.getDesc())){
				headerStr.append("¡¾"+ header.getDesc() +"¡¿");
			}
			headerStr.append("has ["+header.getJarSize()+"]  dependency jar            "  + "\n" );
			System.out.println(headerStr.toString());
		}			
	}
	
	public abstract List<Header> construnctHeader();
	
	public abstract void transformConflictResult() throws Exception ;
				


	public List<ConflictResult> getConflictResults() {
		return conflictResults;
	}

	public void setPrinter(AbstractPrinter printer) {
		this.printer = printer;
	}
	
	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}	
	
}
