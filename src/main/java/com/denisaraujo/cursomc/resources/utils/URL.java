package com.denisaraujo.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String str){
		String[] vet = str.split(",");
		List<Integer> list = new ArrayList<>();

		//Método antigo
//		for (int i = 0; i < vet.length; i++) {
//			list.add(Integer.parseInt(vet[i]));
//		}
		
		//usando lâmbda java 8
		return Arrays.asList(str.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
	
}
