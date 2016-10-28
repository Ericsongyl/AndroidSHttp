package com.zsy.core.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @description: 格式化工具类
 * @date: 2015-7-3 上午10:31:04
 * @author: wangqing
 * @version 1.0.0
 */
public class FormatUtils {

	/**
	 * 分转成元
	 * 
	 * @param amString
	 * @return
	 */
	public static String formatF2Y(long amString) {
		return formatF2Y(String.valueOf(amString));
	}

	/**
	 * 分转成元
	 * 
	 * @param amString
	 * @return
	 */
	public static String formatF2Y(int amString) {
		return formatF2Y(String.valueOf(amString));
	}

	/**
	 * formatF2Y 分转换成元
	 * 
	 * @param amString
	 * @return String
	 * @since 1.0.0
	 */

	public static String formatF2Y(String amString) {
		StringBuffer result = new StringBuffer();
		if (isEmpty(amString)) {
			result.append("0.00");
		} else if (amString.length() == 1) {
			result.append("0.0").append(amString);
		} else if (amString.length() == 2) {
			result.append("0.").append(amString);
		} else {
			String intString = amString.substring(0, amString.length() - 2);
			for (int i = 1; i <= intString.length(); i++) {
				if ((i - 1) % 3 == 0 && i != 1) {
					result.append(",");
				}
				result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
			}
			result.reverse().append(".").append(amString.substring(amString.length() - 2));
		}
		return result.toString();
	}

	/**
	 * formatY2F 元转换成分
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatY2F(long amount) {
		return formatY2F(String.valueOf(amount));
	}

	/**
	 * formatY2F 元转换成分
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatY2F(int amount) {
		return formatY2F(String.valueOf(amount));
	}

	/**
	 * formatY2F 元转换成分
	 * 
	 * @param amount
	 * @return String
	 * @since 1.0.0
	 */
	public static String formatY2F(String amount) {
		String currency = "";
		int index = 0;
		int length = 0;
		if (amount != null) {
			currency = amount.replaceAll("\\$|\\￥|\\,", "");
			index = currency.indexOf(".");
			length = currency.length();
		}
		Long amLong = 0l;
		if (isEmpty(amount) || index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	/**
	 * 格式化人民币，同时/100 bichuanfeng
	 * 
	 * @return String
	 */
	public static String getDecimalFormat(String str) {
		double initValue = 0;
		String outStr = "";
		try {
			if (str != null && !"".equals(str.trim())) {
				initValue = Double.parseDouble(str);
				Double yuanValue = initValue / 100;
				DecimalFormat fmt = new DecimalFormat("##,###,###,###,###.##");
				double d;
				d = Double.parseDouble(String.valueOf(yuanValue));
				outStr = fmt.format(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outStr;
	}

	/**
	 * 格式化人民币，同时/100， 保留小数点后两位，不足2位补0 bichuanfeng
	 * 
	 * @return String
	 */
	public static String getDecimalFormat2(String str) {
		double initValue = 0;
		String outStr = "";
		try {
			if (str != null && !"".equals(str.trim())) {
				initValue = Double.parseDouble(str);
				Double yuanValue = initValue / 100;
				DecimalFormat fmt = new DecimalFormat("##,###,###,###,###.##");
				double d;
				d = Double.parseDouble(String.valueOf(yuanValue));
				outStr = fmt.format(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (outStr.contains(".") && outStr.substring(outStr.lastIndexOf(".")).length() <= 2) {
			outStr = outStr + "0";
		}
		return outStr;
	}

	/**
	 * 金额格式化
	 * 
	 * @param s
	 *            金额
	 * @param len
	 *            小数位数
	 * @return 格式后的金额
	 */
	public static String insertComma(String s, int len) {
		if (s == null || s.length() < 1) {
			return "";
		}
		try {
			NumberFormat formater = null;
			double num = Double.parseDouble(s);
			if (len == 0) {
				formater = new DecimalFormat("###,###");
			} else {
				StringBuffer buff = new StringBuffer();
				buff.append("###,###.00");
				for (int i = 0; i < len; i++) {
					buff.append("#");
				}
				formater = new DecimalFormat(buff.toString());
			}
			return formater.format(num);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 金额去掉“,”
	 * 
	 * @param s
	 *            金额
	 * @return 去掉“,”后的金额
	 */
	public static String delComma(String s) {
		String formatString = "";
		if (s != null && s.length() >= 1) {
			formatString = s.replaceAll(",", "");
		}

		return formatString;
	}

	/**
	 * 格式化日期
	 * 
	 * @param s
	 * @return 样式:2016-06-15
	 */
	public static String dateFormat(String s) {
		try {
			long time = Long.parseLong(s);//
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = new Date(time);
			String t1 = format.format(d1);
			return t1;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期与时间
	 * 
	 * @param s
	 * @return 样式:2016-06-15 16:50:30
	 */
	public static String dateTimeFormat(String s) {
		try {
			long time = Long.parseLong(s);//
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = new Date(time);
			String t1 = format.format(d1);
			return t1;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期与时间
	 * 
	 * @param s
	 * @return 样式:16:50:30
	 */
	public static String timeFormat(String s) {
		try {
			long time = Long.parseLong(s);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			Date d1 = new Date(time);
			String t1 = format.format(d1);
			return t1;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期与时间
	 * 
	 * @param s
	 * @return 样式:10月24日 10:00
	 */
	public static String presaleFormat(String s) {
		try {
			long time = Long.parseLong(s);
			SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
			Date d = new Date(time);
			String t = format.format(d);
			return t;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化日期与时间
	 * 
	 * @param s
	 * @return 样式:10月24日 10:00
	 */
	public static String presaleFormat2(String s) {
		try {
			long time = Long.parseLong(s);
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
			Date d = new Date(time);
			String t = format.format(d);
			return t;
		} catch (Exception e) {
			return "";
		}
	}

	public static String presaleFormatYMD(String s) {
		try {
			long time = Long.parseLong(s);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date(time);
			String t = format.format(d);
			return t;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 自定义时间格式化
	 * 
	 * @param dateStyle
	 *            传递进来需要格式化后日期的样式
	 * @param s
	 *            需要格式化的时间戳
	 * @return 格式化后的时间 bichuanfeng 2016年1月6日
	 */
	public static String customDataTime(String dateStyle, String s) {
		try {
			long time = Long.parseLong(s);
			SimpleDateFormat format = new SimpleDateFormat(dateStyle);
			Date d = new Date(time);
			String t = format.format(d);
			return t;
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean isEmpty(String str) {
		boolean isEmpty = false;
		if ((str != null) && (str.trim().length() > 0)) {
			isEmpty = false;
		} else {
			isEmpty = true;
		}
		return isEmpty;
	}
}
