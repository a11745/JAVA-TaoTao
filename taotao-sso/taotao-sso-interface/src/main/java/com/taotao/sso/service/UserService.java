package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
	TaotaoResult checkData(String data,int type) throws Exception;
	TaotaoResult register(TbUser user)throws Exception;
	TaotaoResult login(String username,String password) throws Exception;
	TaotaoResult getUserByToken(String token) throws Exception;
}
