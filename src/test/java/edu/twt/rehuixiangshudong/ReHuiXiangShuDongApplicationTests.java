package edu.twt.rehuixiangshudong;

import com.alibaba.fastjson2.JSONObject;
import edu.twt.rehuixiangshudong.mapper.UserMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import static org.apache.http.impl.client.HttpClients.createDefault;

@SpringBootTest
class ReHuiXiangShuDongApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void httpGetClientTest() throws IOException {
        //1.创建HttpClient对象
        CloseableHttpClient httpClient = createDefault();
        //2.创建连接对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/userAgreement");
        //3.调用execute发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //4.获取返回的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.printf("服务器返回的状态码是：" + statusCode);
        //5.获取返回的数据 使用工具类解析
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.printf("服务器返回的数据是：" + body);
        //6.关闭资源
        response.close();
        httpClient.close();
    }

    @Test
    public void httpPostClientTest() throws IOException {
        CloseableHttpClient httpClient = createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/login");

        //POST传递参数：阿里 fastJSON + StringEntity + setEntity
        //创建json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "xxx");
        jsonObject.put("password", "xxx");
        //设置传输参数
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        httpPost.setEntity(stringEntity);
        //指定编码方式
        stringEntity.setContentEncoding("utf-8");
        //数据格式
        stringEntity.setContentType("application/json");
        //封装并发送
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //返回数据处理
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.printf("返回数据" + body);
        //关闭资源
        response.close();
        httpClient.close();
    }

}
