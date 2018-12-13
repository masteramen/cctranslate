package cc.translate.api.model;

import org.jetbrains.annotations.Nullable;

/**
 * Token Key Wrapper
 * 
 */
public class TokenKey {

    @Nullable
    private String key;

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }
}
