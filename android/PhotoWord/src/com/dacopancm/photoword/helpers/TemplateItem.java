package com.dacopancm.photoword.helpers;

public class TemplateItem {
	private String text1;
	private String text2;
	private int graphic;

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public int getGraphic() {
		return graphic;
	}

	public void setGraphic(int graphic) {
		this.graphic = graphic;
	}

	public TemplateItem(String text1, String text2, int graphic) {
		super();
		this.text1 = text1;
		this.text2 = text2;
		this.graphic = graphic;
	}

}
