package tk.wlemuel.cotable.model;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * BlogDetail
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogDetail
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class BlogDetail extends Entity {

    private final static String TAG_BODY = "string";

    private String postId;

    private String body;

    public BlogDetail(String postId, String body) {
        setPostId(postId);
        setBody(body);
    }

    public static BlogDetail parse(String data) {
        BlogDetail blog = new BlogDetail("", "<p>Hello, I'm Lemuel.</p>");


        if (data != null && !data.equals("")) {

            try {
                JSONTokener jsonParser = new JSONTokener(data);

                JSONObject content = (JSONObject) jsonParser.nextValue();
                blog.setBody(content.getString("data"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return blog;

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
