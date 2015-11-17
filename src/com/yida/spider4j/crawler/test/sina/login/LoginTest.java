package com.yida.spider4j.crawler.test.sina.login;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.yida.spider4j.crawler.utils.common.FastJSonUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;
import com.yida.spider4j.crawler.utils.httpclient.Result;
import com.yida.spider4j.crawler.utils.httpclient.StringEntityHandler;
import com.yida.spider4j.crawler.utils.io.FileUtils;

/**
 * @ClassName: LoginTest
 * @Description: 新浪微博登录测试
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月29日 下午5:48:58
 *
 */
public class LoginTest {
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static void main(String[] args) throws Exception {
		//登录账号和密码
		String account = "xxxxxxxxxxxxx";
		String pwd = "xxxxxxxxx";
		
		//新浪微博登录
		Result result = login(account,pwd);
		System.out.println(new StringEntityHandler().handleEntity(result.getHttpEntity()));
		String cookie = result.getCookie();
		cookie = getSUB(cookie);
		System.out.println("cookie:" + cookie);
		
		//访问【文章同学】的新浪微博首页
		String html = visitWenZhang(cookie);
		
		//把返回的html内容写入文件，方便打开进行验证是否正确返回
		FileUtils.writeFile(html, "C:/wenzhang.html", "UTF-8", false);
		
		
	}
	
	public static String getSUB(String cookie) {
		String sub = cookie.replaceAll(".*;SUB=(.*);SUBP=.*", "$1");
		sub = "SUB=" + sub + ";";
		return sub;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: visitWenZhang
	 * @Description: 访问【文章同学】的新浪微博
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	public static String visitWenZhang(String cookie) throws Exception {
		String wenzhang = "http://weibo.com/wenzhang626";
		Map<String,String> headers = new HashMap<String,String>();
		//SUB cookie项是关键
		//SUB=_2A257NaVGDeTxGedH7lsZ8yvPwziIHXVYQpGOrDV8PUNbuNAMLROnkW8p9rH2Bsuc2yUSKU1PzJykmlLc7Q..;
		headers.put("Cookie", cookie);
		String html = HttpClientUtils.getHTML(wenzhang,headers);
		//System.out.println(html);
		return html;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: login
	 * @Description: 模拟新浪微博登录
	 * @param @param account
	 * @param @param pwd
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	public static Result login(String account,String pwd) throws Exception {
		String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)&_=" + 
				System.currentTimeMillis();
		String content = prelogin();
		Map<String,Object> paramMap = FastJSonUtils.toMap(content);
		String pubkey = paramMap.get("pubkey").toString();
		String servertime = paramMap.get("servertime").toString();
		String nonce = paramMap.get("nonce").toString();
		String rsakv = paramMap.get("rsakv").toString();
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Host", "login.sina.com.cn");
		headers.put("Origin", "http://weibo.com");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("entry", "weibo");
		params.put("gateway", "1");
		params.put("from", "");
		params.put("savestate", "7");
		params.put("useticket", "1");
		params.put("pagerefer", "http://s.weibo.com/weibo/%25E6%2596%2587%25E7%25AB%25A0%25E5%2590%258C%25E5%25AD%25A6?topnav=1&wvr=6&b=1");
		params.put("vsnf", "1");
		params.put("su", encodeAccount(account));
		params.put("service", "miniblog");
		params.put("servertime", servertime);
		params.put("nonce", nonce);
		params.put("pwencode", "rsa2");
		params.put("rsakv", rsakv);
		params.put("sp", getSP(pwd, pubkey, servertime, nonce));
		params.put("encoding", "UTF-8");
		params.put("sr", "1366*768");
		params.put("prelt", "1011");
		params.put("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
		params.put("domain", "weibo.com");
		params.put("returntype", "META");
		
		Result result = HttpClientUtils.post(url, headers, params);
		//System.out.println(json);
		return result;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: prelogin
	 * @Description: 登录必需参数获取
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	public static String prelogin() throws Exception {
		String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=&rsakt=mod&client=ssologin.js(v1.4.18)&_=1446099453139";
		String content = HttpClientUtils.getHTML(url);
		
		if(null != content && !content.equals("")) {
			content = content.replaceAll("sinaSSOController.preloginCallBack\\((.*)\\)", "$1");
		}
		//System.out.println(content);
		return content;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getSP
	 * @Description: 登录密码加密
	 * @param @param pwd
	 * @param @param pubkey
	 * @param @param servertime
	 * @param @param nonce
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getSP(String pwd,String pubkey,String servertime,String nonce) {
		String t = "10001";
		String message = servertime + "\t" + nonce + "\n" + pwd;
		String result = null;
		try {
			result = rsa(pubkey, t , message);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		System.out.println("RSA加密后的密码：" + result);
		return result;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: encodeAccount
	 * @Description: 登录账号编码
	 * @param @param account
	 * @param @return
	 * @return String
	 * @throws
	 */
	private static String encodeAccount(String account) {
		String userName = "";
		try {
			userName = StringUtils.base64Encode(URLEncoder.encode(account,
					"UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return userName;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: rsa
	 * @Description: RSA加密
	 * @param @param pubkey
	 * @param @param exponentHex
	 * @param @param pwd
	 * @param @return
	 * @param @throws IllegalBlockSizeException
	 * @param @throws BadPaddingException
	 * @param @throws NoSuchAlgorithmException
	 * @param @throws InvalidKeySpecException
	 * @param @throws NoSuchPaddingException
	 * @param @throws InvalidKeyException
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 */
	public static String rsa(String pubkey, String exponentHex, String pwd)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			UnsupportedEncodingException {
		KeyFactory factory = KeyFactory.getInstance("RSA");

		BigInteger m = new BigInteger(pubkey, 16);
		BigInteger e = new BigInteger(exponentHex, 16);
		RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

		//创建公钥
		RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
		Cipher enc = Cipher.getInstance("RSA");
		enc.init(Cipher.ENCRYPT_MODE, pub);

		byte[] encryptedContentKey = enc.doFinal(pwd.getBytes("UTF-8"));

		return new String(encodeHex(encryptedContentKey));
	}

	protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	public static char[] encodeHex(final byte[] data) {
		return encodeHex(data, true);
	}
}
