package tp.ejb.utils;

/* Iso2utf.java Created on 13. April 2005, 11:52 */
/* http://www.jspwiki.org/attach/UTF8Issues/Iso2utf.java */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/** @author lv26174 */

public class Iso2utf {

	private static final String NL = System.getProperty("line.separator"),
			FS = System.getProperty("file.separator"),
			QT = "\"",
			SRC = "SOURCE",
			DST = "DESTINATION",
			FAILED = "failed:",
			FAILED_TXT = "failed in covertion txt:",
			FAILED_COPY = "failed in copying:",
			FAILED_TXT_FILE = "failed in covertion txt file:",
			FAILED_COPY_FILE = "failed in copying file:",
			VERSION = "Version 0.9.1",
			SP = " ",
			DEF_IN_ = "/home/francois/devl/ws-helios-3/bank-ejb/ejbModule",
			DEF_IN = "D:\\Apps\\jee2010\\workspace\\webankejb\\src",
	        DEF_OUT_ = "/home/francois/devl/ws-helios-3/bank-ejb/converted",
	        DEF_OUT = "D:\\Apps\\jee2010\\workspace\\webankejb\\srcconv",
			CANNOT_FIND = "cannot find", CANNOT_DELETE = "cannot delete",
			NO_DIR = "not a directory", NO_ACCESS = "cannot access",
			CANNOT_MAKE = "cannot make", HEX_DIGITS = "0123456789ABCDEF",
			ISO88591 = "ISO-8859-1", UTF8 = "UTF8",
			BAD_UTF8 = "Malformed UTF-8 string?:", ZZZ = "ZZZ";

	private static final int SIZE = 256 * 1024;
	private static byte[] ib = new byte[SIZE];

	/** Creates a new instance of RenamePdfOut */
	public Iso2utf() {
	}

	/** run rename pdf out */
	public static void main(String[] args) {
		Iso2utf iso2utf = new Iso2utf();
		try {
			String s = "";
			for (int k = 0; k < args.length; k++)
				s = s + SP + args[k];
			System.out.println(VERSION + SP + s);
			if (args.length >= 2)
				iso2utf.process(new File(args[0]), new File(args[1]));
			else
				iso2utf.process(new File(DEF_IN), new File(DEF_OUT));
		} catch (Throwable t) {
			System.out.println(FAILED + t);
		} finally {
		}
	}

	private void process(File inDir, File outDir) {
		try {
			if (!inDir.exists()) {
				System.out.println(CANNOT_FIND + SP + inDir.getCanonicalPath());
				return;
			}
			if (!inDir.isDirectory()) {
				System.out.println(NO_DIR + SP + inDir.getCanonicalPath());
				return;
			}
			if (!inDir.canRead()) {
				System.out.println(NO_ACCESS + SP + inDir.getCanonicalPath());
				return;
			}
			if (outDir.exists())
				if (!outDir.delete()) {
					System.out.println(CANNOT_DELETE + SP
							+ outDir.getCanonicalPath());
					return;
				}
			if (!outDir.mkdirs()) {
				System.out
						.println(CANNOT_MAKE + SP + outDir.getCanonicalPath());
				return;
			}
			this.convert__(inDir.listFiles(), outDir);
		} catch (Throwable t) {
			System.out.println(FAILED + t);
		}
	}

	private void convert__(File[] listFiles, File outDir) throws IOException {
		String name, iso;
		File out;
		int sz;
		FileInputStream rd;
		FileOutputStream wr;
		for (File f : listFiles) {
			name = this.translateFilename(f.getName());
			out = new File(outDir.getCanonicalPath() + FS + name);
			if (f.isDirectory()) {
				if (out.mkdir())
					this.convert__(f.listFiles(), out);
				else
					System.out.println(CANNOT_MAKE + SP + f.getCanonicalPath()
							+ FS + name);
			}
			else if (f.getName().endsWith(".txt") || f.getName().endsWith(".java")) {
				try {
					rd = new FileInputStream(f);
					wr = new FileOutputStream(out);
					try {
						sz = rd.read(this.ib);
						iso = new String(this.ib, 0, sz, ISO88591);
						wr.write(iso.getBytes(UTF8));
					} catch (Throwable t) {
						System.out.println(FAILED_TXT + t);
					}
					wr.close();
					rd.close();
				} catch (Throwable t) {
					System.out.println(FAILED_TXT_FILE + t);
				}
			}
			else {
				try {
					rd = new FileInputStream(f);
					wr = new FileOutputStream(out);
					sz = SIZE;
					while (sz == SIZE) {
						try {
							sz = rd.read(this.ib);
							if (sz > 0)
								wr.write(this.ib, 0, sz);
						} catch (Throwable t) {
							System.out.println(FAILED_COPY + t);
						}
					}
					wr.close();
					rd.close();
				} catch (Throwable t) {
					System.out.println(FAILED_COPY_FILE + SP
							+ f.getCanonicalPath() + " " + t);
				}
			}
		}
	}

	private void convert_(File[] listFiles, File outDir) throws IOException {

		String name, iso;
		File out;
		int sz;
		FileInputStream rd;
		FileOutputStream wr;
		for (File f : listFiles) {
			name = this.translateFilename(f.getName());
			out = new File(outDir.getCanonicalPath() + FS + name);
			if (f.isDirectory()) {
				if (out.mkdir())
					this.convert_(f.listFiles(), out);
				else
					System.out.println(CANNOT_MAKE + SP + f.getCanonicalPath()
							+ FS + name);
			} else if (f.getName().endsWith(".txt")) {
				try {
					rd = new FileInputStream(f);
					wr = new FileOutputStream(out);
					try {
						sz = rd.read(this.ib);
						iso = new String(this.ib, 0, sz, ISO88591);
						wr.write(iso.getBytes(UTF8));
					} catch (Throwable t) {
						System.out.println(FAILED_TXT + t);
					}
					wr.close();
					rd.close();
				} catch (Throwable t) {
					System.out.println(FAILED_TXT_FILE + t);
				}
			} else {
				try {
					rd = new FileInputStream(f);
					wr = new FileOutputStream(out);
					sz = SIZE;
					while (sz == SIZE) {
						try {
							sz = rd.read(this.ib);
							if (sz > 0)
								wr.write(this.ib, 0, sz);
						} catch (Throwable t) {
							System.out.println(FAILED_COPY + t);
						}
					}
					wr.close();
					rd.close();
				} catch (Throwable t) {
					System.out.println(FAILED_COPY_FILE + SP
							+ f.getCanonicalPath() + " " + t);
				}
			}
		}

	}

	private String translateFilename(String text) {
		String r = null;
		try {
			r = this.urlEncode(this.urlDecode(text.getBytes(ISO88591))
					.getBytes(UTF8));
		} catch (Throwable t) {
			System.out.println(FAILED + t);
		} finally {
		}
		return r;
	}

	/**
	 * URL encoder does not handle all characters correctly. See <A HREF=
	 * "http://developer.java.sun.com/developer/bugParade/bugs/4257115.html">
	 * Bug parade, bug #4257115</A> for more information.
	 * <P>
	 * Thanks to CJB for this fix.
	 */
	private String urlDecode(byte[] bytes) {
		String processedPageName = null;
		try {
			if (bytes != null) {
				byte[] decodeBytes = new byte[bytes.length];
				int decodedByteCount = 0;
				try {
					for (int count = 0; count < bytes.length; count++) {
						switch (bytes[count]) {
						case '+':
							decodeBytes[decodedByteCount++] = (byte) ' ';
							break;
						case '%':
							decodeBytes[decodedByteCount++] = (byte) ((HEX_DIGITS
									.indexOf(bytes[++count]) << 4) + (HEX_DIGITS
									.indexOf(bytes[++count])));
							break;
						default:
							decodeBytes[decodedByteCount++] = bytes[count];
						}
					}
				} catch (IndexOutOfBoundsException ae) {
					System.out.println(BAD_UTF8 + ae);
				}
				processedPageName = new String(decodeBytes, 0,
						decodedByteCount, "ISO-8859-1");
			}
		} catch (Throwable t) {
			System.out.println(FAILED + t);
		} finally {
		}
		return (processedPageName.toString());
	}

	/**
	 * java.net.URLEncoder.encode() method in JDK < 1.4 is buggy. This
	 * duplicates its functionality.
	 */
	protected static String urlEncode(byte[] rs) {
		StringBuffer result = new StringBuffer();
		try {
			for (int i = 0; i < rs.length; i++) {
				char c = (char) rs[i];
				switch (c) {
				case '_':
				case '.':
				case '*':
				case '-':
				case '/':
					result.append(c);
					break;
				case ' ':
					result.append('+');
					break;
				default:
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
							|| (c >= '0' && c <= '9'))
						result.append(c);
					else {
						result.append('%');
						result.append(HEX_DIGITS.charAt((c & 0xF0) >> 4));
						result.append(HEX_DIGITS.charAt(c & 0x0F));
					}
				}
			}
		} catch (Throwable t) {
			System.out.println(FAILED + t);
		} finally {
		}
		return result.toString();
	}

}