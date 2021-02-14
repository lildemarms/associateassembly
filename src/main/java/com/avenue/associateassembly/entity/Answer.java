package com.avenue.associateassembly.entity;

public enum Answer {

	NO("No"), 
	YES("Yes");

	private String answer;

	Answer(String answer) {
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
