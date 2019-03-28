package hadix.javadoc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
public interface JavaInterface {

  /**
   * 这是一个示例方法
   *
   * @param arg 参数
   * @return 字符串结果
   */
  @ApiOperation("示例方法")
  String doSomeThing(Integer arg);
}
