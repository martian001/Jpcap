

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {



    public static String post(String uri, Map<String, Object> params) {
        HttpPost httppost = new HttpPost(uri);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            nvps.add(new BasicNameValuePair(entry.getKey(), value != null ? value.toString() : null));
        }
        httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        return execute(httppost);
    }

    private static String execute(HttpUriRequest request) {
        String out = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse rsp;
        int status = 0;
        try {
            rsp = client.execute(request);
            status = rsp.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = rsp.getEntity();
                out = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }
    
    public static byte[] getBytes(String url) throws ClientProtocolException, IOException {
    	HttpGet httpGet = new HttpGet(url);
    	
        byte[] out = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        int status = 0;
        try {
        	response = client.execute(httpGet);
            status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                out = EntityUtils.toByteArray(entity);
            }
        }finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }
}
