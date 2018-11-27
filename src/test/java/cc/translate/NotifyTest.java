package cc.translate;
import java.io.IOException;

import javax.imageio.ImageIO;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import dorkbox.util.ActionHandler;

public
class NotifyTest {

    public static
    void main(String[] args) throws IOException {
        Notify notify = Notify.create()
         .title("翻译" )
         .text("This is a notification " + 2 + "" +
               "notifi jkj kjkj kjkj kjk kjkj kjkjkk jkjj kj jkjkjkk jkkjkj  jk jkkjk kj jkj"
               + "lex gg gg g gg g")
         .hideAfter(5000)
         .position(Pos.TOP_RIGHT)
//     .setScreen(0)
         .darkStyle()
          .shake(1300, 4)
         .image(ImageIO.read(NotifyTest.class.getResource("/cc.gif")))
         
    //.hideCloseButton()
         .onAction(new ActionHandler<Notify>() {
             @Override
             public
             void handle(final Notify arg0) {
                 System.err.println("Notification 1 clicked on!");
             }
         });
 notify.show();

 }


    
}
