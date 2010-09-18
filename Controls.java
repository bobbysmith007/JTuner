// Controls.java
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

import java.util.Hashtable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.Box;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import javax.swing.ImageIcon;

public class Controls extends JPanel implements ActionListener {

	private boolean on = false;
	private int rate = 48000;
	private int channels = 1;
	private boolean useRightChannel = true;

	private JRadioButton left = null;
	private JRadioButton right = null;

	private JRadioButton mono = null;
	private JRadioButton stereo = null;

	private RateSlider rateSlider = null;
	private OnOffButton onOffButton = null;

	private static final int HEIGHT = 40;

	public Controls() {

		setOpaque(false);
		setPreferredSize(new Dimension(400, HEIGHT));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		MonoStereoPanel msP = new MonoStereoPanel();
		LeftRightPanel lrP = new LeftRightPanel();
		rateSlider = new RateSlider();

		/*
		double y;
		double x;
		Dimension d = msP.getPreferredSize();
		x = d.getWidth();
		d = rateSlider.getPreferredSize();
		y = d.getHeight();
		msP.setPreferredSize(new Dimension((int)x, (int)y));
		*/
		add(msP);
		add(lrP);
		add(rateSlider);

		JLabel l = new JLabel();
		l.setPreferredSize(new Dimension(10, 1));
		l.setMaximumSize(new Dimension(10, 1));
		add(l);

		add(onOffButton = new OnOffButton());

		l = new JLabel();
		l.setPreferredSize(new Dimension(10, 1));
		l.setMaximumSize(new Dimension(10, 1));
		add(l);

		setBorder(BorderFactory.createEtchedBorder());

		if(on) {
			setAllEnabled(false);
		} else {
			setAllEnabled(true);
		}
		
		mono.addActionListener(this);
		stereo.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		onOffButton.addActionListener(this);

	}
	public void stop() {
		if(on) onOffButton.doClick();
	}
	public void addActionListener(ActionListener l) {
		onOffButton.addActionListener(l);
	}


	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == onOffButton) {			
			setAllEnabled(on);
			on = !on;
		} else if(o == mono) {
			channels = 1;
			setLeftRightEnabled(false);
		} else if(o == stereo) {
			channels = 2;
			setLeftRightEnabled(true);
		} else if(o == left) {
			useRightChannel = false;
		} else if(o == right) {
			useRightChannel = true;
		}
	}
	private void setAllEnabled(boolean boo) {
		mono.setEnabled(boo);
		stereo.setEnabled(boo);
		rateSlider.setEnabled(boo);
		if((boo == true) && (channels == 2)) {
			setLeftRightEnabled(true);
		} else {
			setLeftRightEnabled(false);
		}
	}
	private void setLeftRightEnabled(boolean boo) {
		left.setEnabled(boo);
		right.setEnabled(boo);
	}
	private void setOnOffControlState() {
		if(!on) {
			left.setEnabled(true);
			right.setEnabled(true);
			mono.setEnabled(true);
			stereo.setEnabled(true);
			rateSlider.setEnabled(true);
			//if(dial != null) dial.setSamplingState(-1);
		} else {
			left.setEnabled(false);
			right.setEnabled(false);
			mono.setEnabled(false);
			stereo.setEnabled(false);
			rateSlider.setEnabled(false);
			//if(dial != null) dial.setSamplingState(0);
		}
	}

	public int getRate() {
		return rate;
	}
	public int getChannels() {
		return channels;
	}
	public boolean getUseRightChannel() {
		return useRightChannel;
	}

	private class MonoStereoPanel extends JPanel {
		public MonoStereoPanel() {
			setOpaque(false);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			JLabel l = new JLabel();
			l.setOpaque(false);
			l.setPreferredSize(new Dimension(5, 1));
			l.setMaximumSize(new Dimension(5, 1));
			add(l);


			ButtonGroup bg = new ButtonGroup();
			JRadioButton j = new JRadioButton();
			j.setOpaque(false);
			j.setIcon(new ImageIcon(this.getClass().getResource("images/rb.png")));
			j.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbs.png")));
			j.setDisabledIcon(new ImageIcon(this.getClass().getResource("images/rbd.png")));
			j.setDisabledSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbds.png")));
			if(channels == 1) j.doClick();
			mono = j;
			j = new JRadioButton();
			j.setOpaque(false);
			j.setIcon(new ImageIcon(this.getClass().getResource("images/rb.png")));
			j.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbs.png")));
			j.setDisabledIcon(new ImageIcon(this.getClass().getResource("images/rbd.png")));
			j.setDisabledSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbds.png")));
			if(channels == 2) j.doClick();
			stereo = j;

			bg.add(mono);

			bg.add(stereo);		
			add(mono);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-MONO.png")));
			add(l);
			l = new JLabel();
			l.setOpaque(false);
			l.setPreferredSize(new Dimension(5, 1));
			l.setMaximumSize(new Dimension(5, 1));
			add(l);
			add(stereo);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-STEREO.png")));
			add(l);
			//java.awt.Component c = Box.createHorizontalStrut(20);
			//c.setSize(20, 1);
			//add(c);			
			l = new JLabel();
			l.setOpaque(false);
			l.setPreferredSize(new Dimension(10, 1));
			l.setMaximumSize(new Dimension(10, 1));
			add(l);

			//setBorder(BorderFactory.createEtchedBorder());
		}
	}
	private class LeftRightPanel extends JPanel {
		public LeftRightPanel() {
			setOpaque(false);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			ButtonGroup bg = new ButtonGroup();
			JRadioButton j = new JRadioButton();
			j.setOpaque(false);
			j.setIcon(new ImageIcon(this.getClass().getResource("images/rb.png")));
			j.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbs.png")));
			j.setDisabledIcon(new ImageIcon(this.getClass().getResource("images/rbd.png")));
			j.setDisabledSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbds.png")));
			if(!useRightChannel) j.doClick();
			left = j;
			j = new JRadioButton();
			j.setOpaque(false);
			j.setIcon(new ImageIcon(this.getClass().getResource("images/rb.png")));
			j.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbs.png")));
			j.setDisabledIcon(new ImageIcon(this.getClass().getResource("images/rbd.png")));
			j.setDisabledSelectedIcon(new ImageIcon(this.getClass().getResource("images/rbds.png")));
			if(useRightChannel) j.doClick();
			right = j;
			bg.add(left);
			bg.add(right);
			add(left);
			JLabel l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-L.png")));
			add(l);
			l = new JLabel();
			l.setOpaque(false);
			l.setPreferredSize(new Dimension(5, 1));
			l.setMaximumSize(new Dimension(5, 1));
			add(l);
			add(right);
			l = new JLabel();
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-R.png")));
			add(l);
			l = new JLabel();
			l.setOpaque(false);
			l.setPreferredSize(new Dimension(10, 1));
			l.setMaximumSize(new Dimension(10, 1));
			add(l);

			//setBorder(BorderFactory.createEtchedBorder());
		}

	}
	private class RateSlider extends JSlider implements ChangeListener {
		public RateSlider() {
			super(0, 4, 4);
			setOpaque(false);
			setToolTipText("Set Sampling Frequency");
			setPreferredSize(new Dimension(30, HEIGHT));
			setMajorTickSpacing(1);
			//setPaintTicks(true);
			setSnapToTicks(true);
			Hashtable h = new Hashtable();
			JLabel l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-11.png")));
			h.put(new Integer(0), l);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-22.png")));
			h.put(new Integer(1), l);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-32.png")));
			h.put(new Integer(2), l);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-44.png")));
			h.put(new Integer(3), l);
			l = new JLabel();
			l.setOpaque(false);
			l.setIcon(new ImageIcon(this.getClass().getResource("images/text-48.png")));
			h.put(new Integer(4), l);
			setLabelTable(h);
			setPaintLabels(true);
			addChangeListener(this);
			setEnabled(true);

			//setBorder(BorderFactory.createEtchedBorder());
		}
		public void stateChanged(ChangeEvent e) {
			if(!getValueIsAdjusting()) {
				switch (getValue()) {
				case 0:
					rate = 11025;
					break;
				case 1:
					rate = 22050;
					break;
				case 2:
					rate = 32000;
					break;
				case 3:
					rate = 44100;
					break;
				case 4:
					rate = 48000;
					break;
				}
			}
		}
						
	} // RateSlider

	private class OnOffButton extends JButton {
		public OnOffButton() {
			setBackground(Color.black);
			setPreferredSize(new Dimension(30, 30));
			setMargin(new Insets(0, 0, 0, 0));
			setFont(new Font("Dialog", 1, 10));
			setForeground(Color.white);
			setForOn(on);
			addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setForOn(on);
					}
				});
						
		}				
		private void setForOn(boolean forOn) {
			if(forOn) {
				setText("On");
				setBackground(Color.red);
			} else {
				setText("Off");
				setBackground(Color.black);
			}
		}
	} // OnOffButton
} // Controls



