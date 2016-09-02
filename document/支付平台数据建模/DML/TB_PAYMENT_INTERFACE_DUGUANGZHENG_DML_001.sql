insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (18, '888009974300149',
   'vkaMEnYlElSu0MLakz08PO37BryMBNxBKIlTd0JVH1cQplaABex2o2ByUJw07I2e', 0,
   'https://ipos.10086.cn/ips/cmpayService',
   'http://demo.ztpuhui.com/pay/mobileFront.html',
   'http://demo.ztpuhui.com/pay/mobileBack.html', '', '中移动', 'cmpay', '', 1,
   '/upload/images/bank/pc/yidong.png', 2);
insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (19, '23234677', 'gzhszsd520', 0, 'https://pay3.chinabank.com.cn/PayGate',
   'http://demo.ztpuhui.com/pay/onLineFront.html',
   'http://demo.ztpuhui.com/pay/onLineBack.html', '', '网银在线',
   'chinabank_pay', '', 4, '/upload/images/bank/pc/wangyinzaixian.png', 1);
insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (22, '118108', '6scg3sgh8gb37smh', 0, 'https://gw.baofoo.com/payindex',
   'http://demo.ztpuhui.com/pay/baoFooFront.html',
   'http://demo.ztpuhui.com/pay/baoFooBack.html', 'utf-8', '宝付',
   'baofoo_pay', 'md5', 2, '/upload/images/bank/pc/baofu.png', 1);
insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (21, '201405211000001250', 'md5key_0851zsd_8700', 0,
   'https://yintong.com.cn/payment/bankgateway.htm',
   'http://demo.ztpuhui.com/pay/lianLianFront.html',
   'http://demo.ztpuhui.com/pay/lianLianBack.html', 'utf-8', '连连支付',
   'yintong', '', 1, '/upload/images/bank/pc/lianLianlogo.png', 0);
insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (1000, '10012471057',
   'Df50sW27uo57d35wL467sSQxscy8y9b6dg496U8HFjls6LMBko88548Q279p', 0, null,
   'http://www.zhaoshangdai.com', 'http://www.zhaoshangdai.com', 'utf-8',
   '易宝支付', 'yee_pay', 'MD5', 5,
   '/upload/image/mobile/201508141700333765486448579.png', 0);
insert into TB_PAYMENT_INTERFACE
  (INTERFACE_ID, MERCHANT_ID, MERCHANT_KEY, RECHARGE_FEE, PAY_URL,
   RETURN_URL, NOTICE_URL, CHARSET, INTERFACE_NAME, INTERFACE_VALUE,
   SIGN_TYPE, ORDER_BY, LOGO_URL, IS_ENABLE)
values
  (24, '17532', '{pU_DGAB', 0, 'https://pay.ecpss.com/sslpayment',
   'https://demo.ztpuhui.com/pay/huichaoFront.html',
   'http://demo.ztpuhui.com/pay/huichaoBack.html', 'utf-8', '汇潮支付',
   'ecpsspay', 'MD5', 1, '/upload/images/bank/pc/huichao.png', 1);
commit;
