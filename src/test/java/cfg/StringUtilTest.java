package cfg;

import com.towel.cfg.StringUtil;

public class StringUtilTest {
	private static void testRemoveIllegalCharacters() {
		String x = "m a r k y";
		String result = StringUtil.removeCharacters(x, ' ');
		System.out.println(result);
	}

	public static void main(final String[] args) {
		testRemoveIllegalCharacters();
	}
}
