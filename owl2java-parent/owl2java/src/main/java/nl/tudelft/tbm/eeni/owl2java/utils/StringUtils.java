package nl.tudelft.tbm.eeni.owl2java.utils;

public class StringUtils {

	public static String headerStr = "########################################################################";
	public static String preStr = "#### ";
	public static String indent = "  ";

	protected static String indentNextLines(String text, String indent) {
		StringBuilder ret = new StringBuilder();
		String[] lines = text.split("\\n");

		for (int i = 0; i < lines.length; i++) {
			ret.append(indent).append(lines[i]);
			if (i < lines.length - 1 || text.endsWith("\n")) {
				ret.append("\n");
			}
		}
		return ret.toString();
	}

	public static String indentText(String text) {
		text = indentNextLines(text, indent);
		return text;
	}

	public static String indentText(String text, int level) {
       return indentNextLines(text, String.valueOf(StringUtils.indent).repeat(Math.max(0, level)));
	}

	public static String toFirstLowerCase(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	public static String toFirstUpperCase(String string) {
		if (!string.isEmpty())
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		return string;
	}
	public static String toHeader(String text) {
        return "\n" + StringUtils.headerStr + "\n" + StringUtils.preStr + text + "\n" + StringUtils.headerStr
                + "\n";
	}
	public static String toSubHeader(String text) {
        return "\n" + StringUtils.preStr + text + "\n";
	}

}
