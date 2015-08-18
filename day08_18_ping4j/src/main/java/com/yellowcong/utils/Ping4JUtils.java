package com.yellowcong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Ping4JUtils {
	//用于 存储 我们字典对应的 拼音和汉字
	private static Map<String,String> map=new HashMap<String,String>();
	//key 是我们的 拼音    Set<String > 是对应的汉字
	private static Map<String,Set<String>> spellingMap=new HashMap<String,Set<String>>();
	
	public static void main(String[] args) {
		loadDict();
        
        System.out.println(spellingMap.get("baidu"));
	}
	
	public static void loadDict(){
		InputStream in = null;
		try {
			in = Ping4JUtils.class.getClassLoader().getResourceAsStream("main2012.dic");
			BufferedReader buff = new BufferedReader(new InputStreamReader(in , "UTF-8"), 512);
			//汉语拼音设定
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	        format.setVCharType(HanyuPinyinVCharType.WITH_V);
	        
	        String line = null;
	        while((line = buff.readLine())!= null){
	        	//将一句话转化为一个一个的字符
	        	char[] temps=line.trim().toLowerCase().toCharArray();
	        		
	        	StringBuffer sb=new StringBuffer();
	        	for(char ch : temps){
	        		//判断字符是否是 汉字
					if(CharacterUtil.identifyCharType(ch)==CharacterUtil.CHAR_CHINESE){
						String[] ss=PinyinHelper.toHanyuPinyinStringArray(ch,format);
						if(ss!=null && ss.length>0){
							sb.append(ss[0]);
						}
						
					}else{
						sb.append(ch);
					}
				}
	        	//添加到map中
	        	//key  拼音   val 汉字
	        	map.put(line, sb.toString());
	        	
	        	//获取拼音，
				Set<String> set=spellingMap.get(sb.toString());
				if(set==null || set.size()==0){
					set=new HashSet<String>();
					set.add(line);
				}else{
					set.add(line);
				}
				spellingMap.put(sb.toString(), set);
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
