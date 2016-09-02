package com.hszsd.account.service.impl;

import com.hszsd.account.dao.AccountBankMapper;
import com.hszsd.account.dao.AccountCommMapper;
import com.hszsd.account.dao.AccountQuickBankMapper;
import com.hszsd.account.dao.BankMapper;
import com.hszsd.account.dto.AccountBankCommDTO;
import com.hszsd.account.dto.AccountBankDTO;
import com.hszsd.account.dto.AccountQuickBankDTO;
import com.hszsd.account.entity.AccountCommBankExample;
import com.hszsd.account.entity.AccountQuickCommBankExample;
import com.hszsd.account.po.AccountBankPO;
import com.hszsd.account.po.AccountQuickBankPO;
import com.hszsd.account.service.AccountService;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.entity.Bank;
import com.hszsd.entity.example.AccountBankExample;
import com.hszsd.entity.example.BankExample;
import com.hszsd.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 *  用户账户信息service接口实现类
 *  @author YangWenJian
 *  @version V1.0.0
 */
@Service(value = "accountServiceImpl")
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory
            .getLogger(AccountServiceImpl.class);

    //用户开通快捷支付
    private final Integer bindStatus=1;
    //1、认证成功 -1、认证失败 0、认证中
    private final Integer validStatus=1;
    @Autowired
    private AccountCommMapper accountCommMapper;

    @Autowired
    private AccountBankMapper accountBankMapper;

    @Autowired
    private AccountQuickBankMapper accountQuickBankMapper;


    @Autowired
    private BankMapper bankMapper;


    @Autowired
    private UserService userService;
    /**
     * 获取用户绑定银行卡信息
     * @param accountBankDTO 查询信息实体
     * @return 2000 参数缺失
     *         0000 操作成功
     *         1000 操作失败
     */
    @Override
    public Result queryAccountBank(AccountBankDTO accountBankDTO) {
        logger.info("queryAccountBank accountBankDTO={}",accountBankDTO);
        Result result=new Result();
        if(accountBankDTO==null){
            logger.info("queryAccountBank accountBankDTO is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("accountBankDTO is null");
            return result;
        }
        if(StringUtils.isEmpty(accountBankDTO.getUserId())){
            logger.info("queryAccountBank accountBankDTO.getUserId is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("accountBankDTO.getUserId is null");
            return result;
        }
        AccountCommBankExample example=this.getAccountCommBankExample(accountBankDTO);
        List<AccountBankDTO> list=null;
        try {
            list=accountCommMapper.queryAccountCommBank(example);
            result.setResCode(ResultCode.RES_OK);
            result.setResult(list);
            logger.info("queryAccountBank accountCommMapper.queryAccountCommBank is success");
            return result;
        }catch (Exception e){
            logger.error("queryAccountBank accountCommMapper.queryAccountCommBank is error,message={}",e.getMessage());
            result.setResCode(ResultCode.RES_NO);
            return result;
        }
    }


    /**
     * 获取用户快捷支付银行卡关系表
     * @param accountQuickBankDTO 查询信息实体
     * @return 2000 参数缺失
     *         0000 操作成功
     *         1000 操作失败
     */
    @Override
    public Result queryAccountQuickBank(AccountQuickBankDTO accountQuickBankDTO) {
        logger.info("queryAccountQuickBank accountQuickBankDTO={}",accountQuickBankDTO);
        Result result=new Result();
        if(accountQuickBankDTO==null){
            logger.info("queryAccountQuickBank accountQuickBankDTO is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("accountQuickBankDTO is null");
            return result;
        }
        if(StringUtils.isEmpty(accountQuickBankDTO.getUserId())){
            logger.info("queryAccountQuickBank accountQuickBankDTO.getUserId is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("accountQuickBankDTO.getUserId is null");
            return result;
        }
        AccountQuickCommBankExample example=this.getAccountQuickCommBankExample(accountQuickBankDTO);
        List<AccountQuickBankDTO> list=null;
        try {
            list=accountCommMapper.queryAccountQuickCommBank(example);
            result.setResCode(ResultCode.RES_OK);
            result.setResult(list);
            logger.info("queryAccountQuickBank accountCommMapper.queryAccountQuickBank is success");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("queryAccountQuickBank accountCommMapper.queryAccountCommBank is error,message={}",e.getMessage());
            result.setResCode(ResultCode.RES_NO);
            return result;
        }
    }


    /**
     * 用户绑卡
     * @param accountBankCommDTO  用户绑卡实体信息
     * @return 2000 参数为空
     *         7004 用户已存在快捷卡
     *         0000 绑定成功
     *         5013 银行编号不存在
     */
    @Override
    public Result updateUserAccountBankComm(AccountBankCommDTO accountBankCommDTO) {
        logger.info("updateUserAccountBankComm accountBankCommDTO={}",accountBankCommDTO.toString());

        Result result=new Result();
        if(accountBankCommDTO==null){
            logger.info("updateUserAccountBankComm accountBankCommDTO is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("updateUserAccountBankComm is null");
            return result;
        }
        //用户编号
        if(StringUtils.isEmpty(accountBankCommDTO.getUserId())){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getUserId is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getUserId is null");
            return result;
        }
        //银行帐号
        if(StringUtils.isEmpty(accountBankCommDTO.getAccount())){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getAccount is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getAccount is null");
            return result;
        }

        //银行代码
        if(StringUtils.isEmpty(accountBankCommDTO.getBankCode())){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getBankCode is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getBankCode is null");
            return result;
        }
        //绑卡请求标识
        if(StringUtils.isEmpty(accountBankCommDTO.getRequestId())){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getRequestId is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getRequestId is null");
            return result;
        }
        //用户IP
        if(StringUtils.isEmpty(accountBankCommDTO.getUserIp())){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getUserIp is null");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getUserIp is null");
            return result;
        }
        //银行帐号
        if(accountBankCommDTO.getAccount().length()<6){
            logger.info("updateUserAccountBankComm accountBankCommDTO.getAccount.length is error");
            result.setResCode(ResultCode.RES_NONULL);
            result.setResMsg("getAccount.length is null");
            return result;
        }

        //根据code获取银行名称
        BankExample bankExample=new BankExample();
        BankExample.Criteria bankCriteria=bankExample.createCriteria();
        bankCriteria.andBankRelaEqualTo("["+accountBankCommDTO.getBankCode()+"]");
        List<Bank> bankPOs=bankMapper.selectByExample(bankExample);;
        if(bankPOs.size()==0){
            logger.info("updateUserAccountBankComm bankPOs is size 0");
            result.setResCode(ResultCode.BANK_CODE_NULL);
            return result;
        }
        Result resultUser=userService.getUserInfo(accountBankCommDTO.getUserId());
        if(!resultUser.getResCode().equals(ResultCode.RES_OK)){
            logger.error("updateUserAccountBankComm getUserInfo is error");
            return resultUser;
        }

        //查询用户绑定成功的
        AccountQuickBankDTO quickBankDTO=new AccountQuickBankDTO();
        quickBankDTO.setUserId(accountBankCommDTO.getUserId());
        quickBankDTO.setBindStatus(bindStatus);
        Result rs=this.queryAccountQuickBank(quickBankDTO);
        if(!rs.getResCode().equals(ResultCode.RES_OK)){
            logger.error("updateUserAccountBankComm queryAccountQuickBank is error");
            return rs;
        }
        List<AccountBankDTO> list=(List<AccountBankDTO>)rs.getResult();
        if(list.size()!=0){
            logger.info("updateUserAccountBankComm  Shortcut card already exists");
            result.setResCode(ResultCode.ACCOUNT_QUICK_CODE_YES);
            return result;
        }
        //插入快捷支付表
        AccountQuickBankPO accountQuickBankPO=new AccountQuickBankPO();
        accountQuickBankPO.setUserId(accountBankCommDTO.getUserId());
        accountQuickBankPO.setUserType(accountBankCommDTO.getUserType().getId());
        accountQuickBankPO.setBankCode(accountBankCommDTO.getBankCode());
        accountQuickBankPO.setUserIp(accountBankCommDTO.getUserIp());
        accountQuickBankPO.setRequestId(accountBankCommDTO.getRequestId());
        accountQuickBankPO.setCardTop(accountBankCommDTO.getAccount().substring(0,6));
        accountQuickBankPO.setCardLast(accountBankCommDTO.getAccount().substring(accountBankCommDTO.getAccount().length()-4,accountBankCommDTO.getAccount().length()));
        accountQuickBankPO.setBindStatus(bindStatus);
        accountQuickBankPO.setValidStatus(validStatus);
        accountQuickBankPO.setCreateTime(DateUtil.getDateLong());
        accountQuickBankPO.setCreateBy(accountBankCommDTO.getUserId());
        accountQuickBankPO.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        accountQuickBankMapper.insert(accountQuickBankPO);

        //删除用户的提现银行卡
        AccountBankExample example=new AccountBankExample();
        AccountBankExample.Criteria criteria=example.createCriteria();
        criteria.andUserIdEqualTo(accountBankCommDTO.getUserId());
        accountBankMapper.deleteByExample(example);

        //插入提现表
        AccountBankPO accountBankPO=new AccountBankPO();
        accountBankPO.setUserId(accountBankCommDTO.getUserId());
        accountBankPO.setAccount(accountBankCommDTO.getAccount());
        accountBankPO.setBrank(bankPOs.get(0).getName());
        accountBankPO.setBranch(accountBankCommDTO.getBranch());
        accountBankPO.setProvince("0");
        accountBankPO.setAddtime(DateUtil.getDateLong());
        accountBankPO.setArea("0");
        accountBankPO.setQu("0");
        accountBankPO.setRequestId(accountBankCommDTO.getRequestId());
        accountBankMapper.insert(accountBankPO);
        logger.info("updateUserAccountBankComm is success");
        result.setResCode(ResultCode.RES_OK);
        return result;
    }

    private AccountCommBankExample getAccountCommBankExample(AccountBankDTO accountBank){
        AccountCommBankExample example=new AccountCommBankExample();
        AccountCommBankExample.Criteria criteria=example.createCriteria();
        //判断传入是否传入用户ID
        if(accountBank.getId()!=null){
            criteria.andIdEqualTo(accountBank.getId());
        }
        //判断用户ID是否为空
        if(StringUtils.isNotEmpty(accountBank.getUserId())){
            criteria.andUserIdEqualTo(accountBank.getUserId());
        }

        //判断用户银行帐号是否为空
        if(StringUtils.isNotEmpty(accountBank.getAccount())){
            criteria.andAccountEqualTo(accountBank.getAccount());
        }

        //判断用户银行名称是否为空
        if(StringUtils.isNotEmpty(accountBank.getBrank())){
            criteria.andBrankEqualTo(accountBank.getBrank());
        }

        //判断用户开户行是否为空
        if(StringUtils.isNotEmpty(accountBank.getBranch())){
            criteria.andBranchEqualTo(accountBank.getBranch());
        }

        //判断省是否为空
        if(StringUtils.isNotEmpty(accountBank.getProvince())){
            criteria.andProvinceEqualTo(accountBank.getProvince());
        }

        //判断市是否为空
        if(StringUtils.isNotEmpty(accountBank.getArea())){
            criteria.andAreaEqualTo(accountBank.getArea());
        }

        //判断区是否为空
        if(StringUtils.isNotEmpty(accountBank.getQu())){
            criteria.andQuEqualTo(accountBank.getQu());
        }
        //判断添加时间是否为空
        if(accountBank.getAddtime()!=null){
            criteria.andAddtimeEqualTo(accountBank.getAddtime());
        }

        //判断快捷支付标识是否为空
        if(accountBank.getRequestId()!=null){
            criteria.andRequestIdEqualTo(accountBank.getRequestId());
        }
        example.setOrderByClause("T.ID DESC");
        return example;
    }

    private AccountQuickCommBankExample getAccountQuickCommBankExample(AccountQuickBankDTO accountQuickBankDTO){
        AccountQuickCommBankExample example=new AccountQuickCommBankExample();
        AccountQuickCommBankExample.Criteria criteria=example.createCriteria();
        //主键编号
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getId())){
            criteria.andIdEqualTo(accountQuickBankDTO.getId());
        }

        //用户编号
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getUserId())){
            criteria.andUserIdEqualTo(accountQuickBankDTO.getUserId());
        }
        //用户类型
        if(accountQuickBankDTO.getUserType()!=null){
            criteria.andUserTypeEqualTo(accountQuickBankDTO.getUserType());
        }
        //绑卡请求标识
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getRequestId())){
            criteria.andRequestIdEqualTo(accountQuickBankDTO.getRequestId());
        }

        //用户IP
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getUserIp())){
            criteria.andUserIpEqualTo(accountQuickBankDTO.getUserIp());
        }

        //卡号前6位
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getCardTop())){
            criteria.andCardTopEqualTo(accountQuickBankDTO.getCardTop());
        }
        //卡号后4位
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getCardLast())){
            criteria.andCardLastEqualTo(accountQuickBankDTO.getCardLast());
        }

        //银行编号
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getBankCode())){
            criteria.andBankCodeEqualTo(accountQuickBankDTO.getBankCode());
        }
        //绑定状态 1、绑定成功  -1、解绑
        if(accountQuickBankDTO.getBindStatus()!=null){
            criteria.andBindStatusEqualTo(accountQuickBankDTO.getBindStatus());
        }
        //认证状态 1、认证成功 -1、认证失败 0、认证中
        if(accountQuickBankDTO.getValidStatus()!=null){
            criteria.andValidStatusEqualTo(accountQuickBankDTO.getValidStatus());
        }
        //创建时间
        if(accountQuickBankDTO.getCreateTime()!=null){
            criteria.andCreateTimeEqualTo(accountQuickBankDTO.getCreateTime());
        }
        //创建人
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getCreateBy())){
            criteria.andCreateByEqualTo(accountQuickBankDTO.getCreateBy());
        }

        //修改时间
        if(accountQuickBankDTO.getModifyTime()!=null){
            criteria.andModifyTimeEqualTo(accountQuickBankDTO.getModifyTime());
        }


        //修改人
        if(StringUtils.isNotEmpty(accountQuickBankDTO.getModifyBy())){
            criteria.andModifyByEqualTo(accountQuickBankDTO.getModifyBy());
        }
        example.setOrderByClause("Z.ID DESC");
        return example;
    }

}
