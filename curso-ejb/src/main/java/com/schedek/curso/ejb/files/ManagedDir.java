package com.schedek.curso.ejb.files;

import java.io.File;

public class ManagedDir {

	private File file;
	private ManagedDir parent;

	ManagedDir(File file, ManagedDir parent) {
		this.file = file;
		this.parent = parent;
	}

	public File getFile() {
		return file;
	}

	public ManagedDir getParent() {
		return parent;
	}

	public String getPath() {
		String ret = file.getName();
		ManagedDir d = this;
		while ((d = d.parent) != null) {
			ret = d.file.getName() + "/" + ret;
		}
		return ret;
	}

}
