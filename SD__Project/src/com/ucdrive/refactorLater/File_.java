package com.ucdrive.refactorLater;

import java.util.*;
import java.io.*;
public class File_ extends Folder{

	public File_(){
		super.folders =  null;
	}
	public File_(String path){
		super.folders =  null;
		super.path = path;
	}

	@Override
	public String toString() {
		return  path + "\n";
	}
}
