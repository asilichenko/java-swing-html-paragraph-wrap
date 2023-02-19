/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package ua.in.asilichenko;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

import static javax.swing.text.html.HTML.Tag.IMPLIED;
import static javax.swing.text.html.HTML.Tag.P;

public class HtmlWrapTest extends JFrame {

    public static void main(String[] args) {
        final HtmlWrapTest frame = new HtmlWrapTest();
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public HtmlWrapTest() {
        // disallow parent scroll bar pane to use horizontal scrollbar
        final JEditorPane editorPane = new JEditorPane() {
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        editorPane.setEditable(false);

        editorPane.setEditorKit(new CustomHTMLEditorKit());
        editorPane.setText("<html>" +
                "<body>" +
                "<p>ppppppppppppppppppppppppppppppppppppppppppppppp</p>" +
                "<p>long text line long text line long text line long text line long text line long text line</p>" +
                "<pre>preprepreprepreprepreprepreprepreprepreprepre</pre>" +
                "<pre>pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre pre</pre>" +
                "<br/><br/><br/>" +
                "</body>" +
                "</html>"
        );

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(editorPane);
        getContentPane().add(scrollPane);
    }
}

class CustomHTMLEditorKit extends HTMLEditorKit {

    private final ViewFactory viewFactory = new HTMLFactory() {
        @Override
        public View create(Element elem) {
            AttributeSet attrs = elem.getAttributes();
            Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
            Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
            if (o instanceof Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (IMPLIED == kind) return new WrappableParagraphView(elem); // <pre>
                if (P == kind) return new WrappableParagraphView(elem); // <p>
            }
            return super.create(elem);
        }
    };

    @Override
    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }
}

class WrappableParagraphView extends javax.swing.text.html.ParagraphView {

    public WrappableParagraphView(Element elem) {
        super(elem);
    }

    @Override
    public float getMinimumSpan(int axis) {
        return View.X_AXIS == axis ? 0 : super.getMinimumSpan(axis);
    }
}
