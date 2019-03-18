package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 用户处理Controller
 * @author Administrator
 *
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type)throws Exception{
		TaotaoResult result = userService.checkData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user)throws Exception{
		TaotaoResult result = userService.register(user);
		
		return result;
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletResponse response,HttpServletRequest request) throws Exception{
		TaotaoResult result = userService.login(username, password);
		
		//登录成功后写cokkike
		if (result.getStatus()==200) {
			//把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
			
		}
		
		return result;
	}
	/*
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) throws Exception{
		TaotaoResult result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}
	*/
	//jsonp的第二种方法，spring4.1以上版本使用
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) throws Exception{
		TaotaoResult result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
