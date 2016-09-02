package com.hszsd.user.dao;

import java.util.List;

import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.dto.PhoneMailDTO;
import com.hszsd.user.dto.UserDTO;
import org.springframework.stereotype.Repository;

import com.hszsd.user.dto.UserInfoDTO;
import com.hszsd.user.po.TbUser;
import com.hszsd.user.po.TbUserExample;
import com.hszsd.user.po.UserModel;


@Repository
public interface TbUserMapper extends UserMapper {

	int countByExample(TbUserExample example);

	List<TbUser> selectUserIdExample(TbUserExample example);

	List<TbUser> selectByExample(TbUserExample example);

	List<UserModel> selectUserExample(TbUserExample example);

	List<UserInfoDTO> selectUserInfoExample(TbUserExample example);

	List<GetUserInfoDTO> getUserinfoExample(TbUserExample example);

	List<UserDTO> getUserExample(TbUserExample example);

	List<PhoneMailDTO> getUserIdPhoneMail(TbUserExample example);
}