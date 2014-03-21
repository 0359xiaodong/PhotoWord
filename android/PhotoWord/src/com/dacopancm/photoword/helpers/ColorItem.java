package com.dacopancm.photoword.helpers;

public class ColorItem {
	private int c;
	private int colorfill;
	private String name;

	public ColorItem(int c, int colorfill, String name) {
		super();
		this.c = c;
		this.colorfill = colorfill;
		this.name = name;
	}

	public int getColorfill() {
		return colorfill;
	}

	public void setColorfill(int colorfill) {
		this.colorfill = colorfill;
	}

	public ColorItem() {
		// TODO Auto-generated constructor stub
	}

	public ColorItem(int c, String name) {
		this.c = c;
		this.name = name;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
