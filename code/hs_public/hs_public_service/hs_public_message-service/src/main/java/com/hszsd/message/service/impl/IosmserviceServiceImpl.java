package com.hszsd.message.service.impl;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.message.dto.SmsEntity;
import com.hszsd.message.service.IosmserviceService;
import com.hszsd.message.util.SmsConfig;

@Service(value="iosmserviceServiceImpl")
public class IosmserviceServiceImpl implements IosmserviceService {
	
	private SmsConfig smsConfig;
	private String properties;
	@Override
	public Result SendSMS(SmsEntity smsEntity) {
		MessageSendServiceImpl messageSendServiceImpl=new MessageSendServiceImpl();
		Result res=new Result();
		if(StringUtils.isEmpty(smsEntity)){
			res.setResCode(ResultCode.RES_NONULL);
			return res;
		}
		if(StringUtils.isEmpty(smsEntity.getContent())||StringUtils.isEmpty(smsEntity.getPhoneName())){
			res.setResCode(ResultCode.RES_NONULL);
			return res;
		}
		
		String str= invokeBlocking("SendSMS",new Object[] {smsConfig.getAccount(),smsConfig.getPassword(), smsEntity.getPhoneName(), smsEntity.getContent()+smsConfig.getAutograph(), "MSG"});
        if("0".equals(str)){
        	res.setResCode(ResultCode.RES_OK);
        }else{
        	res.setResCode(ResultCode.RES_NO);
        }
        messageSendServiceImpl.setProperties(properties);
        String resMsg=messageSendServiceImpl.getProperties().getProperty(this.getClass().getCanonicalName()
				+ ".SMS.code-"+str,null);
    	res.setResMsg(resMsg);
		return res;
	}

	public void setProperties(String properties) {
		this.properties=properties;
	}
	public  String invokeBlocking(
			String method, Object[] opAddEntryArgs) {

		String str = null;
		RPCServiceClient serviceClient;
		try {
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			// OptionswebServiceModel.
			EndpointReference targetEPR = new EndpointReference(smsConfig.getSmsUrl());
			options.setTo(targetEPR);
			// 在创建QName对象时，QName类的构造方法的第一个参数表示WSDL文件的命名空间名，也就是<wsdl:definitions>元素的targetNamespace属性值
			QName opAddEntry = new QName("http://services.webservices.com/", method);
			/*if (null != webServiceModel.getSoapAction()
					&& !"".equals(webServiceModel.getSoapAction())) {
				options.setAction(webServiceModel.getSoapAction());
			}*/
			// options.setUseSeparateListener(true);

			// 参数，如果有多个，继续往后面增加即可，不用指定参数的名称
			// 返回参数类型，这个和axis1有点区别
			// invokeBlocking方法有三个参数，其中第一个参数的类型是QName对象，表示要调用的方法名；
			// 第二个参数表示要调用的WebService方法的参数值，参数类型为Object[]；
			// 第三个参数表示WebService方法的返回值类型的Class对象，参数类型为Class[]。
			// 当方法没有参数时，invokeBlocking方法的第二个参数值不能是null，而要使用new Object[]{}
			// 如果被调用的WebService方法没有返回值，应使用RPCServiceClient类的invokeRobust方法，
			// 该方法只有两个参数，它们的含义与invokeBlocking方法的前两个参数的含义相同
			Class[] classes = new Class[] { String.class };
			str = (String) serviceClient.invokeBlocking(opAddEntry,
					opAddEntryArgs, classes)[0];
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return str;
	}
	public SmsConfig getSmsConfig() {
		return smsConfig;
	}
	public void setSmsConfig(SmsConfig smsConfig) {
		this.smsConfig = smsConfig;
	}

	public static void main(String[] args) {
		SmsEntity smsEntity=null;
		//smsEntity.setContent("sasa");
		System.out.println(StringUtils.isEmpty(smsEntity));
	}
}
