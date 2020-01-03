package com.schedek.curso.ejb.files;

import com.schedek.curso.ejb.AppConfig;
import com.schedek.curso.ejb.util.Security;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {

	public static String assignFilename(File f, String filename) {
		String ext = "", fn = filename, rfn;
		if (fn.contains(".")) {
			ext = fn.substring(fn.lastIndexOf("."));
			fn = fn.substring(0, fn.lastIndexOf("."));
		}

		fn = FileUtil.normalizeString(fn);

		File up;
		int cnt = 0;

		do {
			rfn = fn + (cnt == 0 ? "" : (" (" + cnt + ")")) + ext;
			up = new File(f, rfn);
			cnt++;
		} while (up.exists());
		return rfn;
	}

	public static String normalizeString(String s) {
		String rs = s.toLowerCase();
//		try {
//			byte[] bytes = s.getBytes("windows-1250");
//			rs = new String(bytes, "UTF-8");
//		} catch (UnsupportedEncodingException ex) {
//			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
//		}
		String src = "žščřďťňěáéíóúýů";
		//String src = "";
		String dst = "zscrdtneaeiouyu";
		for (int i = 0; i < src.length(); i++) {
			rs = rs.replaceAll(src.substring(i, i + 1), dst.substring(i, i + 1));
		}

		String decomposed = Normalizer.normalize(rs, Normalizer.Form.NFKD);
		/*
		 * Build a new String with only ASCII characters.
		 */
		StringBuilder buf = new StringBuilder();
		for (int idx = 0; idx < decomposed.length(); ++idx) {
			char ch = decomposed.charAt(idx);
			if (ch < 128) {
				buf.append(ch);
			}
		}
		String r = buf.toString();

		r = r.replaceAll("[^abcdefghijklmnopqrstuvwxyz1234567890]+", "-");
		r = r.replaceAll("--", "-");
		if (r.startsWith("-")) {
			r = r.substring(1);
		}
		if (r.endsWith("-")) {
			r = r.substring(0, r.length() - 1);
		}
		return r;
	}
	
		public static boolean checkFilename(AppConfig cfg,File img) {
        File f = new File(cfg.getRoot());
        return  (img.getAbsolutePath().startsWith(f.getAbsolutePath()));
    }


	public static boolean copyFile(InputStream is, File out) {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(out);
			copyInputStream(is, fos);
			return true;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			//Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ex) {
				Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return false;
	}

	public static void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		if (in == null) {
			return;
		}
		if (out == null) {
			throw new IOException("Target stream is null");
		}
		byte[] buffer = new byte[65535];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	public static final class StreamedFile {

		private String name;
		private InputStream stream;

		public StreamedFile(String name, InputStream stream) {
			this.name = name;
			this.stream = stream;
		}

		public String getName() {
			return name;
		}

		public InputStream getStream() {
			return stream;
		}

	}

	public static String getMD5str(File f) {
		byte[] md5 = getMD5(f);
		if (md5 == null) {
			return null;
		}
		return Security.hexEncode(md5);
	}

	public static byte[] getMD5(File f) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			InputStream is = new DigestInputStream(
					new BufferedInputStream(new FileInputStream(f.getAbsolutePath())), md);
			byte[] buffer = new byte[0x100];
			while (is.read(buffer) != -1);
			byte[] ret = md.digest();
			is.close();
			return ret;
		} catch (Exception ex) {
			Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
