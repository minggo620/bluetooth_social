package edu.minggo.chat.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import edu.minggo.chat.R;
import edu.minggo.chat.model.Smiley;
/**
 * 解析表情图片
 * @author minggo
 * @created 2013-2-18下午02:30:11
 */
public class SmileyParser {
	private static SmileyParser instance;

	public static SmileyParser getInstance() {
		return instance;
	}

	public static void init(Context context) {
		instance = new SmileyParser(context);
	}

	private static Context context;
	private static String[] smileyTexts;
	private static Pattern pattern;
	private static HashMap<String, Integer> smileyMap;

	@SuppressWarnings("static-access")
	private SmileyParser(Context context) {
		this.context = context;
		this.smileyTexts = context.getResources().getStringArray(
				R.array.default_smiley_name);
		this.smileyMap = buildSmileyToRes();
		this.pattern = buildPattern();
	}

	private HashMap<String, Integer> buildSmileyToRes() {
		System.out.println((Smiley.QQSIconIds.length+Smiley.EmjiIconIds.length)+"====="+smileyTexts.length);
		if ((Smiley.QQSIconIds.length+Smiley.EmjiIconIds.length) != smileyTexts.length) {
			throw new IllegalStateException("Smiley resource ID/text mismatch");
		}

		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(
				smileyTexts.length);
		for (int i = 0; i < smileyTexts.length; i++) {
			if(i<110)
			smileyToRes.put(smileyTexts[i], Smiley.QQSIconIds[i]);
			else if(110<=i&&i<230)
			smileyToRes.put(smileyTexts[i], Smiley.EmjiIconIds[i-110]);
		}

		return smileyToRes;
	}

	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(smileyTexts.length * 3);
		patternString.append('(');
		for (String s : smileyTexts) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");
		return Pattern.compile(patternString.toString());
	}

	public CharSequence addSmileySpans(CharSequence text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			int resId = smileyMap.get(matcher.group());
			
			Drawable d = context.getResources().getDrawable(resId);
			d.setBounds(0, 0, 32, 32);
			ImageSpan span = new ImageSpan(d, matcher.group() + ".gif",
					ImageSpan.ALIGN_BOTTOM);
			
			//ImageSpan isp=new ImageSpan(context, resId);
			//isp.getDrawable().setBounds(0, 0, 10, 10);;
			builder.setSpan(span, matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
	
	
	public CharSequence addSmileySpansx(CharSequence text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			int resId = smileyMap.get(matcher.group());
			builder.setSpan(new ImageSpan(context, resId), matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}
