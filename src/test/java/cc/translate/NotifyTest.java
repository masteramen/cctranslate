package cc.translate;
/*
 * Copyright 2015 dorkbox, llc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import dorkbox.util.ActionHandler;
import dorkbox.util.ImageUtil;
import dorkbox.util.LocationResolver;
import dorkbox.util.ScreenUtil;
import net.coobird.thumbnailator.tasks.io.ImageSink;

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
