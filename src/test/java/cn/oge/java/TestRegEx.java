package cn.oge.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TestRegEx {
	@Test
	public void test1() {
		String str = "{\"BasicResponse\":{\"succeeded\":1},\"RTDataSets\":[{\"tag\":\"0101B0AKA01FA031H\",\"kksCode\":\"EB002HP1MKA01MK016BL01J1CA004CA01\",\"RTDataValues\":[]}]}";
		// String.matches()是全匹配
		Assert.assertFalse(str.matches("\\{"));
		Assert.assertTrue(str.matches("\\{.*"));
		
	}

	@Test
	public void test2() {
		String str = "{\"BasicResponse\":{\"succeeded\":1},\"RTDataSets\":[{\"tag\":\"0101B0AKA01FA031H\",\"kksCode\":\"EB002HP1MKA01MK016BL01J1CA004CA01\",\"RTDataValues\":[]}]}";
		Pattern pattern = Pattern.compile("^.*?\\{");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			System.out.println("ok"+matcher.group());
		}else{
			System.out.println("no");
		}
	}

	@Test
	public void test3() {
		String Str = new String("Welcome to Tutorialspoint.com");
		Assert.assertTrue(Str.matches("(.*)Tutorials(.*)"));
		Assert.assertFalse(Str.matches("Tutorials"));
		Assert.assertTrue(Str.matches("Welcome(.*)"));
	}

}
