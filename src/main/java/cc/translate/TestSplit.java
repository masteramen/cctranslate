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

		String str="s1 w2 w3 w4 w5";

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
		return sb.toString().trim().split("\n");
	}

}
