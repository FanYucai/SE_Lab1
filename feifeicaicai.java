import java.util.*;
import java.util.regex.*;
import java.security.MessageDigest;

class feifeicaicai {
	static String exp = "";
	static String input = "";
	static String cmd = "";
	
	static Pattern simPattern = Pattern.compile("^!simplify ([a-zA-Z]+=[0-9]+[\\s]*)+$");
	static Pattern simSpecPattern = Pattern.compile("^!simplify\\s*$");
	static Pattern simSplit = Pattern.compile("[\\s=]+");
	static Pattern derPattern = Pattern.compile("^!d/d[a-zA-Z]+$");
	static Pattern plusSplit = Pattern.compile("[+]");
	static Pattern mulSplit = Pattern.compile("[*]");
	static Pattern bracketsPattern = Pattern.compile("([a-zA-Z0-9]+\\*)*\\([a-zA-Z0-9*+-]+\\){1}(\\*[a-zA-Z0-9]+)*");
	static Pattern naiveBrPattern = Pattern.compile("\\([a-zA-Z0-9*+-^]+\\)");
	static Pattern specBrPattern = Pattern.compile("\\)\\*\\(");
	
	static String[] symbols = new String[100];
	static String[] elements = new String[100];
	static String[] symbolsOut = new String[100];
	static String[] elementsOut = new String[100];
	static String[] splitByPlus;
	static String[] splitByMul;
		
	public static String combin(String[] strs, int [] num, int strsLen){
		String [] hash = new String[1000];
		int [] cnt = new int [1000];
		int [] pos = new int [1000];
		int p=0;
		
//		for (int i=0;i<strsLen;i++)
//			System.out.println(strs[i] + "-->" + String.valueOf(num[i]) );
//		System.out.println("Len = " + strsLen);
		
		for (int i=0;i<strsLen;i++){ 
			int x=Math.abs(strs[i].hashCode())%953;
			while (hash[x]!=null){
				if (hash[x].equals(strs[i])) 
					break;
				else x+=3;
			}
			if (hash[x]==null) {
				hash[x]=strs[i];
				cnt[x]=num[i];
				pos[p]=x;
				p++;
			} else
				cnt[x]+=num[i];
		}

		String ret="";
		for (int i=0;i<p;i++){
			if (cnt[pos[i]]==1)
				ret=ret+"+"+hash[pos[i]];
			else
				ret=ret+"+"+String.valueOf(cnt[pos[i]])+"*"+hash[pos[i]];
		}
		return ret;
	}
	
	public static String getCommon(String exp) {
		if(exp.indexOf("+") == -1) 
			return exp; 
		String resultStr = "";
		String[] charMul = new String[1000];
		String[] digitMul = new String[1000];
		int charMulCnt = 0;
		int digitMulCnt = 0;
		String offset_HAHAHA = "0";
		
		Pattern patternDigitPlus = Pattern.compile("[0-9]+\\+");
		Matcher digitPlusMatcher = patternDigitPlus.matcher(exp);
		if(digitPlusMatcher.find()) {
			offset_HAHAHA = digitPlusMatcher.group(0);
			offset_HAHAHA = offset_HAHAHA.substring(0, offset_HAHAHA.length()-1);
			exp = exp.replace(digitPlusMatcher.group(0), "");
		}
		Pattern digitPattern = Pattern.compile("[0-9]+");
		Pattern pattern = Pattern.compile("([a-zA-Z]+\\*)*[a-zA-Z]+");
//		Matcher matcher = Matcher.matches(exp);
		splitByPlus = plusSplit.split(exp);
		for(int i=0; i<splitByPlus.length; i++) {
			String mulStr = splitByPlus[i];
			Matcher digitMatcher = digitPattern.matcher(mulStr);
			Matcher charMatcher = pattern.matcher(mulStr);
			boolean ifCh;
			if(digitMatcher.find()) 
				digitMul[digitMulCnt++] = digitMatcher.group(0);
				//System.out.println("wqw "+digitMatcher.group(0));
			else 
				digitMul[digitMulCnt++] = "1";
				
			if(charMatcher.find())
				charMul[charMulCnt++] = charMatcher.group(0);
				//System.out.println("qwq "+charMatcher.group(0));
		}
		
//			System.out.println("Offset = " + offset_HAHAHA);
//			for(int i=0; charMul[i] != null; i++) {
//				System.out.println("qwq: " + charMul[i]);
//			}
//			for(int i=0; digitMul[i] != null; i++) {
//				System.out.println("qqq: " + digitMul[i]);
//			}
//		
		int[] nums = new int[1000];

		for(int i=0; i<digitMulCnt; i++) {
			nums[i] = Integer.parseInt(digitMul[i]); 		
		}
		resultStr = combin(charMul, nums, digitMulCnt);
		if(offset_HAHAHA != "0")
			resultStr = offset_HAHAHA + resultStr;
		else 
			resultStr = resultStr.substring(1);
		return resultStr;
	}
		
	public static String merge(String simplifiedExp) {
		String outAns="";
		splitByPlus = plusSplit.split(simplifiedExp);
		for(int i=0; i<splitByPlus.length; i++) {
			splitByMul = mulSplit.split(splitByPlus[i]);
			Arrays.sort(splitByMul);
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
			if (mulsAns!=0)
			outAns=outAns+"+"+outTmp;
		}
		if (outAns.length()==0) outAns="0";else
		outAns=outAns.substring(1,outAns.length());
		
		String[] plusAnsSplit= plusSplit.split(outAns); 

		int plusAns=0;
		boolean flag=false;
		boolean flagVar=false;
		outAns="";
		for (int i=0;i<plusAnsSplit.length;i++)
			if (isNumeric(plusAnsSplit[i])){
				flag=true;
				//System.out.println(plusAnsSplit[0]+"aaa");
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
	
		outAns = getCommon(outAns);
		return outAns;
	}	

	public static boolean isNumeric(String str){
		for (int i=0;i<str.length();i++)
		if (!Character.isDigit(str.charAt(i)))
			return false;
		return true;
	}
	
	public static String unfoldBrPower(String exp){
		String resultStr = "";
		Pattern powerBrPattern = Pattern.compile("\\([0-9a-zA-Z]\\)\\^[0-9]+");
		
		return resultStr;
	}
	
	public static String digitCharSplit(String exp) {
		Pattern pattern = Pattern.compile("[0-9]+[a-zA-Z]+");
		Pattern numericPattern = Pattern.compile("[0-9]+");
		Matcher matcher  = pattern.matcher(exp);
		Matcher numericMatcher;
		while(matcher.find()) {
			//System.out.println(matcher.group(0));
			String tempStr = matcher.group(0);
			
			numericMatcher = numericPattern.matcher(tempStr);
			if(numericMatcher.find()) {
				String charStr = tempStr;
				String numericStr = numericMatcher.group(0);
//				System.out.println(numericStr);
				charStr = charStr.replace(numericStr, "");
//				System.out.println(charStr);	
				String resultStr = numericStr + "*" + charStr;
				exp = exp.replace(tempStr, resultStr);			}
		}
		return exp;
	}

	public static String specBrPower(String exp) {
		String inBr, res = "";
		String totBrPower;
		String powerStr;
		int powerCnt = 0, leftStack = 1, rightIndex = 0, leftIndex = 0;
		Pattern specBrPattern = Pattern.compile("\\)\\^");
//		Matcher specMatcher = specPattern.matcher(exp);
		int index = exp.indexOf(")^");
		rightIndex = index+2;
		leftIndex = index-1;
		leftStack = 1;
		while(rightIndex < exp.length() && Character.isDigit(exp.charAt(rightIndex))) {
			rightIndex++;
		}
		
		while(leftIndex >= 0 && leftStack != 0) {
			if(exp.charAt(leftIndex) == ')')
				leftStack++;
			if(exp.charAt(leftIndex) == '(')
				leftStack--;
			leftIndex--;
			//System.out.println("index = " + String.valueOf(leftStack));
		}
		
		totBrPower = exp.substring(leftIndex+1, rightIndex);
		
		System.out.println("totBrPower" + totBrPower);
		
		powerStr = totBrPower.split("\\^")[1];
		inBr = totBrPower.split("\\^")[0];
		powerCnt = Integer.parseInt(powerStr); 
		
		for(int i=0; i<powerCnt-1; i++) {
			res += inBr + "*";
		}
		res += inBr;
		
		exp = exp.replace(totBrPower, res);
		System.out.println("res="+exp);
	
		if(specBrPattern.matcher(exp).find()) 
			exp = specBrPower(exp);
		return exp;
	}
	
	public static String dealIndex(String exp){
		Pattern pattern = Pattern.compile("[a-zA-Z]+[\\s]*\\^[\\s]*[0-9]+");
		Matcher matcher  = pattern.matcher(exp);
		Pattern specPattern = Pattern.compile("\\)\\^");
		Matcher specMatcher = specPattern.matcher(exp);
		if(specMatcher.find()) 
			exp = specBrPower(exp);
		
		while(matcher.find()) {
			//System.out.println(matcher.group(0));
			String tempStr = matcher.group(0);
			String[] result = tempStr.split("[\\s]*[\\^]+[\\s]*");
			//System.out.println(result[0]);
			//int p=1;
			int p=Integer.parseInt(result[1]);
			String rep=result[0];
			for (int i=1;i<p;i++)
				rep=rep+'*'+result[0];
			//System.out.println(rep);
			exp=exp.replace(tempStr, rep);
		}
		return exp;
	}
	
	public static int simplify(String exp, String cmd) {
		String simplifiedExp=exp;
		String[] strs = simSplit.split(cmd);
		for(int i=1; i<strs.length; ) {
//			if((simplifiedExp = simplifiedExp.replaceAll(strs[i++], strs[i++])) != exp) {
//				
////				System.out.println("qweqweqw"+simplifiedExp);
//			} else {
//				System.out.println("Existing unavailable syntax.");
//				return -1;
//			}
			if((simplifiedExp = simplifiedExp.replaceAll(strs[i++], strs[i++])) == exp) 
				System.out.println("Existing unavailable syntax."); 
		}
		System.out.println(merge(simplifiedExp));		
		return 0;
	}	
	
	public static String fraction(String para, String inBrackets) {
		String resultStr = "";
		String[] elements = plusSplit.split(inBrackets);
		for(int i=0; i<elements.length-1; i++) {
			resultStr += para + "*" + elements[i] + "+";
		}
		resultStr += para + "*" + elements[elements.length-1];
		return resultStr;
	}
	
	public static String deleteBrackets(String exp) {
		String resultStr = "";
		//System.out.println(exp);
		Matcher naiveBrMatcher = naiveBrPattern.matcher(exp);
		naiveBrMatcher.find();
		resultStr = naiveBrMatcher.group(0);
//		System.out.println("括号里面的内容：" + resultStr);
//		Matcher plusForExp = plusSplit.matcher(exp);
		exp = exp.replaceAll("\\([a-zA-Z0-9*+-^]+\\)", "1");
		exp = merge(exp);
//		System.out.println("括号之外的系数：" + exp);
		
		resultStr = merge(resultStr.substring(1, resultStr.length()-1));
		resultStr = merge(fraction(exp, resultStr));
//		System.out.println("去括号之后: " + resultStr);
		return resultStr;
	}
	
	public static String mergeBrackets(String exp) {
		String resultStr = "";
		while(true) {
			Matcher bracketsMatcher = bracketsPattern.matcher(exp);
			if(bracketsMatcher.find()) {
				exp = exp.replace(bracketsMatcher.group(0), 
				deleteBrackets(bracketsMatcher.group(0)));
				//System.out.println(exp);
			} else 
				break;
		}
		return resultStr = exp;
	}
	
	public static String mergeSpecBrackets(String exp) {
		//System.out.println("xxx: "+exp);
		String resultStr = "";
		int index = exp.indexOf(")*(");

		int ltIt = index-1, rtIt = index+3;
		int leftStack = 1, rightStack = 1;
		while(ltIt >= 0 && leftStack != 0) {
			if(exp.charAt(ltIt) == '(')
				leftStack--;
			else if(exp.charAt(ltIt) == ')')
				leftStack++;
			ltIt--;
		}
		
		while(rtIt < exp.length() && rightStack != 0) {
			if(exp.charAt(rtIt) == '(')
				rightStack++;
			else if(exp.charAt(rtIt) == ')')
				rightStack--;
			rtIt++;
		}		
		
		String handleExp = exp.substring(ltIt+1, rtIt);
		String firstBr, secondBr;
		String[] firstSplit = new String[1000];
//		System.out.println("HandleExp = " + handleExp);
		int index2 = handleExp.indexOf(")*(");
//		for(int i=0; i<handleExp; i++) {
//			
//		}
		firstBr = handleExp.substring(1, index2);
		secondBr = handleExp.substring(index2+2);
		
		String firstRes = "";
//		System.out.println("handleExp = " + handleExp);
//		System.out.println("firstBr = " + firstBr);
//		System.out.println("secondBr = " + secondBr);
		rightStack = 0;
		int plusIndex = 0, preIndex = 0;
		int firstSplitCnt = 0;
		String tempFirstBr = firstBr;
		
		for(int i=0; i<tempFirstBr.length(); i++) {
			//System.out.println("tempFirstBr[i] = " + tempFirstBr.charAt(i));
			if(rightStack == 0 && tempFirstBr.charAt(i) == '+') {
//				plusIndex = i;
				firstSplit[firstSplitCnt++] = tempFirstBr.substring(0, i);
				tempFirstBr = tempFirstBr.substring(i+1); // after '+';
				i = 0;
				rightStack = 0;
				continue;
			}
			if(firstBr.charAt(i) == '(')
				rightStack++;
			if(firstBr.charAt(i) == ')')
				rightStack--;
		}
		
		firstSplit[firstSplitCnt++] = tempFirstBr.substring(0, tempFirstBr.length());
		
//		firstSplit = firstBr.split("\\+"); //wrong
//		for(int i=0; i<firstSplitCnt; i++) {
//			//firstRes = firstSplit[i] + "*" + secondBr + "+";
//			System.out.println("---->? " + firstSplit[i]);
//		}
		
		for(int i=0; i<firstSplitCnt-1; i++) 
			firstRes += firstSplit[i] + "*" + secondBr + "+";
	
		firstRes += firstSplit[firstSplitCnt-1] + "*" + secondBr;
		firstRes = "(" + firstRes + ")";
		exp = exp.replace(handleExp, firstRes);
		//System.out.println("1234567: " + exp);
		
		if(specBrPattern.matcher(exp).find()) 
			exp = mergeSpecBrackets(exp);
		else if(naiveBrPattern.matcher(exp).find())
			exp = mergeBrackets(exp);
		return exp;
	}

	public static int countPower(String expFrag, String tarVar) {
		int count = 0;
		int index = 0;
		while ((index = expFrag.indexOf(tarVar,index)) != -1) {
			expFrag = expFrag.substring(index + tarVar.length());
			count++;
			index = 0;
		}
		return count;
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
			//System.out.println(splitByPlus[i]);
			int cnt = countPower(splitByPlus[i], tarVar);
//			System.out.println("cnt = " + cnt + "for: " + splitByPlus[i] + "-->"+ tarVar);

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
	
	public static String init(String exp) {
	
		exp = dealIndex(exp);
		
		if(specBrPattern.matcher(exp).find()) 
			exp = mergeSpecBrackets(exp);
		else if(naiveBrPattern.matcher(exp).find())
			exp = mergeBrackets(exp);
		
		exp = digitCharSplit(exp);
		exp = merge(exp);
		return exp;
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("-------------------------------------------\n");
		System.out.print("   (lanxuan & N0e1) All rights preserved.  \n");
		System.out.print("-------------------------------------------\n");

		while(true) {
			System.out.print("\n>> ");
			input = in.nextLine(); 		// Current Expression;
			Matcher simMatcher = simPattern.matcher(input);
			Matcher derMatcher = derPattern.matcher(input);
			Matcher simSecMatcher = simSpecPattern.matcher(input);
			if(input.isEmpty()) {
				System.out.println("Shit!");
			} else if(input.charAt(0) != '!') {
				exp = input;
				exp = exp.replaceAll("\\s", "");
				Matcher specBrMatcher = specBrPattern.matcher(exp);

				exp = init(exp);
				System.out.println(merge(exp));
			} else {
				if(exp.isEmpty()) {
					System.out.println("No expressions available!");
					continue;
				}
				cmd = input;
				if(simSecMatcher.matches()) {
					System.out.println(exp);
				} else if(simMatcher.matches()) {
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