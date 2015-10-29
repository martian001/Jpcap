import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

public class NewShortMessageServiceImpl {

    /*    <bean id="newShortMessageService" class="com.qfang.service.impl.NewShortMessageServiceImpl">
        <property name="enterpriseID" value="11943" />
        <property name="url" value="http://58.251.49.114" />
        <property name="account" value="002" />
        <property name="pwd" value="20150505"></property>
        <!-- <property name="account" value="admin" />
        <property name="pwd" value="wabc1503"></property> -->
    </bean>*/
    private String account = "002";// 帐号
    private String pwd = "20150505"; // 密码
    private String url = "http://58.251.49.114";
    private String enterpriseID = "11943"; // 企业id

    public Boolean sendMessage(List<String> phones, String content) {
        String mobiles = StringUtils.join(phones, ",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enterpriseID", enterpriseID);
        params.put("loginName", account);
        params.put("password", MD5Encode(pwd));
        params.put("content", content);
        params.put("mobiles", mobiles);
        String result = HttpClientUtil.post(url + "/sendSMS.action", params);
        if (result != null && result.contains("<Result>0</Result>")) {
            return true;
        }
        return false;
    }

    // MD5 加密函数
    public String MD5Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }

    public final String byte2hexString(byte[] bytes) {
        StringBuffer bf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                bf.append("0");
            }
            bf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return bf.toString();
    }

    public double getBalance() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enterpriseID", this.enterpriseID);
        params.put("loginName", this.account);
        params.put("password", MD5Encode(pwd));
        String response = HttpClientUtil.post(this.url + "/getSmsBalance.action", params);
        if (StringUtils.isBlank(response)) {
            throw new Exception("余额查询请求失败");
        }
        // 解析结果
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(response);
            Element rootElt = doc.getRootElement();
            int result = Integer.parseInt(rootElt.elementText("Result"));
            if (result > 0) {
                if (result == 3) {//余额不足
                    return 0;
                } else {
                    throw new RuntimeException();
                }
            }
            return Double.parseDouble(rootElt.elementText("Balance"));
        } catch (DocumentException e) {
            throw new Exception("余额查询结果解析失败");
        }
    }

    @Test
    public void testGetBalance() {
        System.out.println("start");
        try {
            System.out.println(new NewShortMessageServiceImpl().getBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }

    @Test
    public void testSMS() {
        List<String> phones = new ArrayList<String>();
        phones.add("13229592218");
        String content = "测试短信";
        System.out.println("start");
        try {
          //  System.out.println(new NewShortMessageServiceImpl().sendMessage(phones, content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
