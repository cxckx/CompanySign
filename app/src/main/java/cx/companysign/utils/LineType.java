package cx.companysign.utils;

/**
 * Created by cxcxk on 2016/10/15.
 */
public class LineType {
    public static int LABLE = 0;
    public static int CONTENT = 1;

    private String content;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
