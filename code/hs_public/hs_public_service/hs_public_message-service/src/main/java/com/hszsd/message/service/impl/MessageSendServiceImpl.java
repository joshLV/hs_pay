package com.hszsd.message.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.logger.Logger;
import com.hszsd.common.util.logger.LoggerFactory;
import com.hszsd.message.dto.SendMailMessage;
import com.hszsd.message.dto.SmsEntity;
import com.hszsd.message.service.MessageSendService;
import com.hszsd.message.util.ResultMsgCode;
import com.hszsd.message.util.SmsConfig;
import com.jianzhou.sdk.BusinessService;
import org.springframework.stereotype.Service;


/**
 * @author 艾伍 贵州合石电子商务有限公司
 * @version 1.0.0
 */
@Service(value = "messageSendService")
public class MessageSendServiceImpl implements MessageSendService {

	Logger logger = LoggerFactory.getLogger(MessageSendServiceImpl.class);
	private static final String PROPERTIES_SUFFIX = ".properties";
	/**
	 * spring 邮件发送对象
	 */
	private JavaMailSenderImpl javaMailSender;
	/**
	 * 短信发送配置
	 */
	private SmsConfig smsConfig;

	/**
	 * 邮件发送者别名
	 */
	private String personal;
	
	private Properties properties;

	@Override
	public Result sendMail(SendMailMessage msg) {
		//使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		Result res = getMimeMessageHelper(msg, mailMessage);
		if (ResultMsgCode.RES_OK.equals(res.getCode())) {
			MimeMessageHelper mail = (MimeMessageHelper) res.getResult();
			try {
                //发送文本并指定类型是否是HTML
				mail.setText(msg.getText(), msg.isHtml());
				File[] files = msg.getFile();
				if (null != files && files.length > 0) {
					for (File file : files) {
						// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
						try {
                            //添加附件
							mail.addAttachment(
									MimeUtility.encodeWord(file.getName()),
									file);
						} catch (UnsupportedEncodingException e) {
							res.setCode(ResultMsgCode.ERRO_FILE);
							res.setMessage("Attachment error:" + file.getName());
							logger.trace(res.getMessage(), e);
							return res;
						}
					}
				}
				try {
					javaMailSender.send(mailMessage);
				} catch (MailException e) {
					res.setCode(ResultMsgCode.RES_NO);
					res.setMessage("send mail error:" + e.getMessage());
					logger.trace(res.getMessage(), e);
				}
			} catch (MessagingException e) {
				res.setCode(ResultMsgCode.RES_NO);
				res.setMessage("send mail error:" + e.getMessage());
				logger.trace(res.getMessage(), e);
			}
		}
		res.setResult(null);
		return res;
	}

	private Result getMimeMessageHelper(SendMailMessage msg,
			MimeMessage mailMessage) {
		Result res = new Result();
		// 设置utf-8或GBK编码，否则邮件会有乱码
		MimeMessageHelper messageHelper = null;
		try {
			messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
			if (null != msg.getTo() && msg.getTo().length > 0) {
				messageHelper.setTo(msg.getTo());// 接受者
			} else {
				res.setCode(ResultMsgCode.NULL_TO);
				res.setMessage("No recipient");
			}
            //抄送
			if (null != msg.getCc()) {
				messageHelper.setCc(msg.getCc());
			}
            //密送
			if (null != msg.getBcc()) {
				messageHelper.setBcc(msg.getBcc());
			}
			if (null != personal && !"".equals(personal)) {
				try {
                    //使用辅助类MimeMessage设定参数
					messageHelper.setFrom(javaMailSender.getUsername(),
							personal);// 发送者,这里还可以另起Email别名，不用和xml里的username一致
				} catch (UnsupportedEncodingException e) {
					res.setCode(ResultMsgCode.ERROR_ALIAS);
					res.setMessage("Alias setting error");
					logger.trace(res.getMessage(), e);
				}
			} else {
				messageHelper.setFrom(javaMailSender.getUsername());// 发送者
			}
			if (null != msg.getSubject() && !"".equals(msg.getSubject())) {
				messageHelper.setSubject(msg.getSubject());// 主题
			} else {
				res.setCode(ResultMsgCode.NULL_SUBJECT);
				res.setMessage("No theme");
			}
			res.setCode(ResultMsgCode.RES_OK);
		} catch (MessagingException e) {
			res.setCode(ResultMsgCode.ERROR_CONFIG_MAIL);
			res.setMessage("Mail configuration error");
			logger.trace(res.getMessage(), e);
		}
		res.setResult(messageHelper);
		return res;
	}



	/**
	 * 如果超过短信的长度，则分成几条发
	 *
	 * @param smsEntity
	 * @return
	 */
	@Override
	public Result sendSms(SmsEntity smsEntity) {
		Result res = new Result();
		String content = smsEntity.getContent();
		String phoneName = smsEntity.getPhoneName();
		content = StringUtils.trimToEmpty(content);
		phoneName = StringUtils.trimToEmpty(phoneName);
		if (StringUtils.isEmpty(content)) {
			res.setCode(ResultMsgCode.RES_NONULL);
			res.setMessage(properties.getProperty(this.getClass().getCanonicalName()
					+ ".SMS.code-6",null));
			return res;
		}
		if (StringUtils.isEmpty(phoneName)) {
			res.setMessage(properties.getProperty(this.getClass().getCanonicalName()
					+ ".SMS.code-7",null));
			res.setCode(ResultMsgCode.RES_NONULL);
			return res;
		}

		int maxLength=smsConfig.getMaxLength();
		// 如果超过最大长度，则分成几条发送
		int count = content.length() / maxLength;
		int reminder = content.length() % maxLength;

		if (reminder != 0) {
			count += 1;
		}
		StringBuffer sbuId = new StringBuffer();
		StringBuffer sbuMsg = new StringBuffer();
		int i = 0;
		while (count > i) {
			Result result = doSend(
					StringUtils.substring(content, i * maxLength, (i + 1)
							* maxLength), phoneName);
			sbuId.append(res.getResult());
			sbuId.append(";");
			sbuMsg.append(res.getMessage());
			sbuMsg.append(";");
			/**
			 * 只要有发送成功的都标识发送成功，失败的自己根据返回的编号进行处理
			 */
			if (ResultMsgCode.RES_OK.equals(result.getCode())) {
				res.setCode(ResultMsgCode.RES_OK);
			}
			i++;
		}
		res.setResult(sbuId.toString());
		res.setMessage(sbuMsg.toString());
		return res;
	}

	/**
	 * 短信发送
	 * @param content 短信内容
	 * @param phoneNo 手机号
	 * @return
	 */
	private Result doSend(String content, String phoneNo) {
		Result res = new Result();
		BusinessService bs = new BusinessService();
		bs.setWebService(smsConfig.getSmsUrl());//设置WebServie的地址
		/**
		 * 发送手机短信
		 */
		int reponse = bs.sendBatchMessage(smsConfig.getAccount(), smsConfig.getPassword(), phoneNo, content+smsConfig.getAutograph());
		res.setResult(reponse);
		if (reponse < 0) {
			res.setCode(ResultMsgCode.RES_NO);
			//根据编号获取对应的提示信息，没有找到时返回null
			res.setMessage(properties.getProperty(this.getClass().getCanonicalName()
					+ ".SMS.code" + reponse,null));
		} else {
			res.setCode(ResultMsgCode.RES_OK);
		}
		return res;
	}

	/**
	 * 设置配置文件路径
	 * @param properties 文件相对路径
	 */
	public void setProperties(String properties) {
        //从0开始计算该字符串存在的位置
		int isFile = properties.lastIndexOf(PROPERTIES_SUFFIX);
        //创建读取数据流对象
		InputStream inputStream = null;
		if (isFile > 0) {
            //此类所在的包下取资源
			inputStream = this.getClass().getResourceAsStream(properties);
		} else {
			String language = Locale.getDefault().toString();
			inputStream = this.getClass().getResourceAsStream(
					properties + language + PROPERTIES_SUFFIX);
		}
		try {
			Properties p = new Properties();
			p.load(inputStream);
			this.properties = p;
		} catch (IOException e) {
			e.printStackTrace();
			logger.trace(e);
		}
	}

	/**
	 * @param javaMailSender spring邮件发送对象
	 *            the javaMailSender to set
	 */
	public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
		this.javaMailSender = javaMailSender;
	}


	/**
	 * @param  smsConfig 短信发送配置 the smsConfig to set
	 */
	public void setSmsConfig(SmsConfig smsConfig) {
		this.smsConfig = smsConfig;
	}

	/**
	 * @param personal 邮件发送者别名
	 *            the personal to set
	 */
	public void setPersonal(String personal) {
		this.personal = personal;
	}

}
