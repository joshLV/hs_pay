package com.hszsd.webpay.service.impl;

import com.hszsd.account.dto.AccountBankCommDTO;
import com.hszsd.account.dto.UserType;
import com.hszsd.account.service.AccountService;
import com.hszsd.common.util.Result;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.AccountBankCondition;
import com.hszsd.webpay.condition.AccountQuickBankCondition;
import com.hszsd.webpay.condition.BankInfoCondition;
import com.hszsd.webpay.dao.AccountBankDao;
import com.hszsd.webpay.dao.AccountQuickBankDao;
import com.hszsd.webpay.dao.BankInfoDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.AccountBankPO;
import com.hszsd.webpay.po.AccountQuickBankPO;
import com.hszsd.webpay.service.CommonService;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.service.YeePayService;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.UUIDUtils;
import com.hszsd.webpay.util.YeePayUtil;
import com.hszsd.webpay.web.dto.AccountBankDTO;
import com.hszsd.webpay.web.dto.AccountQuickBankDTO;
import com.hszsd.webpay.web.dto.BankInfoDTO;
import com.hszsd.webpay.web.dto.QuickBankOutDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.BindCardConfirmForm;
import com.hszsd.webpay.web.form.BindCardForm;
import com.hszsd.webpay.web.form.BindCardPayForm;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 易宝快捷支付业务层接口实现
 * Created by suocy on 2016/7/15.
 */
@Service("yeePayService")
@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
public class YeePayServiceImpl implements YeePayService {

    private static final Logger logger = LoggerFactory.getLogger(YeePayServiceImpl.class);

    @Autowired
    private TradeRecordDao tradeRecordDao;
    @Autowired
    private AccountQuickBankDao accountQuickBankDao;
    @Autowired
    private AccountBankDao accountBankDao;
    @Autowired
    private BankInfoDao bankInfoDao;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommonService commonService;


    private StringDao stringDao = RedisBaseDao.getStringDao();


    /**
     * 绑卡请求
     * @param bindCardForm 绑卡表单对象 requestId不为空时，请求短信验证码重发
     * @param userInfoDTO 用户信息对象
     * @return
     */
    @Override
    public ResultInfo bindCard(BindCardForm bindCardForm,GetUserInfoDTO userInfoDTO) {
        ResultInfo resultInfo = new ResultInfo();
        String userId = userInfoDTO.getUserId();
        String cardNo = bindCardForm.getCardNo();
        //每个用户只能绑定一张快捷卡
        ResultInfo queryResult = queryQuickBank(userId);
        if(!queryResult.getIsSuccess()){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        List<QuickBankOutDTO> quickBankOutDTOs = (List<QuickBankOutDTO>)queryResult.getParams();
        if (quickBankOutDTOs != null&&quickBankOutDTOs.size() > 0){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.QUICKBANK_ISEXIST);
            return resultInfo;
        }
        //生成绑卡请求号requestId
        String requestId;
        if(StringUtils.isEmpty(bindCardForm.getRequestId())){
            requestId = UUIDUtils.getUUID(false);
        }else{
            requestId = bindCardForm.getRequestId();
        }
        //封装调用第三方接口参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("requestid", requestId);
        params.put("identityid", userId);
        params.put("cardno", cardNo);
        params.put("idcardno", userInfoDTO.getCardId());
        params.put("username", userInfoDTO.getRealName());
        params.put("phone", bindCardForm.getPhone());
        params.put("userip", bindCardForm.getUserIp());
        //发送绑卡请求，接收返回参数
        Map<String, String> bindBankcard = YeePayUtil.bindBankcard(params);

        logger.info("TransId:{}----->bindCard back params ： {} ",bindCardForm.getTransId(),bindBankcard);
        String merchantAccount = formatString(bindBankcard.get("merchantaccount"));
        String errorCode = formatString(bindBankcard.get("error_code"));
        String errorMsg = formatString(bindBankcard.get("error_msg"));
        String customError = formatString(bindBankcard.get("customError"));

        if(!"".equals(errorCode)) {
            logger.error("YeePay have an error occurred , error_code: {} error_msg: {} ",errorCode,errorMsg);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(!"".equals(customError)) {
            logger.error("BindCard have an error occurred by : {} ",customError);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(StringUtils.isEmpty(merchantAccount)){
            logger.error("bindCard have an error occurred by merchantAccount is null ");
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        if(StringUtils.isEmpty(bindCardForm.getRequestId())){
            //封装绑卡信息且保存
            AccountQuickBankPO quickBank = new AccountQuickBankPO();
            quickBank.setId(requestId);
            quickBank.setUserType(GlobalConstants.ACCOUNT_QUICK_BANK.USERTYPE_2);
            quickBank.setUserId(userId);
            quickBank.setRequestId(requestId);
            quickBank.setUserIp(bindCardForm.getUserIp());
            //银行卡前6位
            quickBank.setCardTop(cardNo.substring(0,6));
            //银行卡后4位
            quickBank.setCardLast(cardNo.substring(cardNo.length() - 4, cardNo.length()));
            quickBank.setBindStatus(GlobalConstants.ACCOUNT_QUICK_BANK.BINDSTATUS_1); //已绑定
            quickBank.setValidStatus(GlobalConstants.ACCOUNT_QUICK_BANK.VALIDSTATUS_2); //未认证
            quickBank.setCreateDate(new Date());
            quickBank.setCreateBy(userId);
            accountQuickBankDao.insert(quickBank);

            //将用户绑定的银行卡号放入redis，供确认绑卡接口使用
            //单位秒, 失效时间为1天 86400000/24/3600/1000 = 24
            stringDao.save(GlobalConstants.COMMON.REDIS_CARDNO_PREFIX + requestId,cardNo,86400000);
        }

        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(requestId);
        return resultInfo;
    }

    /**
     * 确认绑卡请求
     * @param bindCardConfirmForm 确定绑卡表单对象
     * @param userInfoDTO 用户信息对象
     * @return
     */
    @Override
    public ResultInfo bindCardConfirm(BindCardConfirmForm bindCardConfirmForm, GetUserInfoDTO userInfoDTO) {
        ResultInfo resultInfo = new ResultInfo();
        //封装调用第三方接口参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("requestid", bindCardConfirmForm.getRequestId());
        params.put("validatecode", bindCardConfirmForm.getSmsCode());

        //发送绑卡请求，接收返回参数
        Map<String, String> result = YeePayUtil.confirmBindBankcard(params);
        logger.info("TransId:{}----->bindCardConfirm back params ： {} ",bindCardConfirmForm.getTransId(),result);
        String merchantAccount = formatString(result.get("merchantaccount"));
        String requestId = formatString(result.get("requestid"));
        String bankCode = formatString(result.get("bankcode"));
        String cardTop = formatString(result.get("card_top"));
        String cardLast = formatString(result.get("card_last"));
        String errorCode = formatString(result.get("error_code"));
        String errorMsg = formatString(result.get("error_msg"));
        String customError = formatString(result.get("customError"));

        if(!"".equals(errorCode)) {
            logger.error("YeePay have an error occurred , error_code: {} error_msg: {} ",errorCode,errorMsg);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(!"".equals(customError)) {
            logger.error("bindCardConfirm have an error occurred by : {} ",customError);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(StringUtils.isEmpty(merchantAccount)){
            logger.error("bindCardConfirm have an error occurred by merchantAccount is null ");
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;

        }
        try{
            //保存或更新AccountBank----------------------------开始---------------------------------------------
            //根据绑定的bankCode获取银行信息
            BankInfoCondition bankInfoCondition = new BankInfoCondition();
            bankInfoCondition.or().andBankValueEqualTo(bankCode);
            List<BankInfoDTO> bankInfoDTOs = bankInfoDao.selectByCondition(bankInfoCondition);
            if(null == bankInfoDTOs){
                logger.error("Table tb_online_bank no have this bankCode :{} ", bankCode);
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
                return resultInfo;
            }
            //获取银行卡号
            String cardNo = stringDao.getValue(GlobalConstants.COMMON.REDIS_CARDNO_PREFIX + bindCardConfirmForm.getRequestId());
            if(StringUtils.isEmpty(cardNo)){
                logger.error("bindCardConfirm have an error occurred by cardNo is null ");
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
                return resultInfo;
            }

            //根据银行卡号获取绑卡信息
            AccountBankCondition accountBankCondition = new AccountBankCondition();
            accountBankCondition.or().andAccountEqualTo(cardNo);
            List<AccountBankDTO> accountBankDTOs = accountBankDao.selectByCondition(accountBankCondition);
            //保存提现银行卡信息
            if(null == accountBankDTOs || accountBankDTOs.size() < 1 ){
                AccountBankPO accountBankPo = new AccountBankPO();
                accountBankPo.setId(UUIDUtils.getId(10,10));//24位
                accountBankPo.setUserId(userInfoDTO.getUserId());
                accountBankPo.setAccount(cardNo);
                accountBankPo.setBank(bankInfoDTOs.get(0).getBankName());
                accountBankPo.setRequestId(requestId);
                accountBankPo.setCreateDate(new Date());
                accountBankPo.setCreateBy(userInfoDTO.getUserId());
                accountBankDao.insert(accountBankPo);
            }else {//更新银行卡信息
                AccountBankPO accountBankPO = new AccountBankPO();
                accountBankPO.setId(accountBankDTOs.get(0).getId());
                accountBankPO.setBank(bankInfoDTOs.get(0).getBankName());
                accountBankPO.setRequestId(requestId);
                accountBankDao.updateByPrimaryKeySelective(accountBankPO);
            }
            //保存或更新AccountBank----------------------------结束---------------------------------------------

            //更新AccountQuickBank快捷绑卡信息------------------开始---------------------------------------------
            AccountQuickBankPO accountQuickBankPO = new AccountQuickBankPO();
            accountQuickBankPO.setId(bindCardConfirmForm.getRequestId());
            accountQuickBankPO.setBankCode(bankCode);
            accountQuickBankPO.setCardTop(cardTop);
            accountQuickBankPO.setCardLast(cardLast);
            accountQuickBankPO.setValidStatus(GlobalConstants.ACCOUNT_QUICK_BANK.VALIDSTATUS_1);//已认证
            accountQuickBankPO.setUpdateBy(userInfoDTO.getUserId());
            accountQuickBankPO.setUpdateDate(new Date());
            int count = accountQuickBankDao.updateByPrimaryKeySelective(accountQuickBankPO);
            if (count==0){
                throw new RuntimeException("bindCardConfirm have an error occurred by accountQuickBank updateByPrimaryKeySelective is failed");
            }
            //更新AccountQuickBank快捷绑卡信息------------------结束---------------------------------------------
            //同卡同出
            bindCardSuccess(bindCardConfirmForm);
            //dubbox数据保存
            AccountBankCommDTO accountBankCommDTO = new AccountBankCommDTO();
            accountBankCommDTO.setUserId(userInfoDTO.getUserId());
            accountBankCommDTO.setAccount(cardNo);
            accountBankCommDTO.setBankCode(bankCode);
            accountBankCommDTO.setRequestId(bindCardConfirmForm.getRequestId());
            accountBankCommDTO.setUserIp(bindCardConfirmForm.getUserIp());
            accountBankCommDTO.setUserType(UserType.USERID);
            logger.info("accountService.updateUserAccountBankComm params :{} ", JsonUtil.obj2json(accountBankCommDTO));
            Result updateResult = accountService.updateUserAccountBankComm(accountBankCommDTO);

            if(!ResultConstants.OPERATOR_SUCCESS.getCode().equals(updateResult.getResCode())){
                logger.error("bindCardConfirm have an error occurred by accountService.updateUserAccountBankComm is failed and resCode :"+updateResult.getResCode());
            }
            resultInfo.setIsSuccess(true);
            resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
            return resultInfo;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("确认绑卡成功后，操作数据库失败！userId ："+bindCardConfirmForm.getUserId()+",卡号后四位 ：" + cardLast);
        }
    }

    /**
     * 查询用户绑定的快捷支付卡
     * @param userId
     * @return
     */
    @Override
    public ResultInfo queryQuickBank(String userId) {
        ResultInfo resultInfo = new ResultInfo();
        //查询用户共有多少张快捷卡
        List<QuickBankOutDTO> quickBankOuts = new ArrayList<QuickBankOutDTO>();
        //先查本地库，如果没有快捷卡，在查dubbox
        AccountQuickBankCondition condition = new AccountQuickBankCondition();
        condition.or().andUserIdEqualTo(userId)
                .andBindStatusEqualTo(GlobalConstants.ACCOUNT_QUICK_BANK.BINDSTATUS_1)
                .andValidStatusEqualTo(GlobalConstants.ACCOUNT_QUICK_BANK.VALIDSTATUS_1);
        condition.setOrderByClause("create_date desc");
        List<AccountQuickBankDTO> acountQuickBankDTOs = accountQuickBankDao.selectByCondition(condition);
        if(acountQuickBankDTOs != null && acountQuickBankDTOs.size()>0){
            for(AccountQuickBankDTO acountQuickBankDTO : acountQuickBankDTOs){
                QuickBankOutDTO quickBankOut = new QuickBankOutDTO();
                quickBankOut.setId(acountQuickBankDTO.getId());
                quickBankOut.setCardTop(acountQuickBankDTO.getCardTop());
                quickBankOut.setCardLast(acountQuickBankDTO.getCardLast());
                //根据绑定的bankCode获取银行信息
                BankInfoCondition bankInfoCondition = new BankInfoCondition();
                bankInfoCondition.or().andBankValueEqualTo(acountQuickBankDTO.getBankCode());
                List<BankInfoDTO> bankInfoDTOs = bankInfoDao.selectByCondition(bankInfoCondition);
                if(null == bankInfoDTOs){
                    logger.error("Table tb_online_bank no have this bankcode :{} ", acountQuickBankDTO.getBankCode());
                    continue;
                }
                quickBankOut.setBankName(bankInfoDTOs.get(0).getBankName());
                quickBankOut.setLogo(bankInfoDTOs.get(0).getBankLogoMob());
                quickBankOuts.add(quickBankOut);
            }
            resultInfo.setIsSuccess(true);
            resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
            resultInfo.setParams(quickBankOuts);
            return resultInfo;
        }
        com.hszsd.account.dto.AccountQuickBankDTO dubboxAccountQuickBankDTO = new com.hszsd.account.dto.AccountQuickBankDTO();
        dubboxAccountQuickBankDTO.setUserId(userId);
        dubboxAccountQuickBankDTO.setBindStatus(1);
        dubboxAccountQuickBankDTO.setValidStatus(1);
        logger.info("accountService.queryAccountQuickBank params: {}",JsonUtil.obj2json(dubboxAccountQuickBankDTO));
        Result result;
        try{
            result = accountService.queryAccountQuickBank(dubboxAccountQuickBankDTO);
            logger.info("accountService.queryAccountQuickBank resCode: {}",result.getResCode());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("accountService.queryAccountQuickBank resCode: {}",e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        if(!ResultConstants.OPERATOR_SUCCESS.getCode().equals(result.getResCode())){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        List<com.hszsd.account.dto.AccountQuickBankDTO> dubboxAccountQuickBankDTOs = (List<com.hszsd.account.dto.AccountQuickBankDTO>)result.getResult();
        for(com.hszsd.account.dto.AccountQuickBankDTO acountQuickBankDTO : dubboxAccountQuickBankDTOs){
            QuickBankOutDTO quickBankOut = new QuickBankOutDTO();
            quickBankOut.setId(acountQuickBankDTO.getRequestId());
            quickBankOut.setCardTop(acountQuickBankDTO.getCardTop());
            quickBankOut.setCardLast(acountQuickBankDTO.getCardLast());
            //根据绑定的bankCode获取银行信息
            BankInfoCondition bankInfoCondition = new BankInfoCondition();
            bankInfoCondition.or().andBankValueEqualTo(acountQuickBankDTO.getBankCode());
            List<BankInfoDTO> bankInfoDTOs = bankInfoDao.selectByCondition(bankInfoCondition);
            if(null == bankInfoDTOs){
                logger.error("Table tb_online_bank no have this bankCode :{} ", acountQuickBankDTO.getBankCode());
                continue;
            }
            quickBankOut.setBankName(bankInfoDTOs.get(0).getBankName());
            quickBankOut.setLogo(bankInfoDTOs.get(0).getBankLogoMob());
            quickBankOuts.add(quickBankOut);

        }
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(quickBankOuts);
        return resultInfo;
    }

    /**
     * 快捷卡支付--不需要短信验证
     * @param bindCardPayForm
     * @param backUrl  支付回调地址
     * @return
     */
    @Override
    public ResultInfo quickCardPay(BindCardPayForm bindCardPayForm, String backUrl) {
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(bindCardPayForm.getTransId());
        //封装调用第三方接口参数
        Map<String, String> map = new HashMap<String, String>();
        //封装接口参数
        map.put("orderid", tradeRecordDTO.getTransId());
        map.put("amount", String.valueOf(tradeRecordDTO.getMoney().multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)));
        map.put("productname", tradeRecordDTO.getProductName());
        map.put("identityid", tradeRecordDTO.getUserId());
        map.put("card_top", bindCardPayForm.getCardTop());
        map.put("card_last", bindCardPayForm.getCardLast());
        map.put("callbackurl", backUrl);
        map.put("userip", bindCardPayForm.getUserIp());
        //发送支付请求
        Map<String, String> result = YeePayUtil.directBindPay(map);
        logger.info("TransId:{}----->quickCardPay back params ： {} ",bindCardPayForm.getTransId(),result);
        String merchantAccount = formatString(result.get("merchantaccount"));
        String errorCode = formatString(result.get("error_code"));
        String errorMsg = formatString(result.get("error_msg"));
        String customError = formatString(result.get("customError"));
        if(!"".equals(errorCode)) {
            logger.error("YeePay have an error occurred , error_code: {} error_msg: {} ",errorCode,errorMsg);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(!"".equals(customError)) {
            logger.error("quickCardPay have an error occurred by : {} ",customError);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        else if(StringUtils.isEmpty(merchantAccount)){
            logger.error("quickCardPay have an error occurred by merchantAccount is null ");
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;

        }
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 快捷支付回调
     * @param data
     * @param encryptKey
     * @return 处理是否成功
     */
    @Override
    public boolean callBack(String data, String encryptKey) {
        logger.info("YeePayServiceImpl-->>callBack-->> 开始........");
        Map<String, String>	result	= YeePayUtil.decryptCallbackData(data, encryptKey);
        String orderId  = result.get("orderid");
        String status = result.get("status");
        String errorCode = result.get("errorcode");
        String errorMsg = result.get("errormsg");
        String customError = result.get("customError");
        logger.info("YeePayServiceImpl-->>callBack.do-->>back params： " + result);
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(orderId);
        try {
            logger.info("YeePayServiceImpl-->>callBack-->>tradeRecord: " + tradeRecordDTO);
            //判断是否充值成功
            if("1".equals(status)){
                if(tradeRecordDTO.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_1){
                    paymentService.editTradeStatusAndCallBack(orderId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_2,GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    logger.info("YeePayServiceImpl-->>callBack-->>交易成功！");
                    boolean coreResult = false;
                    int orderType = tradeRecordDTO.getOrderType();
                    //根据orderType判断调用接口
                    if(orderType == GlobalConstants.TRADE_RECORD.ORDER_TYPE_ZTWY){
                        coreResult = paymentService.optCoreAccountZTWY(tradeRecordDTO, GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    }
                    else if(orderType == GlobalConstants.TRADE_RECORD.ORDER_TYPE_ZTSC){
                        coreResult = paymentService.optCoreAccountZTSC(tradeRecordDTO, GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    }else{
                        logger.error("YeePayServiceImpl-->>callBack:未知的订单类型，orderType: {}",orderType );
                    }
                    if(!coreResult){
                        logger.error("YeePayServiceImpl-->>callBack:功能服务器接口调用失败，transId: {} ,orderType: {}",orderId,orderType );
                    }
                }
                return true;
            }
            else if ("2".equals(status)||"3".equals(status)){
                if(tradeRecordDTO.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_1){
                    paymentService.editTradeStatusAndCallBack(orderId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    logger.info("YeePayServiceImpl-->>callBack-->>交易失败！");
                }
                return true;
            }else{
                if(!"".equals(errorCode)){
                    paymentService.editTradeStatusAndCallBack(orderId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    logger.info("YeePayServiceImpl-->>callBack-->>交易失败！-->>errorCode={}, errorMsg={}",errorCode,errorMsg);
                    return true;
                }
                else if (!"".equals(customError)){
                    paymentService.editTradeStatusAndCallBack(orderId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,GlobalConstants.TRADE_RECORD.QUICK_PAY);
                    logger.info("YeePayServiceImpl-->>callBack-->>交易失败！-->>{}",customError);
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("YeePayServiceImpl-->>callBack-->>transId: " + orderId + "Exception: " + e.getMessage());
            return false;
        }

        logger.info("YeePayServiceImpl-->>callBack-->>未成功处理的支付回调：" + result);
        return false;
    }


    /**
     * 绑卡成功后，对快捷卡和提现卡进行”同进同出“操作
     * @param bindCardConfirmForm
     */
    private void bindCardSuccess(BindCardConfirmForm bindCardConfirmForm){
        logger.info("YeePayServiceImpl-->>bindCardSuccess-->>确定绑卡成功-->>开始");
        AccountQuickBankDTO accountQuickBankDTO = accountQuickBankDao.selectByPrimaryKey(bindCardConfirmForm.getRequestId());

        AccountBankCondition condition = new AccountBankCondition();
        condition.or().andUserIdEqualTo(accountQuickBankDTO.getUserId())
                .andRequestIdNotEqualTo(accountQuickBankDTO.getRequestId());
        accountBankDao.deleteByCondition(condition);

        logger.info("YeePayServiceImpl-->>bindCardSuccess-->>确定绑卡成功-->>结束");
    }


    /**
     * 格式化字符串
     */
    private static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }
}
