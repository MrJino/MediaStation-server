package noh.jinil.boot.parser;

public class Tag {
	private String name;
	private String description;
	
	Tag(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getTagName() {
		return this.name;
	}
	
	public String getTagDescription() {
		return this.description;
	}
}
