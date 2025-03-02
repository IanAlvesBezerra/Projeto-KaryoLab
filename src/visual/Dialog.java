package visual;

import javax.swing.JFrame;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class Dialog extends JDialog {
	
	public Dialog(JFrame frame, JPanel panel) {
        super(frame, true);
        
        setSize(874, 614);  
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);

        setContentPane(panel);
        
        setVisible(false);
    }
	
	public Dialog(JFrame frame, JPanel panel, int width, int height) {
        super(frame, true);
        
        setSize(width, height);  
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);

        setContentPane(panel);
        
        setVisible(false);
    }
}