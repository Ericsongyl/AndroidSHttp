package com.zsy.core.bean;

import java.io.Serializable;

/**
 * 
 * @description: 登陆后返回的用户信息类
 * @date: 2015-7-3 上午10:06:14
 * @author: wangqing
 * @version 1.0.0
 */
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -3272438813126942208L;
	public int code;
	public String message;
	public LoginInfo data;

	public static class LoginInfo implements Serializable {
		private static final long serialVersionUID = -3272438813126942308L;
		public String customerId;
		public String loginName;
		public String gesturePwd;
		public String transCode;
		public String mobile;
		public String loginPsw;
		public int riskTolerance;
		public String riskToleranceText;
		public String cardCode; //身份证号
		public int isCheckCard; //是否实名，1实名
		public String trueName; //真实姓名
	}

}
