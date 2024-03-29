package edu.twt.rehuixiangshudong.zoo.filter;

import edu.twt.rehuixiangshudong.mapper.UserMapper;
import edu.twt.rehuixiangshudong.zoo.constant.JwtClaimsConstant;
import edu.twt.rehuixiangshudong.zoo.properties.JwtProperties;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import edu.twt.rehuixiangshudong.zoo.util.ThreadLocalUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebFilter(urlPatterns = "/*")
@Slf4j
public class TokenFilter implements jakarta.servlet.Filter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        //1.获取请求url
        String url = httpServletRequest.getRequestURL().toString();
        String uri = httpServletRequest.getRequestURI();//请求后缀 协议://域名"/xxx/xxx"
        log.info("收到请求 URL" + url + "来自" + httpServletRequest.getRemoteAddr());
        //2.判断是否为登录请求或注册请求或获取用户协议请求 如果是 放行
        // 检查是否是根目录的请求
        if ("/".equals(uri)) {
            // 放行对根目录的请求，不执行后续过滤器链
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        if (url.contains("login") || url.contains("register") || url.contains("userAgreement") || url.contains("images")) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //3.如果不是登录或注册 获取请求头中的token
        String jwt = httpServletRequest.getHeader("token");

        //4.判断令牌是否存在,如果不存在返回错误结果
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头中没有token!");
            Result<Object> fail = Result.fail(0,"NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(fail);
            httpServletResponse.getWriter().write(notLogin);
            return;
        }
        //5.先解析jwt 解析成功则判断是否过期：手动废除jwt：获得jwt的签发时间 并与用户最后登录时间匹配 若签发时间在登陆时间之前 则不予通过
        Map<String,Object> claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            log.info("解析jwt令牌失败！");
            Result<Object> fail = Result.fail(0,MessageConstant.NOT_LOGIN);
            String notLogin = JSONObject.toJSONString(fail);
            httpServletResponse.getWriter().write(notLogin);
            return;
        }
        //获取用户uid
        int uid = (int)claims.get(JwtClaimsConstant.USER_ID);
        //使用ThreadLocal记住当前的uid
        ThreadLocalUtil.setCurrentUid(uid);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String redisJwt = operations.get(String.valueOf(uid));
        //若redis中没有jwt 则令其失效
        if (redisJwt == null) {
            log.info("uid为 {} 用户的令牌过期失效！",uid);
            httpServletResponse.setStatus(401);
            Result<Object> fail = Result.fail(0,"NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(fail);
            httpServletResponse.getWriter().write(notLogin);
            return;
        }
/*下面是之前通过数据库操作判断令牌是否过期
//比较时间
//获取用户名 并通过mapper获取用户的最后登录时间
String username = (String) claims.get(JwtClaimsConstant.USERNAME);
Timestamp lastLoginTime = userMapper.getLastLoginTimeByUsername(username);
int uid = (int)claims.get(JwtClaimsConstant.USER_ID);
//使用ThreadLocal记住当前的uid
ThreadLocalUtil.setCurrentUid(uid);
//获取签发时间
Timestamp createTime = new Timestamp((long)claims.get(JwtClaimsConstant.CREATE_AT));
int result = createTime.compareTo(lastLoginTime);
//compareTo方法比较Timestamp类 若小于零则调用者小
if (result < 0) {
    log.info("uid为 {} 用户的令牌过期失效！",uid);
    Result<Object> fail = Result.fail(0,"NOT_LOGIN");
    String notLogin = JSONObject.toJSONString(fail);
    httpServletResponse.getWriter().write(notLogin);
    return;
        }
 */

/*前面已经做过解析，此处不再解析
//6.解析token
try {
    JwtUtil.parseJWT(jwtProperties.getSecretKey(),jwt);
} catch (Exception e) {//解析失败 返回错误结果
    log.info("解析jwt令牌失败！");
    Result<Object> fail = Result.fail(0,"NOT_LOGIN");
    String notLogin = JSONObject.toJSONString(fail);
    httpServletResponse.getWriter().write(notLogin);
    return;
        }
*/
        //7.放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("注册了过滤器...");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("过滤器关闭...");
    }
}