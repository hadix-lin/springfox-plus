package hadix.javadoc;

/** 这是一个JavaClass示例 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class JavaClass {
  /** 字段1 */
  private String field1;
  /** 字段2 */
  private String field2;
  /** 字段3 */
  private String field3;

  /**
   * 一个示例方法
   *
   * @param p1 参数1
   * @param p2 参数2
   * @return 返回字符串值
   */
  public String methodOfClass(String p1, int p2) {
    return null;
  }

  /** f1 getter */
  public String getField1() {
    return field1;
  }

  /** f1 setter */
  public void setField1(String field1) {
    this.field1 = field1;
  }

  /** f2 getter */
  public String getField2() {
    return field2;
  }

  /** f3 setter */
  public void setField3(String field3) {
    this.field3 = field3;
  }

  public static class InnerClass {
    /** 字段1 */
    private String field1;
    /** 字段2 */
    private String field2;
    /** 字段3 */
    private String field3;

    /**
     * 一个示例方法
     *
     * @param p1 参数1
     * @param p2 参数2
     * @return 返回字符串值
     */
    public String methodOfClass(String p1, int p2) {
      return null;
    }

    /** f1 getter */
    public String getField1() {
      return field1;
    }

    /** f1 setter */
    public void setField1(String field1) {
      this.field1 = field1;
    }

    /** f2 getter */
    public String getField2() {
      return field2;
    }

    /** f3 setter */
    public void setField3(String field3) {
      this.field3 = field3;
    }
  }
}
