package com.cat.boot.util;

import java.util.Random;

public class RadomUtil {
	
    private static char[] Number = {'1','2','3','4','5','6','7','8','9','0'};
    private static char[] Lowercase = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static char[] Capitalization = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private static Object[] AnyChar = { Number , Lowercase , Capitalization };

	public static char RandomPos(){
        char[] currentChar = (char[]) AnyChar[new Random().nextInt(AnyChar.length)];
        int pos = new Random().nextInt(currentChar.length);
        int i = 0;
        for(char c : currentChar){
            if(i++ == pos){
                return c;
            }
        }
        return 0;
    }
	
	public static String createStrongRadom(int c){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0 ; i < c ; i ++){
            stringBuffer.append(RandomPos());
        }
        return stringBuffer.toString();
    }

    
	public static String radom(int len) {
		int random = createRandomInt();
		return createRadom(random, len);
	}

	public static String createRadom(int random, int len) {
		Random rd = new Random(random);
		final int maxNum = 62;
		StringBuffer sb = new StringBuffer();
		int rdGet;// 取得随机数
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		int count = 0;
		while (count < len) {
			rdGet = Math.abs(rd.nextInt(maxNum));// 生成的数最大为62-1
			if (rdGet >= 0 && rdGet < str.length) {
				sb.append(str[rdGet]);
				count++;
			}
		}
		return sb.toString();
	}

	public static int createRandomInt() {
		// 得到0.0到1.0之间的数字，并扩大100000倍
		double temp = Math.random() * 100000;
		// 如果数据等于100000，则减少1
		if (temp >= 100000) {
			temp = 99999;
		}
		int tempint = (int) Math.ceil(temp);
		return tempint;
	}

	public static void main(String[] args) {
		RadomUtil pwc = new RadomUtil();
		//System.out.println(pwc.radom(8));
		//System.out.println(Calendar.getInstance().getTimeInMillis());
		
		System.out.println(createStrongRadom(8));
	}
}
