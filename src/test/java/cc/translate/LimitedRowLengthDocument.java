package cc.translate;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class LimitedRowLengthDocument extends DefaultStyledDocument {

	private static final long serialVersionUID = -2059737037274062490L;

    private static final String EOL = "\n";
	
    private int max;
    private JTextArea ta = null;

    public LimitedRowLengthDocument(JTextArea ta, int max) {
    	this.ta = ta;
    	this.max = max;
    }

    public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException { 
    	
    	int actRow = ta.getLineOfOffset(offs);
    	int rowBeginn = ta.getLineStartOffset(actRow);
    	int rowEnd = ta.getLineEndOffset(actRow);
      	int referenceValue = 0;
      	
    	if (str.length() > 1) {
    		referenceValue = (rowEnd + str.length()) - rowBeginn;
    	} else {
    		referenceValue = rowEnd - rowBeginn;
    	}
    	
    	if (referenceValue >= max) {
        	if (str.length() > 1) {        		
        		StringBuffer str_buff = new StringBuffer();
        		for (int i=0; i<str.length(); i++) {
        			if (i >= max) {
        				str_buff.append(EOL);
        				str_buff.append(str.charAt(i));
        				str = str.substring(i, str.length());
        				i = 0;
        			} else {
        				str_buff.append(str.charAt(i));
        			}
        		}        		
        		str = str_buff + EOL;
        	} else {
        		str = EOL + str;
        	}    		
    	}    	
    	super.insertString(offs, str, attribute);
    }
}