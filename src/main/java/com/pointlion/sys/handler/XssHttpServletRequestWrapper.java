package com.pointlion.sys.handler;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * XSS过滤
 * @date 2018年10月23日 上午11:19:10
 * @author huang_zongxiang
 * @email 962430867@qq.com
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(xssEncode(name));
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] value = super.getParameterValues(name);
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				value[i] = xssEncode(value[i]);
			}
		}
		return value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return super.getParameterMap();
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。 如果需要获得原始的值，则通过super.getHeaders(name)来获取
	 * getHeaderNames 也可能需要覆盖 这一段代码在一开始没有注释掉导致出现406错误，原因是406错误是HTTP协议状态码的一种，
	 * 表示无法使用请求的内容特性来响应请求的网页。一般是指客户端浏览器不接受所请求页面的 MIME 类型。
	 * 
	 * @Override public String getHeader(String name) {
	 * 
	 *           String value = super.getHeader(xssEncode(name)); if (value !=
	 *           null) { value = xssEncode(value); } return value; }
	 **/

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符 在保证不删除数据的情况下保存
	 * 
	 * @param s
	 * @return 过滤后的值
	 */
	public static String xssEncode(String content) {
        if (null == content) {
            return null;
        }
        if (0 == content.length()) {
            return "";
        }
        // 需要滤除的脚本事件关键字
        String[] eventKeywords = { "onmouseover", "onmouseout", "onmousedown",
                "onmouseup", "onmousemove", "onclick", "ondblclick",
                "onkeypress", "onkeydown", "onkeyup", "ondragstart",
                "onerrorupdate", "onhelp", "onreadystatechange", "onrowenter",
                "onrowexit", "onselectstart", "onload", "onunload",
                "onbeforeunload", "onblur", "onerror", "onfocus", "onresize",
                "onscroll", "oncontextmenu", "alert" };
        content = replace(content, "<script", "", false);
        content = replace(content, "</script", "", false);
        content = replace(content, "<marquee", "", false);
        content = replace(content, "</marquee", "", false);
        content = replace(content, "<svg", "", false);
        // 滤除脚本事件代码
        for (int i = 0; i < eventKeywords.length; i++) {
            content = replace(content, eventKeywords[i],
                    "_" + eventKeywords[i], false); // 添加一个"_", 使事件代码无效
        }
        return content;
    }

    /**
     * 将字符串 source 中的 oldStr 替换为 newStr, matchCase 为是否设置大小写敏感查找
     * 
     * @param source
     *            需要替换的源字符串
     * @param oldStr
     *            需要被替换的老字符串
     * @param newStr
     *            替换为的新字符串
     * @param matchCase
     *            是否需要按照大小写敏感方式查找
     */
    private static String replace(String source, String oldStr, String newStr,
            boolean matchCase) {
        if (source == null) {
            return null;
        }
        // 首先检查旧字符串是否存在, 不存在就不进行替换
        if (source.toLowerCase().indexOf(oldStr.toLowerCase()) == -1) {
            return source;
        }
        int findStartPos = 0;
        int a = 0;
        while (a > -1) {
            int b = 0;
            String str1, str2, str3, str4, strA, strB;
            str1 = source;
            str2 = str1.toLowerCase();
            str3 = oldStr;
            str4 = str3.toLowerCase();
            if (matchCase) {
                strA = str1;
                strB = str3;
            } else {
                strA = str2;
                strB = str4;
            }
            a = strA.indexOf(strB, findStartPos);
            if (a > -1) {
                b = oldStr.length();
                findStartPos = a + b;
                StringBuffer bbuf = new StringBuffer(source);
                source = bbuf.replace(a, a + b, newStr) + "";
                // 新的查找开始点位于替换后的字符串的结尾
                findStartPos = findStartPos + newStr.length() - b;
            }
        }
        return source;
    }
}
