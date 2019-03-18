package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

public class LoginInterceptor implements HandlerExceptionResolver, HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		//modelAndView返回之后，异常处理

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//handler执行之后 modelAndView返回之前

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//执行handler之前先执行此方法
		//1.从cookie中取token信息
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		
		//2.如果娶不到token，跳转到sso的登录页面，需要把当前请求的url作为参数传递给sso，sso登陆成功后跳转回此页面
		if (StringUtils.isBlank(token)) {
			//区当前的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面
			
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//response.sendRedirect("http://localhost:8090"+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		//取到token，调用sso系统的服务判断用户是否登录
		TaotaoResult result = userService.getUserByToken(token);
		//如果用户未登录，既没取到用户信息，，跳转到sso的登录页面 ...
		if (result.getStatus()!=200) {
			//区当前的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面
			
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//response.sendRedirect("http://localhost:8090"+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		//如果取到用户信息，放行
		//吧用户信息防盗request
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		
		//返回值 true放行 返回false 拦截
		return true;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
