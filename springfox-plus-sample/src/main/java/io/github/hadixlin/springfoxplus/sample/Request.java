package io.github.hadixlin.springfoxplus.sample;

/** 这是个请求参数对象 */
public class Request {
  /** 参数1的注释 */
  private String param1;
  /** 参数2的注释 */
  private String param2;
  /** 内嵌参数 */
  private InnerRequest innerRequest;

  public String getParam1() {
    return param1;
  }

  public void setParam1(String param1) {
    this.param1 = param1;
  }

  public String getParam2() {
    return param2;
  }

  public void setParam2(String param2) {
    this.param2 = param2;
  }

  public InnerRequest getInnerRequest() {
    return innerRequest;
  }

  public void setInnerRequest(InnerRequest innerRequest) {
    this.innerRequest = innerRequest;
  }

  public static class InnerRequest {
    /** 内嵌参数1 */
    private String param1;
    /** 内嵌参数2 */
    private String param2;

    public String getParam1() {
      return param1;
    }

    public void setParam1(String param1) {
      this.param1 = param1;
    }

    public String getParam2() {
      return param2;
    }

    public void setParam2(String param2) {
      this.param2 = param2;
    }
  }
}
