package com.ucdrive.refactorLater;

import java.util.*;

public class Folder {
	protected ArrayList<Folder> folders;
	protected String path;

	public Folder() {
		folders = new ArrayList<Folder>();
	}

	public Folder(String Path) {
		folders = new ArrayList<Folder>();
		this.path = Path;
	}

	// getters and setters
	public String getPath() {
		return path;
	}

	public void setPath(String newPath) {
		this.path = newPath;
	}

	public void addFolder(Folder new_folder) {
		folders.add(new_folder);
	}

	@Override
	public String toString() {
		String end = "";
		for (Folder f : folders) {
			end = end + f;
		}
		end += path + "\n";
		return end;
	}
}
