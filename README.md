## Introduction 
*   git clone https://github.com/maven-plugin-jcc/jcc
*   cd jcc && mvn install

如何在工程POM里加入该插件：
``` html
  <plugin>
		<groupId>com.alibaba.maven.plugins</groupId>
	    <artifactId>maven-jcc-plugin</artifactId>
	    <version>0.1</version>
	    <executions>
	    <execution>
	         <id>check</id>	       
	         <goals>
	            <goal>check</goal>
	         </goals>
	      </execution>
	      <execution>
	         <id>check-war</id>	       
	         <goals>
	            <goal>check-war</goal>
	         </goals>
	      </execution>
	    </executions>
  		</plugin>
```

如果你的JAR是和WAR包lib目录下的包比较，那么就使用如下的命令：
``` html
  mvn jcc:check-war -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
```

如果你的JAR是和非WAR包工程比较，那么就使用如下的命令：

``` html
  mvn jcc:check -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
```

结果输出描述:

``` html  
  正在解析..........
  参数中依赖的jar个数 : 1
  工程中的jar个数 : 1

   ==========start print conflict list=============

   >>>> 冲突jar: com.alibaba-druid-1.0.1[P](952)  druid-1.0.1.jar(952)
   >>>> 冲突类个数:952

   ==========冲突的JAR总数：1
   ==========end print conflict list=============  

```

* [P] : 由参数输入引入的JAR 
* (952) : JAR中包含类的总个数


## License

[GPL](http://opensource.org/licenses/gpl-license)


