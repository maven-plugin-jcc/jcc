


## Introduction 
*   git clone https://github.com/maven-plugin-jcc/jcc
*   cd jcc && mvn install

if you compare the jar with the war project,you can user like:

``` html
  mvn jcc:check-war -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
```

if you compare the jar with the pom project,you can user like:

``` html
  mvn jcc:check -DjarPath=D:\.m2\com\alibaba\druid\1.0.1\druid-1.0.1.jar
```



## License

[GPL](http://opensource.org/licenses/gpl-license)


