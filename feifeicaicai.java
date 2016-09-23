import java.util.*;
import java.util.regex.*;
import java.security.MessageDigest;

class feifeicaicai {
	static String exp = "";
	static String input = "";
	static String cmd = "";
	
	static Pattern simPattern = Pattern.compile("^!simplify ([a-zA-Z]+=[0-9]+[\\s]*)+$");
	static Pattern simSplit = Pattern.compile("[\\s=]+");
	static Pattern derPattern = Pattern.compile("^!d/d[a-zA-Z]+$");
	static Pattern plusSplit = Pattern.compile("[+]");
	static Pattern mulSplit = Pattern.compile("[*]");
	static Pattern bracketsPattern = Pattern.compile("([a-zA-Z0-9]+\\*)*\\([a-zA-Z0-9*+-^]+\\)(\\*[a-zA-Z0-9]+)*");
	static Pattern naiveBrPattern = Pattern.compile("\\([a-zA-Z0-9*+-^]+\\)");
	
	
	static String[] symbols = new String[100];
	static String[] elements = new String[100];
	static String[] symbolsOut = new String[100];
	static String[] elementsOut = new String[100];
	static String[] splitByPlus;
	static String[] splitByMul;
	static HashMap<String, String> map = new HashMap<String, String>();
	
	
	public static boolean isNumeric(String str){
		for (int i=0;i<str.length();i++)
		if (!Character.isDigit(str.charAt(i)))
			return false;
		return true;
	}
	
	public static String merge(String simplifiedExp) {
		String outAns="";
		splitByPlus = plusSplit.split(simplifiedExp);
		for(int i=0; i<splitByPlus.length; i++) {
			splitByMul = mulSplit.split(splitByPlus[i]);
			long mulsAns=1;
			boolean flag=false;
			boolean flagVar=false;
			String outTmp="";
			for(int j=0; j<splitByMul.length; j++) {
				if(isNumeric(splitByMul[j])) {
					mulsAns=mulsAns*Integer.parseInt(splitByMul[j]);
					flag=true;
				} else {
					flagVar=true;
					outTmp=outTmp+"*"+splitByMul[j];
				}
			}
			if (mulsAns==1&&!flag) 
				outTmp=outTmp.substring(1, outTmp.length());
			else if (mulsAns==0)
				outTmp="";
			else
				outTmp=String.valueOf(mulsAns)+outTmp;
			
			outAns=outAns+"+"+outTmp;
		}
		outAns=outAns.substring(1,outAns.length());
		if (outAns.length()==0) outAns="0";
		String[] plusAnsSplit= plusSplit.split(outAns); 
		int plusAns=0;
		boolean flag=false;
		boolean flagVar=false;
		outAns="";
		for (int i=0;i<plusAnsSplit.length;i++)
			if (isNumeric(plusAnsSplit[i])){
				flag=true;
//				System.out.println(plusAnsSplit[0]+"aaa");
				plusAns=plusAns+Integer.parseInt(plusAnsSplit[i]);
			}
			else {
				flagVar=true;
				outAns=outAns+"+"+plusAnsSplit[i];
			}
		if (plusAns!=0)	
			outAns=String.valueOf(plusAns)+outAns;
		else if (plusAns==0&&flagVar)
			outAns=outAns.substring(1, outAns.length());
		else if (!flagVar)
			outAns=String.valueOf(plusAns);
		
		return outAns;
	}
	
//	public static String Md5(String s) {
//		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
//		try {
//			byte[] btInput = s.getBytes();
//			// 获得MD5摘要算法的 MessageDigest 对象
//			MessageDigest mdInst = MessageDigest.getInstance("MD5");
//			// 使用指定的字节更新摘要
//			mdInst.update(btInput);
//			// 获得密文
//			byte[] md = mdInst.digest();
//			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public static int simplify(String exp, String cmd) {
		String simplifiedExp=exp;
		String[] strs = simSplit.split(cmd);
		for(int i=1; i<strs.length; ) {
			if((simplifiedExp = simplifiedExp.replaceAll(strs[i++], strs[i++])) != exp) {
				System.out.println(simplifiedExp);
			} else {
				return -1;
			}
		}
		System.out.println(merge(simplifiedExp));		
		return 0;
	}	
	
	public static int countPower(String expFrag, String tarVar) {
		int count = 0;
		int index = 0;
		while ((index = expFrag.indexOf(tarVar,index)) != -1) {
			expFrag = expFrag.substring(index + tarVar.length());
			count++;
		}
		return count;
	} 
	
	public static String fraction(String para, String inBrackets) {
		String resultStr = "";
		String[] elements = plusSplit.split(inBrackets);
		for(int i=0; i<elements.length-1; i++) {
			resultStr += para + "*" + elements[elements.length-1] + "+";
		}
		resultStr += para + "*" + elements[elements.length-1];
		return resultStr;
	}
	
	public static String deleteBrackets(String exp) {
		String resultStr = "";
		Matcher naiveBrMatcher = naiveBrPattern.matcher(exp);
		resultStr = naiveBrMatcher.group(0);
		
//		Matcher plusForExp = plusSplit.matcher(exp);
		exp = exp.replaceAll("\\([a-zA-Z0-9*+-^]+\\)", "1");
		exp = merge(exp);
		resultStr = merge(resultStr.substring(1, exp.length()-1));
		resultStr = fraction(exp, resultStr);
		System.out.println("after deletion: " + resultStr);
		return resultStr;
	}
	
	public static String mergeBrackets(String exp) {
		String resultStr = "";
//		Matcher bracketsMatcher = bracketsPattern.matcher(exp);
		while(true) {
			Matcher bracketsMatcher = bracketsPattern.matcher(exp);
			if(bracketsMatcher.find()) {
				System.out.println("aha " + bracketsMatcher.group(0));
				exp = exp.replace(bracketsMatcher.group(0), 
				deleteBrackets(bracketsMatcher.group(0)));
				System.out.println(exp);
			} else 
				break;
		}
		return resultStr = exp;
	}
	
	public static int derivative(String exp, String cmd) {
		String tarVar = cmd.substring(4);
		String derivedExp = "";
		splitByPlus = plusSplit.split(exp);
		
		if(exp.indexOf(tarVar) == -1) {
			System.out.println("Error! No variable!");
			return -1;
		}
		
		for(int i=0; i<splitByPlus.length; i++) {
			String tempStr = "";
			System.out.println(splitByPlus[i]);
			int cnt = countPower(splitByPlus[i], tarVar);
			System.out.println(cnt);

			if(cnt == 0) {
				tempStr = "0";
			} else {
				int replaceIndex = splitByPlus[i].indexOf(tarVar);
				tempStr = splitByPlus[i];
				int powerOfTarVar = countPower(tempStr, tarVar);
				tempStr = tempStr.replaceFirst(tarVar, String.valueOf(powerOfTarVar));
			}
			if(i==0)
				derivedExp = tempStr;
			else
				derivedExp = derivedExp + "+" + tempStr;
		}
		System.out.println(merge(derivedExp));
		return 0;
	} 
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("-------------------------------------------\n");
		System.out.print("    (lanxuan & N0e1) All Right Reserved.   \n");
		System.out.print("-------------------------------------------\n");

		while(true) {
			System.out.print("\n>> ");
			input = in.nextLine(); 		// Current Expression;
			Matcher simMatcher = simPattern.matcher(input);
			Matcher derMatcher = derPattern.matcher(input);
			if(input.isEmpty()) {
				System.out.println("Shit!");
			} else if(input.charAt(0) != '!') {
				exp = input;
//				System.out.println("");
				exp = mergeBrackets(exp);
				System.out.println("等登等登" + exp);
				System.out.println(merge(exp));
				
				for(Map.Entry<String, String> entry:map.entrySet()){    
					 System.out.println(entry.getKey()+"--->"+entry.getValue());    
				}   
			} else {
				if(exp.isEmpty()) {
					System.out.println("No expressions available!");
					continue;
				}
				cmd = input;
				if(simMatcher.matches()) {
					if(simplify(exp, cmd) == -1) 
						System.out.println("Error! No variable: ");
				
				} else if(derMatcher.matches()) {
					if(derivative(exp, cmd) == -1) 
						System.out.println("Error! No vartiable.");
					
				} else {
					System.out.println("command not found: " + cmd);
				}				
			}
		} //End of while loop
	} //End of main
}