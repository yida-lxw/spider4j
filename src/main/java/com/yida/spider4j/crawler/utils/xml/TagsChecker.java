package com.yida.spider4j.crawler.utils.xml;

/**
 * @ClassName: TagsChecker
 * @Description: 标签闭合检测
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月17日 下午3:30:44
 * 
 */
public class TagsChecker {
	public static boolean check(String str) {
		TagsList[] unclosedTags = getUnclosedTags(str);

		if (unclosedTags[0].size() != 0) {
			return false;
		}
		for (int i = 0; i < unclosedTags[1].size(); i++) {
			if (unclosedTags[1].get(i) != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: fix
	 * @Description: 修复未闭合的HTML标签
	 * @param @param str
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String fix(String str) {
		// 存放修复后的字符串
		StringBuilder fixed = new StringBuilder();
		TagsList[] unclosedTags = getUnclosedTags(str);

		// 生成新字符串
		for (int i = unclosedTags[0].size() - 1; i > -1; i--) {
			fixed.append("<" + unclosedTags[0].get(i) + ">");
		}

		fixed.append(str);

		for (int i = unclosedTags[1].size() - 1; i > -1; i--) {
			String s = null;
			if ((s = unclosedTags[1].get(i)) != null) {
				fixed.append("</" + s + ">");
			}
		}

		return fixed.toString();
	}

	private static TagsList[] getUnclosedTags(String str) {
		// 存放标签
		StringBuilder temp = new StringBuilder();
		TagsList[] unclosedTags = new TagsList[2];
		// 前不闭合，如有</div>而前面没有<div>
		unclosedTags[0] = new TagsList();
		// 后不闭合，如有<div>而后面没有</div>
		unclosedTags[1] = new TagsList();
		// 记录双引号"或单引号'
		boolean flag = false;
		// 记录需要跳过'...'还是"..."
		char currentJump = ' ';
		// 当前 & 上一个
		char current = ' ', last = ' ';

		// 开始判断
		for (int i = 0; i < str.length();) {
			// 读取一个字符
			current = str.charAt(i++);
			if (current == '"' || current == '\'') {
				// 若为引号，flag翻转
				flag = flag ? false : true;
				currentJump = current;
				if (flag) {
					// 跳过引号之间的部分
					while (i < str.length() && str.charAt(i++) != currentJump);
					flag = false;
				}
			}
			// 开始提取标签
			else if (current == '<') {
				current = str.charAt(i++);
				// 标签的闭合部分，如</div>
				if (current == '/') {
					current = str.charAt(i++);

					// 读取标签
					while (i < str.length() && current != '>') {
						temp.append(current);
						current = str.charAt(i++);
					}

					// 从tags_bottom移除一个闭合的标签
					// 若移除失败，说明前面没有需要闭合的标签
					if (!unclosedTags[1].remove(temp.toString())) {
						// 此标签需要前闭合
						unclosedTags[0].add(temp.toString());
					}
					// 清空temp
					temp.delete(0, temp.length());
				}
				// 标签的前部分，如<div>
				else {
					last = current;
					while (i < str.length() && current != ' ' && current != ' '
							&& current != '>') {
						temp.append(current);
						last = current;
						current = str.charAt(i++);
					}

					// 已经读取到标签，跳过其他内容，如<div id=test>跳过id=test
					while (i < str.length() && current != '>') {
						last = current;
						current = str.charAt(i++);
						if (current == '"' || current == '\'') {
							flag = flag ? false : true;
							currentJump = current;
							// 若引号不闭合，跳过到下一个引号之间的内容
							if (flag) {
								while (i < str.length()
										&& str.charAt(i++) != currentJump)
									;
								current = str.charAt(i++);
								flag = false;
							}
						}
					}
					// 判断这种类型：<TagName />
					if (last != '/' && current == '>') {
						unclosedTags[1].add(temp.toString());
					}
					temp.delete(0, temp.length());
				}
			}
		}
		return unclosedTags;
	}
	
	public static void main(String[] args) {
		System.out.println("--功能测试--");
        String str1 = "tt</u>ss</a>aa<div name=\"<test>\" id='3' other='<test>'><b>sff";
        String str2 = "tt<u>ss</u><div id=test name=\"<test>\"><a>fds</a></div>";
        String str3 = "<link rel=\"apple-touch-icon\" href=\"/pics/movie/apple-touch-icon.png\"><data><employee><name id=\"1\">益达</name><name id=\"2\">yida</name>"
				+ "<title>Manager</title></employee></data>";
        System.out.println("检查文本1 " + str1);
        System.out.println("结果1：" + TagsChecker.check(str1));
        System.out.println();
        
        System.out.println("检查文本2 " + str2);
        System.out.println("结果2：" + TagsChecker.check(str2));
        System.out.println();
        
        System.out.println("检查文本3 " + str3);
        System.out.println("结果3：" + TagsChecker.check(str3));
        System.out.println();
        
        System.out.println();
        
        System.out.println("待修复文本1 " + str1);
        System.out.println("修复结果1：" + TagsChecker.fix(str1));
        System.out.println();
        
        System.out.println("待修复文本2 " + str2);
        System.out.println("修复结果2：" + TagsChecker.fix(str2));
        System.out.println();
        
        System.out.println("待修复文本3 " + str3);
        System.out.println("修复结果3：" + TagsChecker.fix(str3));
        System.out.println();
        
        for (int i = 0; i < 10; i++) {
            str1 += str1;
        }
        
        System.out.println();
        System.out.println("--效率测试--");
        System.out.println("文本长度：" + str1.length());
        long t1 = System.currentTimeMillis();
        boolean closed = TagsChecker.check(str1);
        long t2 = System.currentTimeMillis();
        TagsChecker.fix(str1);
        long t3 = System.currentTimeMillis(); 
        System.out.println("检查用时：" + (t2 - t1) + " 毫秒 结果：" + closed);
        System.out.println("修复用时：" + (t3 - t2) + " 毫秒");
	}
}
