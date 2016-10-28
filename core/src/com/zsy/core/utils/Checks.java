package com.zsy.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.Toast;
/**
 * 
 * @description: 
 * @date: 2015-7-3 上午10:07:41
 * @author: wangqing
 * @version 1.0.0
 */
public class Checks {
	
	public static boolean isEmpty(String str) {
		boolean isEmpty = false;
		if ((str != null) && (str.trim().length() > 0)) {
			isEmpty = false;
		} else {
			isEmpty = true;
		}
		return isEmpty;
	}

	public static boolean isEqual(String str, String des) {
		boolean isEqual = false;
		if (!isEmpty(str) && !isEmpty(des)) {
			isEqual = str.equals(des);
		}
		return isEqual;
	}

	/**
	 * 检查银行卡是否合法
	 * isLegalBankCardNum
	 * @param cont
	 * @param card
	 * @return
	 * boolean
	 * 841306 
	 * @since  1.0.0
	 */
	public static boolean isLegalBankCardNum(Context cont, String card) {
		boolean isCard = false;
		String regex = "^\\d{10,24}$";
		isCard = Pattern.matches(regex, card);
		if (!isCard) {
			Toast.makeText(cont, "银行卡号错误", Toast.LENGTH_SHORT).show();
		}
		return isCard;
	}

	/**
	 * 检测是否输入名字
	 * isLegalInputName
	 * 
	 * @param cont
	 * @param name
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isLegalInputName(Context cont, String name) {
		boolean isCard = true;
		if (isEmpty(name) || name.length() < 2) {
			isCard = false;
			Toast.makeText(cont, "您输入的名字不正确", Toast.LENGTH_SHORT).show();
		}
		return isCard;
	}

	/**
	 * 对身份证号进行验证
	 * isLegalCardId
	 * 
	 * @param cont
	 * @param id
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isLegalCardId(Context cont, String id) {
		String regex = "([0-9]{17}([0-9]|X))|([0-9]{15})";
		boolean isTrue = false;
		isTrue = Pattern.matches(regex, id);
//		if (!isTrue) {
//			Toast.makeText(cont, "您输入的身份证号不正确", Toast.LENGTH_SHORT).show();
//		}
		return isTrue;
	}
	
	/**
	 * 检查手机号
	 * isLegalPhoneNum
	 * 
	 * @param cont
	 * @param mobile
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isLegalPhoneNum(Context cont, String mobile) {
		boolean isTel = false;
		isTel = Pattern.matches("^(1)\\d{10}$", mobile);
		if (!isTel) {
			Toast.makeText(cont, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
		}
		return isTel;
	}

	/**
	 * 检测银行卡密码、支付密码是否是6位数字的密码
	 * isLegalSixIntePwd
	 * 
	 * @param cont
	 * @param pwd
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isLegalSixIntePwd(Context cont, String pwd) {
		boolean isPwd = false;
		String regex = "^[0-9]{6}$";
		isPwd = Pattern.matches(regex, pwd);
		if (!isPwd) {
			Toast.makeText(cont, "您输入的密码不正确", Toast.LENGTH_SHORT).show();
		}
		return isPwd;
	}

	/**
	 * 检测验证码
	 * isLegalCaptcha
	 * 
	 * @param cont
	 * @param pwd
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isLegalCaptcha(Context cont, String pwd) {
		boolean isPwd = false;
		String regex = "^[0-9]{6}$";
		isPwd = Pattern.matches(regex, pwd);
//		if (!isPwd) {
//			Toast.makeText(cont, "您输入的验证码不正确", Toast.LENGTH_SHORT).show();
//		}
		return isPwd;
	}

	/**
	 * 检测该字符是否是x
	 * isStr
	 * 
	 * @param text
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isStr(CharSequence text) {
		try {
			if (text.toString().toUpperCase().contains("X")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * 检测是否是纯数字
	 * isNum
	 * 
	 * @param text
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isNum(CharSequence text) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(text);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	public static final boolean validPayPwd(String password, String loginName) {
		// // 6-20 个字符
		// if (!password.matches("^\\d{6,20}$")) {
		// return false;
		// }
		// 不能包含有连续四位及以上顺序(或逆序)数字或字母；（如：1234、abcd等）
		int asc = 1;
		int desc = 1;
		int lastChar = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			int currentChar = password.charAt(i);
			if (!(password.charAt(i) + "").matches("[0-9]")) {
				asc = 0;
				desc = 0;
			} else if (lastChar == currentChar - 1) {
				asc++;
				desc = 1;
			} else if (lastChar == currentChar + 1) {
				desc++;
				asc = 1;
			} else {
				asc = 1;
				desc = 1;
			}
			if (asc >= 4 || desc >= 4) {
				return false;
			}
			lastChar = currentChar;
		}

		// 不能将账号名作为密码的一部分存在于密码，账号密码也不能一样
		if (!isEmpty(loginName)
				&& (loginName.equals(password) || password.contains(loginName))) {
			return false;
		}
		return true;
	}
}
