## Introduction 
*   git clone https://github.com/maven-plugin-jcc/jcc
*   cd jcc && mvn install

### what's it
jcc is a rule can be invoked with the [maven-enforcer-plugin](http://maven.apache.org/enforcer/maven-enforcer-plugin/) for checking jar conflict.

The example shown below will teach you how to check conflicts in your java project.

### how to use

1.[check conflict with 2 mavenProject](https://github.com/maven-plugin-jcc/jcc/wiki/check-2-mavenProject-conflict-jar)

2.[check conflict with a mavenProject and a folder]()




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


