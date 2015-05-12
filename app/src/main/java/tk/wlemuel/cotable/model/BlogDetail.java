package tk.wlemuel.cotable.model;

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

    private final static String HEAD = "<html>\n" +
            "<head>\n" +
            " <title>cnblogs</title>\n" +
            "<meta name=\"viewport\" content=\"width=device-width, minimum-scale=0.5, initial-scale=1.2, maximum-scale=2.0, user-scalable=1\" />\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"content\">";
    private final static String FOOT = "</div>\n" +
            "</body>\n" +
            "</html>";

    private String postId;

    private String body;

    public BlogDetail(String postId, String body) {
        setPostId(postId);
        setBody(body);
    }

    public static BlogDetail parse(String data) {
        BlogDetail blog = new BlogDetail("", "<p>Hello, I'm Lemuel.</p>");


        if (data != null && !data.equals("")) {
//            data = data.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><string>", "");
//
//            data = data.replace("</string>", "");
//
//            data = data.replaceAll("&amp;", "");
//            data = data.replaceAll("quot;", "\"");
//            data = data.replaceAll("lt;", "<");
//            data = data.replaceAll("gt;", ">");
//
//            blog.setBody(data);

            data = data.replace("{\"data\":\"", "");
            data = data.replace("\\/", "/");
            data = data.replace("\",\"op\":\"GetNewContent\"}'", "");
            blog.setBody(data);
        }

        return blog;

//        if(!data.endsWith("</xml>"))
//            data = data + "</xml>";
//
//        XmlPullParser xp = Xml.newPullParser();
//        try {
//            xp.setInput(new StringReader(data));
//
//            int eventType = xp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT){
//                String tag = xp.getName();
//
//                switch (eventType){
//                    case XmlPullParser.START_DOCUMENT:
//                        break;
//                    case XmlPullParser.START_TAG:
//                        if(tag.equalsIgnoreCase(TAG_BODY)){
//                            blog.setBody(xp.nextText());
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if(tag.equalsIgnoreCase(TAG_BODY)){
//                            return blog;
//                        }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

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
