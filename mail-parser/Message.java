import java.util.HashMap;
public class Message {
    HashMap<String,String> headers;
    String content;
    public Message() {
        headers = new HashMap<String,String>();
    }
}
