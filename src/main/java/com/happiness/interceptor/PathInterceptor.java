package com.happiness.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.plugin.Intercepts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 日志拦截器
 * 
 * @author Administrator
 *
 */
public class PathInterceptor implements HandlerInterceptor {

    /**
     * 开启日志标志
     */
    public static final String OOLOG_ON = "on";

    public static final String DEFAULT = "default";
    /**
     * 关闭日志标志
     */
    public static final String OOLOG_OFF = "off";

    /**
     * 日志开启状态
     */
    private String status = "on";

    /**
     * 页面模块名 在Request中的 key
     */
    public static final String PAGE_SEC_NAME_REQUEST_KEY = "PAGE_SEC_NAME";

    /**
     * 模块Map
     */
    private Map<String, String> secMap;

    /**
     * 日志记录工具
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PathInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
            throws Exception {

        if (!OOLOG_ON.equals(status)) {
            return;
        }
        LOGGER.debug("End URL：" + request.getRequestURI());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object handler, ModelAndView arg3)
            throws Exception {
        if (!OOLOG_ON.equals(status)) {
            return;
        }

        long startTime = (Long) request.getAttribute("OO_REQUEST_START_TIME");

        long endTime = System.currentTimeMillis();

        long executeTime = endTime - startTime;

        LOGGER.debug("[" + handler + "] 执行时间 : " + executeTime + "ms");
        /**
         * 判断请求所处的模块
         */
        String uri = request.getRequestURI();
        boolean flg = false;
        if (uri != null) {
            for (String key : secMap.keySet()) {
                if (uri.contains(key)) {
                    flg = true;
                    request.setAttribute(PAGE_SEC_NAME_REQUEST_KEY, secMap.get(key));
                    break;
                }
            }
            if(!flg){
                request.setAttribute(PAGE_SEC_NAME_REQUEST_KEY, secMap.get(DEFAULT));
            }
        }

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        // session过期
        if (OOLOG_ON.equals(status)) {
            LOGGER.debug("Start URL：" + request.getRequestURI());
            long startTime = System.currentTimeMillis();
            request.setAttribute("OO_REQUEST_START_TIME", startTime);
        }
        return true;
    }

    /**
     * 日志状态
     * 
     * @return 日志状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置日志状态
     * 
     * @param status
     *            日志状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 模块配置
     * 
     * @return 模块配置
     */
    public Map<String, String> getSecMap() {
        return secMap;
    }

    /**
     * 模块配置
     * 
     * @param secMap
     *            模块配置
     */
    public void setSecMap(Map<String, String> secMap) {
        this.secMap = secMap;
    }

}
