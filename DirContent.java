import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DirContent extends JFrame implements ActionListener {
	
	private JTextField txt = new JTextField("$HOME/");
	private JButton btnGo = new JButton("Display content of directory");
	private JTextArea txtOutput = new JTextArea(10, 20);
	
	public DirContent() {
		super("Display directory contents");
		setSize(400, 400);
		btnGo.addActionListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(txt, BorderLayout.NORTH);
		add(btnGo, BorderLayout.SOUTH);
		JScrollPane scroll = new JScrollPane(txtOutput);
		add(scroll, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		String dir = txt.getText();
        
        try {
        //Build the background process object and set stdout & stderr
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls "+dir);
        Process process = builder.start();
        InputStream stdout = process.getInputStream();
        InputStream stderr = process.getErrorStream();
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
        String line = null;
        
        //Deals with any errors being thrown by the ls command
        BufferedReader stderrBuffered = new BufferedReader(new InputStreamReader(stderr));
        while ((line = stderrBuffered.readLine()) != null ) {
            JOptionPane.showMessageDialog(null, line, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        //Deals with displaying the list of items
        txtOutput.setText(null);
        while ((line = stdoutBuffered.readLine()) != null ) 
        {
            txtOutput.append(line + "\n");
        }

        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
	}
	
	public static void main(String[] agrs){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
                //Initialise the frame, set centre location & make visible
                DirContent frame = new DirContent();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
			}
		});
	}
}