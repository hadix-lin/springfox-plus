# SpringFox-Plus
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fspringfox-plus.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fspringfox-plus?ref=badge_shield)


## 概述

[SpringFox](<http://springfox.github.io/springfox/>)是一个优秀的项目,为基于Spring的RestAPI文档生成提供了强大的支持,另外还具有优秀的扩展机制.

SpringFox原生提供的插件,只能从Swagger-Annotation注解中提取描述文档.使用注解提供文档有如下缺陷:

1. 注解是运行时依赖,对代码有侵入性
2. 常规情况下注解的绝大部分属性是不需要的
3. 注解的名称冗长(`@ApiParam,@ApiOperation,@ApiModelProperty`)
4. 代码原本可能已经提供了javadoc注释,跟注解内容重复

SpringFox-Plus为Spring-Fox提供了读取javadoc作为API文档的能力.常规情况下可以替代`@ApiParam,@ApiOperation,@ApiModelProperty`等注解的使用.80%以上的API文档也是通过这三个注解提供的.

## 使用方法

1. 在项目中添加maven依赖

   ```xml
   <!-- springfox-plus -->
   <dependency>
       <groupId>io.github.hadix-lin</groupId>
       <artifactId>springfox-plus</artifactId>
       <version>1.0.2-SNAPSHOT</version>
   </dependency>
   
   <!-- 如果使用spring-boot -->
   <dependency>
       <groupId>io.github.hadix-lin</groupId>
       <artifactId>springfox-plus-spring-boot-starter</artifactId>
       <version>1.0.2-SNAPSHOT</version>
   </dependency>
   
   <!-- 使用SNAPSHOT版本需要声明如下仓库 -->
   <repositories>
     <repository>
       <id>oss</id>
       <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
       <snapshots>
         <enabled>true</enabled>
       </snapshots>
     </repository>
   </repositories>
   <pluginRepositories>
     <pluginRepository>
       <id>oss-plugin</id>
       <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
       <snapshots>
         <enabled>true</enabled>
       </snapshots>
     </pluginRepository>
   </pluginRepositories>
   ```
   
2. 导入Bean声明

   ```java
   // 方法一 : 在您的@Configuration类上加上如下代码
   @Import(io.github.hadixlin.springfoxplus.SpringfoxPlusConfiguration.class)
   
   // 方法二 : 确保`JavaDocAutoConfiguration`在Bean扫描范围内
   @ComponentScan(basepackages="io.github.hadixlin.springfoxplus")
   
   // 方法三 : 项目使用SpringBoot,引入maven依赖后,不需要额外配置代码
   // 可以参考源码中的"springfox-plus-sample"项目
   ```

3. 使用maven插件执行目标:`springfox-plus:javadoc`

   插件配置: 下面的配置将`javadoc`默认绑定到了`compile`阶段.所以在执行`mvn compile`时即可解析javadoc

   ```xml
   <build>
     <plugins>
       <plugin>
         <groupId>io.github.hadix-lin</groupId>
         <artifactId>springfox-plus-maven-plugin</artifactId>
         <version>${springfox-plus.version}</version>
         <executions>
           <execution>
             <id>javadoc</id>
             <phase>compile</phase>
             <goals>
               <goal>javadoc</goal>
             </goals>
             <configuration>
               <packages>
                 <!-- 指定要扫描的包,没有该配置默认扫描所有的java源文件-->
                 <p>io.github.hadixlin.springfoxplus</p>
               </packages>
             </configuration>
           </execution>
         </executions>
       </plugin>
     </plugins>
   </build>
   ```

   配置好maven插件后,在pom.xml所在目录中执行下面的命令即可

   ```bash
   mvn springfox-plus:javadoc 
   # 或
   mvn compile
   ```

4. 正常运行项目,使用swagger-ui查看API文档即可,可以看到API方法的的javadoc被读作作为API文档展示.

   swagger-ui可以直接通过`http://host:port/swagger-ui.html`来访问

## 进阶用法

1. 如果有项目中有大量的API接口,同时展示在一个页面上可能不太好看,加载也会比较慢.这时候就需要使用API分组

   ```xml
   <!-- 确保springfox-plus-support在项目的编译时依赖中 -->
   <dependency>
       <groupId>io.github.hadix-lin</groupId>
       <artifactId>springfox-plus-support</artifactId>
       <version>[the last version]</version>
   </dependency>
   ```

   然后在您的项目中定义Bean来描述分组情况,例如添加一个"后台管理接口分组"

   ```java
   @Bean
   public ApiDocGroup adminGroup() {
       // 定义分组
       ApiDocGroup group = new ApiDocGroup("后台管理接口分组");
       // prefix和pattern二选一即可,同时设置优先使用prefix
       group.setPathPrefix(new String[] {"/smaple"});
       group.setPathPattern("/sample.*");
   
       // 可选配置文档说明信息
       ApiDocInfo info = new ApiDocInfo();
       info.setTitle("后台管理接口文档");
       info.setDescription("这里可以添加详细的文档信息,可以是html格式");
       info.setContact("作者的名字");
       info.setVersion("v1.0");
       group.setApiDocInfo(info);
       return group;
   }
   ```

2. 项目发布到生产环境时,应该是不希望开放API文档服务的.有两种方法可以达到该目的

   * 如果您的项目是用`springfox-plus-spring-boot-starter`,那么可以在配置文件(通常是`application.properties`)中加入:

     `springfox-plus.enable=false`
     
   * 或者使用maven profile来管理构建,指定profile来构建包含springfox-plus
     ```xml
       <profiles>
         <profile>
           <id>with-api-doc</id>
           <dependencies>
             <dependency>
               <groupId>io.github.hadix-lin</groupId>
               <artifactId>springfox-plus-spring-boot-starter</artifactId>
               <version>[the last version]</version>
             </dependency>
           </dependencies>
         </profile>
       </profiles>
       <!-- springfox-plus-support 一定要包含在编译时依赖中 -->
       <dependencies>
         <dependency>
           <groupId>io.github.hadix-lin</groupId>
           <artifactId>springfox-plus-support</artifactId>
           <version>[the last version]</version>
         </dependency>
       </dependencies>
     ```

     然后在开发阶段构建时使用如下命令:

     ```bash
     mvn package -P with-api-doc
     ```


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fspringfox-plus.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fspringfox-plus?ref=badge_large)