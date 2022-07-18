package com.example.shirotest.shiro.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.shirotest.shiro.realm.JwtToken;
import com.example.shirotest.shiro.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: iinonglqh
 * @Date: 2022/6/23 11:24
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("preHandle 方法被调用");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("isAccessAllowed 方法被调用");
        /*
        if (isLoginAttempt(request, response)){
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            }catch (Exception e){
                //token 错误
                responseError(response,e.getMessage());
            }
        }*/
        //判断请求的请求头是否带上 "Authorization"
        if (isLoginAttempt(request, response)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String token = httpServletRequest.getHeader("Authorization");
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                //执行登录，校验token
                executeLogin(request, response);
                DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);
                long expiresTime = tokenInfo.getExpiresAt().getTime();
                long currentTime = System.currentTimeMillis();
                //当过期时间-当前时间小于10分钟时，刷新token
                if (expiresTime - currentTime < JwtUtils.getRefreshTime() * 60 * 1000) {
                    Map<String, String> tokenMap = new HashMap<>();
                    String id = JwtUtils.getString(token, "id");
                    tokenMap.put("id", id);
                    //创建token
                    String newToken = JwtUtils.createToken(tokenMap);
                    httpServletResponse.setHeader("Authorization", newToken);
                } else {
                    httpServletResponse.setHeader("Authorization", token);
                }
                return true;
            } catch (Exception e) {
                //token 错误，重定向返回异常信息
                log.error(e.getMessage());
                responseError(response, e.getMessage());
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.info("isLoginAttempt 方法被调用");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }

    /**
     * executeLogin 实际上就是先调用 createToken 来获取 token ，这里我们重写了这个方法，就不会自动去调用 createToken 来获取token
     * 然后调用 getSubject 方法来获取当前用户再调用 login 方法来实现登录
     * 这也解释了我们为什么要自定义 jwtToken ，因为我们不再使用 Shiro 默认的 UsernamePasswordToken 了
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("executeLogin 方法被调用");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        JwtToken jwtToken = new JwtToken(token);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response, String message) {
        log.info("responseError 方法被调用");
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串,拼接参数到地址栏的请求方式需要URL二次编码
            message = URLEncoder.encode(message, "UTF-8");
            //重定向路径需要在 ShiroConfig 中放行
            httpServletResponse.sendRedirect("/shiro-test/unauthorized/" + URLEncoder.encode(message, "UTF-8"));
            //httpServletResponse.sendRedirect("/shiro-test/unauthorized?message=" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
