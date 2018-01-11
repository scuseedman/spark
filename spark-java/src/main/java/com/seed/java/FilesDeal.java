package com.seed.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.seed.utils.IOUtil;

public class FilesDeal {
	public static Pattern SPLIT = Pattern.compile(",\\[");
	public static int num;
	public static void main(String[] args) throws Exception {
		File [] files = new File(args[0]).listFiles();
		for(File file:files){
			if(file.getName().startsWith(".") || file.getName().startsWith("_"))continue;
//			System.out.println(file.getName());
			saveFiles(file);
//			break;
		}
		System.out.println("总记录是 ===>>> " + num);
	}
	public static void saveFiles(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		System.out.println(file.getPath().replace(file.getName(), ""));
		String line = null;
		String basePath = file.getPath().replace(file.getName(), "");
		while( (line = br.readLine()) != null){
			line = line.replaceAll("\\(", "").replaceAll("\\]\\)", "");
			String [] mobiles = SPLIT.split(line);
			List<String> list = Arrays.asList(mobiles[1].replaceAll(" ", "").split(","));
			IOUtil.saveCollection(list,new File(basePath + mobiles[0]));
			num += 1;
		}
		br.close();
	}
}
