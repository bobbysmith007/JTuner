// Stub.java
//
//    Part of Jtuner - program for tuning guitars and other instruments
//    Copyright (C) 2004  Michael Corlett
//    Email: jtuner@corlett.plus.com
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License Version 2 as
//    published by the Free Software Foundation;
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//

package jtuner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JApplet;
import javax.swing.JFrame;
public class Stub extends JApplet {

	private static JTuner tuner;

	public static void main(String [] args) {
		
		JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {System.exit(0);};		
			});

		f.getContentPane().add("Center", tuner = new JTuner());

		f.pack();
		f.setVisible(true);
	}

	public void init() {
		getContentPane().add("Center", tuner = new JTuner());

	}
	public void stop() {
		tuner.stop();
	}
		



}
