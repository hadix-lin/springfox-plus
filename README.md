# SpringFox-Plus

## 概述

[SpringFox](<http://springfox.github.io/springfox/>)是一个优秀的项目,为基于Spring的RestAPI文档生成提供了强大的支持,另外还具有优秀的扩展机制.

SpringFox原生提供的插件,只能从Swagger-Annotation注解中提取描述文档.使用注解提供文档有如下缺陷:

1. 注解是运行时依赖,对代码有侵入性
2. 常规情况下注解的绝大部分属性是不需要的
3. 注解的名称冗长(`@ApiParam,@ApiOperation,@ApiModelProperty`)
4. 代码原本可能已经提供了javadoc注释,跟注解内容重复

SpringFox-Plus为Spring-Fox提供了读取javadoc作为API文档的能力.常规情况下可以替代`@ApiParam,@ApiOperation,@ApiModelProperty`等注解的使用.80%异常的API文档也是通过这三个注解提供的.

## 使用方法

1. 在项目中添加maven依赖

   ```xml
   <!-- springfox deps -->
   <dependency>
       <groupId>io.springfox</groupId>
       <artifactId>springfox-swagger2</artifactId>
       <version>2.9.2</version>
   </dependency>
   <dependency>
       <groupId>io.swagger</groupId>
       <artifactId>swagger-annotations</artifactId>
       <version>1.5.21</version>
   </dependency>
   <dependency>
       <groupId>io.springfox</groupId>
       <artifactId>springfox-swagger-ui</artifactId>
       <version>2.9.2</version>
   </dependency>
   <!-- springfox-plus -->
   <dependency>
       <groupId>io.github.hadixlin</groupId>
       <artifactId>springfox-plus</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

2. 导入Bean声明

   ```java
   // 方法一 : 在您的@Configuration类上加上如下代码
   @Import(io.github.hadixlin.springfoxplus.JavaDocAutoConfiguration.class)
   
   // 方法二 : 确保`JavaDocAutoConfiguration`在Bean扫描范围内
   @ComponentScan(basepackages="io.github.hadixlin.springfoxplus")
   
   // 方法三 : 项目使用SpringBoot,引入maven依赖后,不需要额外配置代码
   // 可以参考源码中的"sample"项目
   ```

3. 在项目运行前执行javadoc解析命令,将javadoc从源文件中提取出来保存到项目的输入目录中

   ```bash
   java -jar springfox-plus-javadoc-parser-jar-with-dependencies.jar <srcRoot> <outputRoot>
   
   # srtRoot : 要扫描解析的java源代码的根目录 
   # outputRoot : 保存解析结果的目录
   ```

4. 正常运行项目,使用swagger-ui查看API文档即可,可以看到API方法的的javadoc被读作作为API文档展示.

