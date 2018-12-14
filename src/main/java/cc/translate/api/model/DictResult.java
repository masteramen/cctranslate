package cc.translate.api.model;

import org.jetbrains.annotations.Nullable;

/**
 * Token Key Wrapper
 * 
 */
public class DictResult {

    @Nullable
    private String word;
    @Nullable
    private String content;
	public String getWord() {
		return word;
	}
	public void setWord(@Nullable String word) {
		this.word = word;
	}
	public String getContent() {
		return content;
	}
	public void setContent(@Nullable String content) {
		this.content = content;
	}
    


}
