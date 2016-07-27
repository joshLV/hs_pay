package com.hszsd.message.dto;

import java.io.File;
import java.io.Serializable;

/**
 * 邮件发送实体类
 * @author 艾伍
 *	贵州合石电子商务有限公司
 * @version 1.0.0
 */
public class SendMailMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060663154483104473L;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件接收人
	 */
	private String[] to;

	/**
	 * 邮件内容
	 */
	private String text;
	/**
	 * 抄送
	 */
	private String[] cc;

	/**
	 * 暗抄送
	 */
	private String bcc;

	/**
	 * 是否Html
	 */
	private boolean html;
	/**
	 * 附件
	 */
	private File[] file;

	/**
	 * @return 邮件主题
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param 邮件主题
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return 邮件接收人
	 */
	public String[] getTo() {
		return to;
	}

	/**
	 * @param 邮件接收人
	 *            the to 邮件接收人 set
	 */
	public void setTo(String[] to) {
		this.to = to;
	}

	/**
	 * @return 邮件内容
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param 邮件内容
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return 抄送
	 */
	public String[] getCc() {
		return cc;
	}

	/**
	 * @param 抄送
	 *            the cc to set
	 */
	public void setCc(String[] cc) {
		this.cc = cc;
	}

	/**
	 * @return 暗抄送
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * @param 暗抄送
	 *            the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return 是否Html
	 */
	public boolean isHtml() {
		return html;
	}

	/**
	 * @param 是否Html
	 *            the html to set
	 */
	public void setHtml(boolean html) {
		this.html = html;
	}

	/**
	 * @return 附件
	 */
	public File[] getFile() {
		return file;
	}

	/**
	 * @param 附件
	 *            the file to set
	 */
	public void setFile(File[] file) {
		this.file = file;
	}
}
