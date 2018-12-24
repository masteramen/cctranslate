package cc.translate;

import java.util.Arrays;
import java.util.StringTokenizer;

public class TestSplit {
	public static String addLinebreaks(String input, int maxLineLength) {
	    StringTokenizer tok = new StringTokenizer(input, " ");
	    StringBuilder output = new StringBuilder(input.length());
	    int lineLen = 0;
	    while (tok.hasMoreTokens()) {
	        String word = tok.nextToken();

	        if (lineLen + word.length() > maxLineLength) {
	            output.append("\n");
	            lineLen = 0;
	        }
	        output.append(word);
	        lineLen += word.length();
	    }
	    return output.toString();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str="　　在改革开放的奋进足音中，亿万中国人民改变了自己的命运，迎来新的希望。全国居民人均可支配收入由171元增加到2.6万元，贫困人口累计减少7.4亿人，建成了包括养老、医疗、低保、住房在内的世界最大的社会保障体系……40年来，百姓生活不断改善，忍饥挨饿、缺吃少穿、生活困顿这些几千年来困扰我国人民的问题总体上一去不复返了。从普遍贫穷到奔向全面小康，从封闭落后到自信开放，改革开放深刻改变中国人民的面貌和中华民族的面貌，为实现民族复兴凝聚起万众一心、众志成城的磅礴力量。";

		splitStringByMaxWords(str,3);

//System.out.println(sb.toString());
	}
	private static String[] splitStringByMaxWords(String str,int maxWords) {
		String[] strArr = str.split("\\s+");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<strArr.length;i+=maxWords) {
			for(int k=i;k<i+maxWords&&k<strArr.length;k++) {
				sb.append(strArr[k]).append(" ");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());

		return sb.toString().trim().split("\n");
	}

}
