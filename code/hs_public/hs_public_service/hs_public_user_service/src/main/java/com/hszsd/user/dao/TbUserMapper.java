package com.hszsd.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hszsd.dao.UserMapper;
import com.hszsd.user.dto.UserInfo;
import com.hszsd.user.po.TbUser;
import com.hszsd.user.po.TbUserExample;
import com.hszsd.user.po.UserModel;


@Repository
public interface TbUserMapper extends UserMapper{

	int countByExample(TbUserExample example);

	List<TbUser> selectUserIdExample(TbUserExample example);

	List<TbUser> selectByExample(TbUserExample example);

	List<UserModel> selectUserExample(TbUserExample example);

	List<UserInfo> selectUserInfoExample(TbUserExample example);

	TbUser selectByPrimaryKey(String userId);

	
}