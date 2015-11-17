package com.yida.spider4j.crawler.utils.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.utils.charset.CharsetDetectorFacade;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: StringEntityHandler
 * @Description: Response响应对象to String处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月24日 下午9:54:03
 *
 */
public class StringEntityHandler extends DefaultConfigurable implements EntityHandler<String>{
    private String encoding = "UTF-8";
    
    /**供外部重置字符串转换编码*/
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public StringEntityHandler(){}
	
	public StringEntityHandler(String encoding) {
		this.encoding = encoding;
	}

	public String handleEntity(HttpEntity entity) throws IOException {
		if(null == entity) {
			return null;
		}
		if(StringUtils.isNotEmpty(this.encoding)) {
			return EntityUtils.toString(entity, this.encoding);
		}
		this.encoding = getHtmlCharset(entity);
		//return toString(entity, Charset.forName(this.encoding));
		return EntityUtils.toString(entity, this.encoding);
		
		/*if (entity != null) {
			String contentCharset = EntityUtils.getContentCharSet(entity);
            String html = EntityUtils.toString(entity, encoding);
            String pageCharset = StringUtils.getCharsetFromMeta(html);
            String charset = null;
            if(pageCharset != null) {
            	charset = pageCharset;
            } else {
            	if(contentCharset != null){
            		charset = contentCharset;
            	} else {
            		charset = encoding;
            	}
            }
            html = EntityUtils.toString(entity, charset);
            boolean unreadable = StringUtils.isMessyCode(html);
            //判断是否乱码
            if(unreadable) {
            	 html = EntityUtils.toString(entity, "GBK");
            }
            return html;
        }
		return null;*/
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getHtmlCharset
	 * @Description: 确定网页的提取字符集编码(确保提取到的网页内容不会乱码,这很关键)
	 * @param @param entity
	 * @param @return
	 * @param @throws IOException
	 * @return String
	 * @throws
	 */
    protected String getHtmlCharset(HttpEntity entity) throws IOException {
        // 从content-type中提取字符集编码
        String contentType = entity.getContentType().getValue();
        String charset = StringUtils.getCharsetFromContentType(contentType);
        if (StringUtils.isNotEmpty(charset)) {
        	LogUtils.debug("Auto get charset from content-type: {" + charset + "}");
            return charset;
        }
        
        Charset defaultCharset = Charset.defaultCharset();
        //InputStream inputStream = entity.getContent();
        String content = EntityUtils.toString(entity,defaultCharset.name());
        if (StringUtils.isNotEmpty(content)) {
            charset = StringUtils.getCharsetFromMeta(content);
            LogUtils.debug("Auto get charset from meta: {" + charset + "}");
        }
        //若字符编码仍然为空,使用字符集编码探测器来自动探测字符集编码
        if(StringUtils.isEmpty(charset)) {
        	Charset charsetObj = CharsetDetectorFacade.getInstance().detectCharset(entity.getContent());
        	if(charsetObj == null) {
        		//若自动探测器返回为空,则采用配置文件spider4j.properties中配置的网页默认提取字符编码
        		charset = this.config.getPageDefaultEncoding();
        	} else {
        		charset = charsetObj.name();
        	}
        }
        return charset;
    }
    
    public static String toString(
            final HttpEntity entity, final Charset defaultCharset) {
        Args.notNull(entity, "Entity");
        InputStream instream = null;
        try {
        instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        	Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;
            try {
                final ContentType contentType = ContentType.get(entity);
                if (contentType != null) {
                    charset = contentType.getCharset();
                }
            } catch (final UnsupportedCharsetException ex) {
                throw new UnsupportedEncodingException(ex.getMessage());
            }
            if (charset == null) {
                charset = defaultCharset;
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[4096];
            int l;
            while((l = reader.read(tmp)) > 0) {
                buffer.append(tmp, 0, l);
            }
            //FileUtils.writeFile(buffer.toString(), "C:/movie.html", "UTF-8", false);
            //System.out.println(buffer.toString());
            return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != instream) {
				try {
					instream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return null;
    }
	
	public String getEncoding() {
		return encoding;
	}
}
