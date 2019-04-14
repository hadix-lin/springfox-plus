package io.github.hadixlin;

public class Response {
  /** 参数1的注释 */
  private String param1;
  /** 参数2的注释 */
  private String param2;
  /** 内嵌参数 */
  private InnerResponse innerResponse;

  public static class InnerResponse {
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

  public InnerResponse getInnerResponse() {
    return innerResponse;
  }

  public void setInnerResponse(InnerResponse innerResponse) {
    this.innerResponse = innerResponse;
  }
}
