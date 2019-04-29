package io.github.hadixlin.springfoxplus.sample;

import io.github.hadixlin.springfox.ApiDocGroup;
import io.github.hadixlin.springfox.ApiDocInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/sample")
public class SampleController {
    @Bean
    public ApiDocGroup adminGroup() {
        ApiDocGroup group = new ApiDocGroup("后台管理接口分组");
        group.setPathPrefix(new String[] {"/smaple"});
        group.setPathPattern("/sample.*");

        ApiDocInfo info = new ApiDocInfo();
        info.setTitle("后台管理接口文档");
        info.setDescription("这里可以添加详细的文档信息,可以是html格式");
        info.setContact("作者的名字");
        info.setVersion("v1.0");
        group.setApiDocInfo(info);
        return group;
    }

    public static void main(String[] args) {
        new SpringApplication(SampleController.class).run(args);
    }

    /**
     * 示例方法<br>
     * 示例方法的注释<br>
     * 示例方法的注释第二行
     *
     * @param id 实体id参数
     * @param argModel 请求参数对象
     * @return 响应对象
     */
    @GetMapping("/get")
    public Response getSampleModel(Integer id, @ModelAttribute Request argModel) {
        return null;
    }
}
