package cc.translate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cc.translate.api.Translate;
import cc.translate.api.exception.IllegalTokenKeyException;
import cc.translate.api.exception.RetrieveTokenKeyFailedException;
import cc.translate.api.model.TranslateResult;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class TestPlayMp3 {
public static void main(String[] args) throws JavaLayerException, RetrieveTokenKeyFailedException, IllegalTokenKeyException, IOException {
	
    File file = new File("/Users/alexwang/Downloads/translate/src/test/java/cc/translate/test.mp3");  

	final Translate translate = new Translate.Builder().logLevel(Translate.LogLevel.NONE).build();

	InputStream is = translate.getAuidoUrl("hello","en");
    
    BufferedInputStream buffer = new BufferedInputStream(is);
    Player player = new Player(buffer);
    player.play();

}
}
