## Introduction 
*   git clone https://github.com/maven-plugin-jcc/jcc
*   cd jcc && mvn install

### how to use
``` 
  <plugin>
		<groupId>com.alibaba.maven.plugins</groupId>
	    <artifactId>maven-jcc-plugin</artifactId>
	    <version>0.2</version>
	    <executions>
	      <execution>
	         <id>check</id>	       
	         <goals>
	            <goal>check</goal>
	         </goals>
	      </execution>	      
	    </executions>
  		</plugin>
```

###demo
jccDemoTest is a maven project,it depend 2 jar:
```
    <dependency>
       <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>0.2.24</version>
    </dependency>
    <dependency>
       <groupId>com.alibaba.crm</groupId>
      <artifactId>jccDemoTest2</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>

```

jccDemoTest2 is alst a maven project,it depend 2 jar:
```
<dependency>
       <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.0.1</version>
    </dependency>
    <dependency>
      <groupId>commons-dbcp</groupId>
      <artifactId>commons-dbcp</artifactId>
      <version>1.4</version>
    </dependency>
```

now I want to add the *tddl-client* to the pom of *jccDemoTest*,but I don't know what the *tddl-client* dependency tree will do any harm to the *jccDemoTest*.

so i can use jcc maven plugin to check is there any conflict:

```
mvn jcc:check -DjarPath=D:\.m2\com\taobao\tddl\tddl-client\3.3.1.0\tddl-client-3.3.1.0.jar

```

console will print:
```
resolve begin..........
jar count of the -Djarpath method: 52
jar count of project : 5

==========start print conflict list=============

>>>> conflict jar:  commons-dbcp-commons-dbcp-1.2.2[P](51)  commons-dbcp-commons-dbcp-1.4(62) conflict class count:44
>>>> conflict detail： commons-dbcp-commons-dbcp-1.4(different-error) 


>>>> conflict jar:  com.alibaba-druid-1.0.2[P](968)  com.alibaba-druid-0.2.24(930) conflict class count:911
>>>> conflict detail： com.alibaba-druid-0.2.24(different-error) 


>>>> conflict jar:  commons-pool-commons-pool-1.3[P](39)  commons-pool-commons-pool-1.5.4(52) conflict class count:39
>>>> conflict detail： commons-pool-commons-pool-1.5.4(same-error) 


>>>> conflict jar:  junit-junit-4.4[P](154)  junit-junit-3.8.1(100) conflict class count:23
>>>> conflict detail： junit-junit-3.8.1(different-error) 

==========conflict jar count：4
==========end print conflict list=============
```

* [P] : the jar of -DjarPath method 
* (952) : the class count of jar
* (different-error)
* (same-error) 
* (warn)
* (md5-error)



## License

[GPL](http://opensource.org/licenses/gpl-license)


