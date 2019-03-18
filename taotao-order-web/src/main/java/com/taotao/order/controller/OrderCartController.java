package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 订单处理页面
 * @author Administrator
 *
 */
@Controller
public class OrderCartController {
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request)throws Exception{
		//用户必须是登录状态
		//取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		//根据用户信息 取收货地址列表，使用的静态数据模拟
		//吧收货地址列表取出 传递给页面
		//从cookie中取购物车商品列表展示到页面
		List<TbItem> cartList = getCartItemList(request);
		//返回逻辑视图
		request.setAttribute("cartList", cartList);
		return "order-cart";
		
	}
	private List<TbItem> getCartItemList(HttpServletRequest request)throws Exception{
		//congcookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		if (StringUtils.isBlank(json)) {
			//如果没有内容，返回空的列表
			return new ArrayList<>();
			
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 生成订单
	 */
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,Model model) throws Exception{
		TaotaoResult result = orderService.createOrder(orderInfo);
		model.addAttribute("orderId", result.getData().toString());
		model.addAttribute("payment", orderInfo.getPayment());
		//预计送达时间，预计三天后送达
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		
		return "success";
	}
}
