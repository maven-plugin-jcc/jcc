## Introduction 
*   git clone https://github.com/maven-plugin-jcc/jcc
*   cd jcc && mvn install

### what's it
jcc is a rule can be invoked with the [maven-enforcer-plugin](http://maven.apache.org/enforcer/maven-enforcer-plugin/) for checking jar conflict.

The example shown below will teach you how to check conflicts in your java project.

### how to use

1.First copy the plugin config below to your pom file

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>1.3</version>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.maven.plugins</groupId>
                <artifactId>maven-jcc-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>
        <executions>
            <execution>
                <id>jcc</id>                        
                <configuration>
                    <rules>                             
                        <conflictRule implementation="com.alibaba.maven.plugin.jcc.rule.ConflictRule">                   
                            
                        </conflictRule>
                    </rules>
                </configuration>
                <goals>
                    <goal>enforce</goal>
                </goals>
            </execution>
        </executions>
    </plugin>


2.Build and Install your project like below

    mvn validate -Dg=com.taobao.tddl -Da=tddl-client -Dv=3.3.1.0 -Dmaven.test.skip=true

3.That's it.you will see the result on the console like below

![jcc.jpg](http://www.getsetter.cn/img/jcc.jpg)



###Note
-  **only support maven3**
- 【p】: the jar of be checked conflict or it's dependency jar (被检查冲突的jar或者是他的依赖的jar)
- 【different conflict class in 2 jars】: （冲突的类个数不等于其中任何一个JAR的类个数）
- 【same conflict class in one jar 】:（冲突的类个数等于其中一个JAR的类个数，这种一般是高低版本，可以排除低版本）
- 【md5-error】:（类个数相同，但是MD5不一致，这种必须排除）
- 【warn】:（类个数相同，MD5相同，排不排除无所谓）
- 【unkown】:（未知的类冲突类型）



## License

[GPL](http://opensource.org/licenses/gpl-license)


