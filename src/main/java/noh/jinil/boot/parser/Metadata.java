package noh.jinil.boot.parser;

import java.util.ArrayList;
import java.util.Collection;

public class Metadata {
	private Collection<Tag> _definedTagList = new ArrayList<>();

	public Collection<Tag> getTagList() {
		return _definedTagList;
	}
	
	void setTag(String key, String object) {
		_definedTagList.add(new Tag(key, object));
	}

}
