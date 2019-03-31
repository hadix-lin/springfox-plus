package hadix;

import hadix.staticdoc.DiskDocStore;
import hadix.staticdoc.DocStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.net.URL;

@SpringBootApplication
@EnableSwagger2
@RestController
@RequestMapping("/sample")
public class SampleController {

  @Bean
  public DocStore docStore() {
    URL resource = SampleController.class.getResource("/META-INF/static-doc");
    return new DiskDocStore(new File(resource.getFile()));
  }

  public static void main(String[] args) {
    new SpringApplication(SampleController.class).run(args);
  }

  /**
   * @param id 实体id参数
   * @param argModel 请求参数对象
   * @return 响应对象
   */
  @GetMapping("/get")
  public Response getSampleModel(Integer id, @ModelAttribute Request argModel) {
    return null;
  }
}
