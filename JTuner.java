// JTuner.java
//
//    Jtuner - program for tuning guitars and other instruments
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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFormat;

import javax.swing.*;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Insets;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;

import java.awt.Rectangle;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.ImageObserver;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;

import java.awt.Toolkit;

import java.util.Hashtable;

import java.io.IOException;

import java.io.Serializable;

import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

import javax.swing.text.Document;
import javax.swing.text.AttributeSet;

import java.text.Format;
import java.text.DecimalFormat;
import java.text.ParseException;


public class JTuner extends JPanel {

	private static final String VERSION = "0.0.2";

	private Controls controls = null;
	private Scope scope = null;
	private Dial dial = null;

	private final static int SAMPLE_COUNT = 96000;
	private int [] drawValues = new int[SAMPLE_COUNT];
	private double [] calcValues = new double[SAMPLE_COUNT];

	private final static int SAMPLE_WINDOW_WIDTH = 100;

	private final static int AV_COUNT_MAX = 400; // 150
	private volatile int avCount = 150;

	private class TargetFreq {

		private volatile double targetFreq = 440;
		private volatile double logTargetFreq = Math.log(targetFreq);

		public void setTargetFreq(double targetFreq) {
			setTargetFreq(targetFreq, Math.log(targetFreq));
		}
		public void setTargetFreq(double targetFreq, double logTargetFreq) {
			this.targetFreq = targetFreq;
			this.logTargetFreq = logTargetFreq;
		}

		public double getTargetFreq() {
			return targetFreq;			
		}
		public double getLogTargetFreq() {
			return logTargetFreq;
		}
	}
	private final TargetFreq targetFreq = new TargetFreq();


	private volatile double calcFreq = -1;
	private volatile boolean clipping = false;

	private boolean autoSense = false;
	private boolean autoSenseChromatic = false;

	private double dialHorizScale = 4;

	private static final double SEMITONE_FACTOR = java.lang.Math.pow(2, 1.0/12.0 );
	private static final double LOG_SEMITONE_FACTOR = java.lang.Math.log(SEMITONE_FACTOR);
	private static final double CENT_FACTOR = java.lang.Math.pow(2, 1.0/1200.0 );
	private static final double LOG_CENT_FACTOR = java.lang.Math.log(CENT_FACTOR);

	private String initError;

	private Image backImage = null;
	private Image backImageBase = null;
	private int backW = -1, backH = -1;
	   

	public JTuner() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(controls = new Controls());
		add(dial = new Dial());	
		add(scope = new Scope());
		controls.addActionListener(new SampleGetter(/*true*/));
		controls.addActionListener(scope);
		backImageBase = (new ImageIcon
						 (this.getClass().getResource
						  ("images/back.png"))
						 ).getImage();
		while(backImageBase.getHeight(null) <= 0);
		setBackImage();
	}
	public void paint(Graphics g) {
		setBackImage();
		if(backImage != null) {	
			g.drawImage(backImage,
						0,
						0,
						this);
		}
		paintChildren(g);
	}
	private void setBackImage() {
		Dimension d = getSize();
		if((backW != d.width) ||
		   (backH != d.height)) {
			if(d.width > 0 && d.height > 0) {
				backW = d.width;
				backH = d.height;
				backImage = backImageBase.getScaledInstance
					(backW, backH, Image.SCALE_FAST);
			}													   
		}
	}

	public void stop() {
		controls.stop();
	}

	private class Dial extends JPanel {
		private DialNotesPanel dialNotesPanel;
		public Dial() {
			setOpaque(false);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(dialNotesPanel = new DialNotesPanel());			
			setBorder(BorderFactory.createEtchedBorder());
		}
		public void setText(double freq) {
			dialNotesPanel.setText(freq);
		}
		public void setSamplingState(int inState) {
			dialNotesPanel.setSamplingState(inState);
		}
		public void repaintPart() {
			dialNotesPanel.repaintPart();
		}
		private class DialNotesPanel extends JPanel {
			private DisplayPanel displayPanel;
			private static final double HORIZ_SCALE_SLIDER_FACTOR = 400.0;
			private Notes notes;
			public DialNotesPanel() {
				setOpaque(false);
				setLayout(new BorderLayout());
				displayPanel = new DisplayPanel();
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
				p.setOpaque(false);
				p.add(new DialHorizScaleSlider());
				p.add(new DialDampingSlider());
				add(p, BorderLayout.NORTH);	
				add(displayPanel, BorderLayout.CENTER);
				add(notes = new Notes(), BorderLayout.SOUTH);
			}
			public void repaintPart() {
				displayPanel.repaintPart();
			}
			public void setSamplingState(int inState) {
				displayPanel.setSamplingState(inState);
			}
			public void setText(double freq) {
				notes.setText(freq);
			}
			private class DisplayPanel extends JPanel {
				LEDPanel ledPanel = new LEDPanel();
				Display display = null;
				public DisplayPanel() {
					setOpaque(false);
					setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
					JPanel p = new JPanel();
					p.setOpaque(false);
					p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
					p.add(display = new Display());
					p.setBorder
						(BorderFactory.createCompoundBorder
						 (
						  BorderFactory.createEmptyBorder(4,4,0,4)
						  ,
						  BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)
						  ));
					add(p);
					add(ledPanel);
				}
				public void repaintPart() {
					display.repaint();
				}
				public void setSamplingState(int inState) {
					switch(inState) {
					case -1:
						ledPanel.setState(0);
						break;
					case 0:				
						ledPanel.setState(1);
						break;
					case 1:
						ledPanel.setState(2);
						break;
					default:
						ledPanel.setState(3);
					}				
				}
				private class Display extends JPanel {

					private final Color LINE_COLOR = new Color(100, 100, 100);
					private final Color TEXT_COLOR = new Color(0, 192, 0);
					private final Color BAR_COLOR = new Color(255, 0, 0);

					private final Arrow rightArrowOn = new Arrow(true, true);
					private final Arrow rightArrowOff = new Arrow(true, false);
					private final Arrow leftArrowOn = new Arrow(false, true);
					private final Arrow leftArrowOff = new Arrow(false, false);

					public Display () {
						setBackground(new Color(20, 20, 20));
						setPreferredSize(new Dimension(400, 50));
					}
					private boolean painted = false;
					public void paint(Graphics g) {
						Dimension d = getSize();
						int w = d.width;
						int h = d.height;

						Graphics2D g2 = (Graphics2D) g;
						g2.setBackground(getBackground());
						g2.clearRect(0, 0, w, h);

						double scaling = ((double)w / dialHorizScale);
						double centre = (double)w / 2.0;

						if(!painted) {
							leftArrowOn.paintIcon(this, g2, 5 * (w / 8), h / 2);
							rightArrowOn.paintIcon(this, g2, 3 * (w / 8), h / 2);
							painted = true;
						}

						double targFreq = targetFreq.getTargetFreq();
						double logTargetFreq = targetFreq.getLogTargetFreq();
						Arrow a = ((calcFreq > 0) && (calcFreq > targFreq)) ? 
							leftArrowOn : leftArrowOff;
						a.paintIcon(this, g2, 5 * (w / 8), h / 2);
						a = ((calcFreq > 0) && (calcFreq < targFreq)) ? 
							rightArrowOn : rightArrowOff;
						a.paintIcon(this, g2, 3 * (w / 8), h / 2);
						
						if(initError != null) {
							g2.setColor(Color.red);
							java.awt.Font errFt = new Font("Sans Serif", 1, 20);
							g2.setFont(errFt);
							java.awt.font.GlyphVector errGv = errFt.createGlyphVector
								(g2.getFontRenderContext(), initError);							
							java.awt.Rectangle errRs  = errGv.getVisualBounds().getBounds();
							int errHt = errRs.height;
							int errWt = errRs.width;
							g2.drawString(initError, (w / 2) - (errWt / 2), (h / 2) + (errHt / 2));
						} else {
							if((calcFreq > 0) && (targFreq > 0)) {
								double pos = Math.log(calcFreq) - logTargetFreq;
								pos = (pos * scaling / LOG_SEMITONE_FACTOR) + centre;
								g2.setStroke(new BasicStroke(3));
								g2.setColor(BAR_COLOR);
								g2.draw(new Line2D.Double(pos, 0, pos, h));
							}
						}

						g2.setStroke(new BasicStroke(1));
						g2.setColor(LINE_COLOR);
						double cent = scaling / 100.0;
						double xright = centre;
						double xleft = centre;
						int count = 0;
						while((xleft -= cent) > 0) {
							xright += cent;
							double top;
							if((++count % 100) == 0) {
								top = 0;
							} else if((count % 10) == 0) {
								top = h - (h / 4);
							} else if((count % 5) == 0) {
								top = h - 3 * (h / 16);
							} else {
								top = h - (h / 8);
							}
							g2.draw(new Line2D.Double(xleft, top, xleft, h));
							g2.draw(new Line2D.Double(xright, top, xright, h));
						}

						g2.setColor(TEXT_COLOR);
						java.awt.Font ft = new Font("Dialog", 1, 10);
						g2.setFont(ft);
						for(int i = 0; i < NB.LOG_NOTES.length; i++) {
							double pos = NB.LOG_NOTES[i] - logTargetFreq;
							pos = (pos * scaling / LOG_SEMITONE_FACTOR) + centre;
							if(pos > -20) {
								if(pos > (w + 20)) break;
								
								java.awt.font.GlyphVector gv = ft.createGlyphVector
									(g2.getFontRenderContext(), NB.NOTE_NAMES[i]);

								java.awt.Rectangle rs  = gv.getVisualBounds().getBounds();
								int ht = rs.height;
								int wt = rs.width;
								g2.drawString(NB.NOTE_NAMES[i], (int)(pos - (double)wt / 2.0), ht);
							}
						}
						g2.setColor(new Color(0, clipping ? 255 : 127, 0));
						ft = new Font("Dialog", 1, 8);
						g2.setFont(ft);
						g2.drawString("CLIPPING", 2, h - (h / 4));

						g2.setColor(new Color(100, 100, 100));
						g2.draw(new Line2D.Double(centre, 0, centre, h));
					}
			
					private class Arrow implements Icon {
						
						static final int IMAGE_HEIGHT = 128;
						static final int IMAGE_WIDTH = 256; 
						static final int ICON_HEIGHT = 16;
						static final int ICON_WIDTH = 32;

						private Image image;

						private int initState = 0;
						private boolean isRight;
						private boolean isOn;

						public Arrow(boolean isRight, boolean isOn) {
							this.isRight = isRight;
							this.isOn = isOn;
						}
						public void paintIcon(Component c, Graphics g, int inx, int iny ) {
							Graphics2D g2 = (Graphics2D)g;
							
							final int x = inx;
							final int y = iny;
							switch(initState) {
							case 0:
								final java.awt.GraphicsConfiguration dc = 
									(GraphicsConfiguration)g2.getDeviceConfiguration();
								initState++;
								(new Thread(new Runnable() {
										public void run() {
											image = setImage
												(
												 dc.createCompatibleImage(IMAGE_WIDTH, IMAGE_HEIGHT),
												 x, y
												 );
											initState++;
											repaint();
										}
									})).start();
								break;
							case 1:
								break;
							case 2:
								g2.drawImage(image,
											 x - (ICON_WIDTH / 2), 
											 y - (ICON_HEIGHT / 2), 
											 null);
								break;
							}
							 			  
						}
						private Image setImage(Image image, int x, int y) {
							Graphics2D gi = (Graphics2D)image.getGraphics();
							gi.setColor(new Color(0, isOn ? 255 : 127, 0));
							
							if(isRight) {
								gi.fillRect(0, IMAGE_HEIGHT / 4, IMAGE_WIDTH / 2, IMAGE_HEIGHT / 2);
							} else {
								gi.fillRect(IMAGE_WIDTH / 2, IMAGE_HEIGHT / 4, IMAGE_WIDTH / 2, IMAGE_HEIGHT / 2);
							}								
							
							gi.fill(new java.awt.Polygon
									(
									 new int[] {IMAGE_WIDTH / 2, 
												isRight ? IMAGE_WIDTH : 0,      
												IMAGE_WIDTH / 2},
									 new int[] {0, 
												IMAGE_HEIGHT / 2, 
												IMAGE_HEIGHT},
									 3
									 ));
							gi.dispose();
							Image newImage = image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
							// Wait for scaling of image to complete
							class Monitor {public boolean done = false;}
							final Monitor monitor = new Monitor();
							newImage.getWidth(new ImageObserver() {
									public boolean imageUpdate(Image i, int a, int b, int c, int d, int e){
										synchronized(monitor) {
											monitor.notify();
											monitor.done = true;
										}
										return true; //NB returning false resulted in an exception;
									}
								});
							try{
								synchronized(monitor) {
									if(!monitor.done) monitor.wait();
								}
							} catch (InterruptedException e) {
								System.out.println("Interrupted - " + e.getMessage());
							}
							// End wait.
							return newImage;
						}
						public int getIconWidth() {
							return ICON_WIDTH;
						}

						public int getIconHeight() {
							return ICON_HEIGHT;
						}

					} // Arrow
				} // Display 
				private class LEDPanel extends JPanel {
					private LED [] leds = new LED [3];
					private int lastLight = 0;
					public LEDPanel() {
						setOpaque(false);
						setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
					
						JPanel p = new JPanel();
						p.setOpaque(false);
						p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));					
						leds[0] = new LED("green");
						leds[1] =  new LED("yellow");
						leds[2] = new LED("red");
						p.add(leds[0]);  
						p.add(leds[1]);
						p.add(leds[2]);
						p.setBorder
							(BorderFactory.createCompoundBorder
							 (
							  BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)
							  ,
							  BorderFactory.createEmptyBorder(2,3,2,3)
							  ));
					
						add(Box.createHorizontalGlue());
						add(p);
						add(Box.createHorizontalGlue());
						setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
					}
					public void setState(int inState) {
						if(lastLight > 0) {
							leds[lastLight - 1].setOnState(false);
						}
						lastLight = inState;
						if(inState > 0) {
							leds[lastLight - 1].setOnState(true);
						}
						repaint();
					}
					private class LED extends JLabel {

						private boolean on = false;

						private ImageIcon onIcon = null;
						private ImageIcon offIcon = null;
						public LED(String color) {
							setOpaque(false);
							if(!color.equals("red") && !color.equals("yellow"))
								color = "green";
							
							color = "images/led" + color;
							onIcon = new ImageIcon(this.getClass().getResource(color + "on.png"));
							offIcon = new ImageIcon(this.getClass().getResource(color + "off.png"));
																			   						   
							setPreferredSize(new Dimension(onIcon.getIconWidth(), onIcon.getIconHeight()));
							setMaximumSize(new Dimension(onIcon.getIconWidth(), onIcon.getIconHeight()));
							setMinimumSize(new Dimension(onIcon.getIconWidth(), onIcon.getIconHeight()));
							setOnState(false);
						}
						public void setOnState(boolean on) {
							this.on = on;
							if(on) {
								setIcon(onIcon);
							} else {
								setIcon(offIcon);
							}
							repaint();
						}
					} // LED
				} // LEDPanel
			} // DisplayPanel
			private class DialHorizScaleSlider extends JSlider {
				public DialHorizScaleSlider() {
					super(100, 19200, (int)(dialHorizScale * HORIZ_SCALE_SLIDER_FACTOR));
					setOpaque(false);
					setPreferredSize
						(new Dimension
						 (200, 
						  getPreferredSize().height));
					setToolTipText("Change horizontal scaling");
					addChangeListener(new ChangeListener () {
							public void stateChanged(ChangeEvent e) {
								JSlider s = (JSlider)e.getSource();
								dialHorizScale = (double)getValue() / HORIZ_SCALE_SLIDER_FACTOR;
								displayPanel.repaint();
							};	
						});

				}
			} // DialHorizScaleSlider
			private class DialDampingSlider extends JSlider {
				public DialDampingSlider() {
					super(1, AV_COUNT_MAX, avCount);
					setOpaque(false);
					setPreferredSize
						(new Dimension
							(200,
							 getPreferredSize().height));
					setToolTipText("Damping Level");
					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								JSlider s = (JSlider)e.getSource();
								avCount = (int)getValue();
							};
						});
				}
			} // DialDampingSlider
						
		} // DialNotesPanel

		private class Notes extends JPanel {
			private static final double MAX_FREQ = 9999;
			private static final double MIN_FREQ = 27;

			private JToggleButton nullButton = new JToggleButton();
			private FreqInput freqInput;
			private FreqSlider freqSlider;
			public Notes() {
				setOpaque(false);
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
			    add(freqSlider = new FreqSlider());
				add(Box.createVerticalGlue());

				JPanel p = new JPanel();
				p.setOpaque(false);
				p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

				ButtonGroup bg = new ButtonGroup();
				JToggleButton nb = null;

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("E", 82.407);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("A", 110);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("D", 146.823);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("G", 195.998);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("B", 246.942);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("E", 329.628);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			

				p.add(new AutoButtonsPanel(bg));

				p.add(Box.createHorizontalGlue());

				add(p);

				//---------
				add(Box.createVerticalGlue());


				//---------
				p = new JPanel();
				p.setOpaque(false);
				p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

				nb = new NoteButton("E'", 164.814);

				p.add(Box.createHorizontalGlue());			
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("A'", 220);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("D'", 293.646);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("G'", 391.996);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("B'", 493.884);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				nb = new NoteButton("E'", 659.256);
				p.add(nb);
				bg.add(nb);

				p.add(Box.createHorizontalGlue());			
				p.add(freqInput = new FreqInput());

				p.add(Box.createHorizontalGlue());

				add(p);

				add(Box.createVerticalGlue());
				
				JLabel l = new JLabel();
				l.setOpaque(false);
				l.setPreferredSize(new Dimension(1, 5));
				l.setMaximumSize(new Dimension(1, 5));
				add(l);

				bg.add(nullButton);

			}
			private void setFrequency(Object source, double freq) {
				targetFreq.setTargetFreq(freq);
				setFrequencyLabels(source, freq);
			}
			private void setFrequencyLabels(Object source, double freq) {
				autoSense = false;
				autoSenseChromatic = false;
				if(!(source instanceof NoteButton)) {
					nullButton.doClick();
				}
				if(!(source instanceof FreqInput)) {
					freqInput.setText(freq);
				}
				freqSlider.setValue(freq);
				if(dial != null) dial.repaint();
			}				
			public void setText(double freq) {
				freqInput.setText(freq);
			}
			private class AutoButtonsPanel extends JPanel {
				public AutoButtonsPanel(ButtonGroup bg) {
					setOpaque(false);
					setLayout(new BoxLayout(this, BoxLayout.X_AXIS));					
					AutoButton j = new AutoButton("AG", "Auto Select Frequency - Guitar Strings", false);
					add(j);
					bg.add(j);
					add(Box.createHorizontalGlue());
					j.doClick();

					j = new AutoButton("AC", "Auto Select Frequency - Chromatic", true);
					add(j);
					bg.add(j);
					setMaximumSize(new Dimension(60, 30));
					setPreferredSize(new Dimension(60, 30));
				}
				private class AutoButton extends JRadioButton {
					public AutoButton(String name, String toolTip, final boolean isChromatic) {
						super();
						setOpaque(false);
						setToolTipText(toolTip);
						setMargin(new Insets(0, 0, 0, 0));

						if(!name.equals("AG")) name = "AC";
						
						setIcon(new ImageIcon(this.getClass().getResource("images/b-" + name + ".png")));
						setSelectedIcon(new ImageIcon(this.getClass().getResource("images/bs-" + name + ".png")));
						setRolloverIcon(new ImageIcon(this.getClass().getResource("images/br-" + name + ".png")));
						setRolloverSelectedIcon(new ImageIcon(this.getClass().getResource("images/brs-" + name + ".png")));
						setPressedIcon(new ImageIcon(this.getClass().getResource("images/br-" + name + ".png")));

						addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									autoSense = true;
									autoSenseChromatic = isChromatic;
								}
							});
					}
				}
			}

			private class FreqSlider extends JSlider  {
				private int disableCount = 0;
				private static final double FAC = 10000.0;
				public FreqSlider() {
					super((int)(Math.log(MIN_FREQ) * FAC), 
						  (int)(Math.log(MAX_FREQ) * FAC), 
						  (int)(Math.log(targetFreq.getTargetFreq()) * FAC));
					setOpaque(false);
					setToolTipText("Set Target Frequency");
					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								if(disableCount <= 0) {
									double tmp = Math.exp((double)getValue() / FAC);
									setFrequency(this, tmp);
								}
							}
						});
				}
				public void setValue(double newValue) {
					disableCount++;
					super.setValue((int)(Math.log(newValue) * FAC));
					disableCount--;
				}
			}
			private class NoteButton extends JRadioButton {
				private double freq;
				public NoteButton(String text, double infreq) {
					super();
					setOpaque(false);
					this.freq = infreq;
					setMargin(new Insets(0, 0, 0, 0));
					final NoteButton n = this;
					addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								setFrequency(n, freq);
							}
						});
					setToolTipText(text + " - " + infreq + " Hz");
					String s = text.substring(0, 1);
					if(text.length() > 1) s = s + "X";
					   
					setIcon(new ImageIcon(this.getClass().getResource("images/b-" + s + ".png")));
					setSelectedIcon(new ImageIcon(this.getClass().getResource("images/bs-" + s + ".png")));
					setRolloverIcon(new ImageIcon(this.getClass().getResource("images/br-" + s + ".png")));
					setRolloverSelectedIcon(new ImageIcon(this.getClass().getResource("images/brs-" + s + ".png")));
					setPressedIcon(new ImageIcon(this.getClass().getResource("images/br-" + s + ".png")));
				}
			}
			private class FreqInput extends JTextField {
				private int disableCount = 0;
				private final DecimalFormat format = new DecimalFormat("##0.000");
				public FreqInput () {
					super();				 
					setBackground(new Color(10, 10, 10));
					setForeground(new Color(0, 255, 0)); 
					setCaretColor(new Color(0, 255, 0));
					setFont(new Font("Dialog", 1, 10));
					setPreferredSize(new Dimension(60, 22));
					setMaximumSize(new Dimension(60, 22));
					setHorizontalAlignment(JTextField.RIGHT);
				}
				protected Document createDefaultModel() {
					return new FreqDocument();
				}
				private void setFreq(double freq) {
					if(disableCount <= 0) {
						setFrequency(this, freq);
					}
				}
				public void setText(String text) {
					disableCount++;
					super.setText(text);
					disableCount--;
				}
				public void setText(double freq) {
					setText(format.format(freq));
				}
				private class FreqDocument extends PlainDocument {
					private double newVal;
					public void insertString(int offs, String str, AttributeSet a) 
						throws BadLocationException {

						String currentText = getText(0, getLength());
						String beforeOffset = currentText.substring(0, offs);
						String afterOffset = currentText.substring(offs, currentText.length());

						if(isValid(beforeOffset + str + afterOffset)) {
							super.insertString(offs, str, a);
							if(newVal >= MIN_FREQ) setFreq(newVal);
						}
					}

					public void remove(int offs, int len) throws BadLocationException {
						String currentText = getText(0, getLength());
						String beforeOffset = currentText.substring(0, offs);
						String afterOffset = currentText.substring(len + offs,
																   currentText.length());
						if(isValid(beforeOffset + afterOffset)) {
							super.remove(offs, len);
							if(newVal >= MIN_FREQ) setFreq(newVal);
						}
					}
					private boolean isValid(String proposedResult) {
						char[] digits = proposedResult.toCharArray();
						int state = 0;
						int precount = 0;
						int postcount = 0;
						boolean valid = true;
						for(int i = 0; i < digits.length; i++) {
							char d = digits[i];
							switch(state) {
							case 0:
								if(Character.isDigit(d)) {
									if(++precount > 4) valid = false;
								} else if(d == '.') {
									state = 1;
								} else {
									valid = false;
								}
								break;
							case 1:
								if(Character.isDigit(d)) {
									if(++postcount > 3) valid = false;
								} else {
									valid = false;
								}
								break;
							}	
							if(!valid) break;
						}
						if(valid) {
							if((precount + postcount) == 0) {
								newVal = 0;
							} else {
								newVal = (new Double(proposedResult)).doubleValue();
							}
						}
						if(newVal > MAX_FREQ) valid = false;
						if(!valid) Toolkit.getDefaultToolkit().beep();
						return valid;						
					}

				}

			} // FreqInput
		
		} // Notes
	} // Dial 
	private class Scope extends JPanel implements ActionListener {
		private int threshold = 5000;
		private int decayThreshold = 2500;
				
		private int samplesRequired = 0;

		private int freqBarPos = 0;

		private int wait = 9600;  // 5th second

		private double scaleValue = 250;

		private Ray ray = null;

		private int scopeSampleOffset = 0;

		private int scopeSamplesPerPixel = 1;

		public Scope() {
			setOpaque(false);
			setLayout(new BorderLayout());
			add(new ThresholdPanel(), BorderLayout.WEST);
			add(ray = new Ray(), BorderLayout.CENTER);
			add(new Scale(), BorderLayout.EAST);
			add(new Wait(), BorderLayout.NORTH);
			add(new HorizScalePanel(), BorderLayout.SOUTH);
			setBorder(BorderFactory.createEtchedBorder());
		}
		public void actionPerformed(ActionEvent e) {
			ray.onOff();
		}

		public int getThreshold() {
			return threshold;
		}
		public int getDecayThreshold() {
			return decayThreshold;
		}
		public int getSamplesRequired() {
			return samplesRequired;
		}
		public int getWait() {
			return wait;
		}
		public void repaintPart() {
			ray.repaint();
		}
		private class Ray extends JPanel {
			private boolean on = false;
			public Ray () {
				setBackground(new Color(20, 20, 20));
				setPreferredSize(new Dimension(400, 100));
			}
			public void onOff() {
				on = !on;
				ray.repaint();
			}
			public void paint(Graphics g) {

				Dimension d = getSize();
				int w = d.width;
				int h = d.height;
				Graphics2D g2 = (Graphics2D) g;
				g2.setBackground(getBackground());
				g2.clearRect(0, 0, w, h);

				if(!on) {
					g2.setColor(new Color(0, 192, 0));
					String st = null;
					int offs = 20;
					int fontsize = 10;
					for(int i = 0; i < 20; i++) {
						switch(i) {
						case 0:
							st = "JTuner Ver. " + VERSION + " - Copyright (C) 2004 Michael Corlett.";							
							fontsize = 12;
							break;
						case 2:
							fontsize = 10;
							st = "This program is free software; you can redistribute it and/or";
							break;
						case 3:
							st = "modify it under the terms of the GNU General Public License";
							break;
						case 4:
							st = "as published by the Free Software Foundation; either version 2";
							break;
						case 5:
							st = "of the License, or (at your option) any later version.";
							break;
						case 7:
							st = "This program is distributed in the hope that it will be useful,";
							break;
						case 8:
							st = "but WITHOUT ANY WARRANTY; without even the implied warranty of";
							break;
						case 9:
							st = "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the";
							break;
						case 10:
							st = "GNU General Public License for more details.";
							break;
						case 12:
							st = "You should have received a copy of the GNU General Public License";
							break;
						case 13:
							st = "along with this program; if not, write to the Free Software";
							break;
						case 14:
							st = "Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.";
							break;
						default:
							offs += 10;
							st = "";
							break;
						}

						java.awt.Font ft = new Font("Sans Serif", 0, fontsize);
						g2.setFont(ft);
						java.awt.font.GlyphVector gv = ft.createGlyphVector
							(g2.getFontRenderContext(), st);							
						java.awt.Rectangle rs  = gv.getVisualBounds().getBounds();
						int ht = rs.height;
						int wt = rs.width;
						g2.drawString(st, 10, offs);
						offs += ht;
					}
					return;
				}

				samplesRequired = scopeSampleOffset + (int)(w * scopeSamplesPerPixel);
				if(samplesRequired > SAMPLE_COUNT) samplesRequired = SAMPLE_COUNT;

				Color green = new Color(0, 255, 0);
				Color blue = new Color(0, 0, 255);
				Color grey = new Color(128, 128, 128);
				Color cyan = new Color(0, 255, 255);

				double factor = ((double)h) / (Short.MAX_VALUE * 2);
				double mid = ((double)h) / 2;
				double ypos = 0;
				//double avypos = 0;
				int threshPos = (int)((
									   (-(((double)threshold) * factor))
									   *
									   (((scaleValue - 250) / 250) + 1)
									   ) + mid);
				int decayThreshPos = (int)((
									   (-(((double)decayThreshold) * factor))
									   *
									   (((scaleValue - 250) / 250) + 1)
									   ) + mid);
				double calcy = 0;
				int sampleIndex = scopeSampleOffset;
				long yPositive = 0;
				int yPositiveSamples = 0;
				long yNegative = 0;
				int yNegativeSamples = 0;

				double scaleFactor = (((scaleValue - 250) / 250) + 1);

				double logTargetFreq = targetFreq.getLogTargetFreq();
				for(int i = 0; i < w; i++) {

					int limit = sampleIndex + scopeSamplesPerPixel;
					if(limit > SAMPLE_COUNT) break;

					for(;sampleIndex < limit; sampleIndex++) {

						if(calcValues[sampleIndex] > 0) {
							calcy = logTargetFreq - Math.log(calcValues[sampleIndex]);
						}

						int tmp = drawValues[sampleIndex];
						if(tmp >= 0) {
							yPositive+=tmp;
							yPositiveSamples++;
						} else {
							yNegative+=tmp;
							yNegativeSamples++;
						}
						if((sampleIndex - scopeSamplesPerPixel) >= scopeSampleOffset) {
							tmp = drawValues[sampleIndex - scopeSamplesPerPixel];
							if(tmp >= 0) {
								yPositive-=tmp;
								yPositiveSamples--;
							} else {
								yNegative-=tmp;
								yNegativeSamples--;
							}							
						}						
					}
					
					double yposPlus = (yPositiveSamples <= 0) ? mid : (
									   ((double)yPositive / (double)yPositiveSamples)
									   *
									   (factor * scaleFactor)
									   ) + mid;
					double yposMinus = (yNegativeSamples <=0) ? mid : (
										((double)yNegative / (double)yNegativeSamples)
										*
										(factor * scaleFactor)
										) + mid;

					double vscaling = ((double)h / dialHorizScale);				
							  
					g2.setColor(blue);
					g2.draw(new Line2D.Double(i, yposPlus, i, yposMinus));
					
					double calcypos = (
									   (calcy * vscaling / LOG_SEMITONE_FACTOR)
									   *
									   scaleFactor
									   ) + mid;
					g2.setColor(new Color(0, 192, 0));
					g2.draw(new Line2D.Double(i, calcypos, i, calcypos - 1));
					
				}
				g2.setColor(grey);
				g2.draw(new Line2D.Double(0, threshPos, w, threshPos));
				g2.draw(new Line2D.Double(0, decayThreshPos, w, decayThreshPos));
				double calcxpos = (getWait() - scopeSampleOffset) / scopeSamplesPerPixel;
				g2.draw(new Line2D.Double(calcxpos, 0, calcxpos, h));
			}
		} // Ray	   
		private class ThresholdPanel extends JPanel {
			Threshold t = null;
			DecayThreshold d = null;
			public ThresholdPanel() {
				setOpaque(false);
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(t = new Threshold());
				add(d = new DecayThreshold());
			}
			private class Threshold extends JSlider {
				public Threshold() {
					super(VERTICAL, 
						  0, 
						  Short.MAX_VALUE, 
						  threshold = ((threshold < decayThreshold) ? decayThreshold : threshold));
					setOpaque(false);

					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								int val = ((JSlider)e.getSource()).getValue();
								if(val < decayThreshold) {
								
									d.setValue(val);
								}
								threshold = val;
								ray.repaint();
							}
						});
					setToolTipText("Attack Threshold");
					setEnabled(true);
				}
			} // Threshold
			private class DecayThreshold extends JSlider {
				public DecayThreshold() {
					super(VERTICAL, 
						  0, 
						  Short.MAX_VALUE, 
						  decayThreshold = ((decayThreshold > threshold) ? threshold : decayThreshold));
					setOpaque(false);
					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								int val = ((JSlider)e.getSource()).getValue();
								if(val > threshold) {
									t.setValue(val);
								}
								decayThreshold = val;
								ray.repaint();
							}
						});
					setToolTipText("Decay Threshold");
					setEnabled(true);
				}
			} // DecayThreshold
		} // ThresholdPanel
		private class Wait extends JSlider {
			public Wait() {
				super(HORIZONTAL, 0, SAMPLE_COUNT, wait);
				setOpaque(false);
				addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSlider s = (JSlider)e.getSource();
							wait = s.getValue();
							Scope.this.repaint();
						}
					});
				setToolTipText("Threshold Delay");
				setEnabled(true);
			}
		} // Wait
		private class HorizScalePanel extends JPanel {
			public HorizScalePanel() {
				setOpaque(false);
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(new HorizScaleSlider());
				add(new HorizOffsetSlider());
			}
			private class HorizScaleSlider extends JSlider {
				public HorizScaleSlider() {
					super(HORIZONTAL, 1, 240, scopeSamplesPerPixel);
					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								scopeSamplesPerPixel = getValue();
								Scope.this.repaint();
							}
						});
					setToolTipText("Horizontal Scale");
					setUI(new ReversedMetalSliderUI());
					setOpaque(false);
				}
			} // HorizScaleSlider
			private class HorizOffsetSlider extends JSlider {
				public HorizOffsetSlider() {
					super(HORIZONTAL, 0, SAMPLE_COUNT - 50, scopeSampleOffset);
					addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								scopeSampleOffset = getValue();
								Scope.this.repaint();
							}
						});
					setToolTipText("Horizontal Display Offset");
					setUI(new ReversedMetalSliderUI());
					setOpaque(false);
				}
			} // HorizOffsetSlider
		} // HorizScalePanel
		private class Scale extends JSlider {
			public Scale() {
				super(VERTICAL, 0, 1000, (int)scaleValue);
				addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSlider s = (JSlider)e.getSource();
							scaleValue = s.getValue();
							ray.repaint();
						}
					});
				setToolTipText("Vertical Scale");
				setUI(new ReversedMetalSliderUI());
				setOpaque(false);
			}
		} // Scale
	} // Scope

	private class SampleGetter implements Runnable, ActionListener {
		
		private Thread thread = null;

		int rate = 0;
		
		public synchronized void run() {

			initError = null;

			TargetDataLine t = null;	   
			
			int channels = controls.getChannels();
			boolean useRightChannel = controls.getUseRightChannel();
			AudioFormat af = null;
			{
				final int BITS_PER_CHANNEL = 16;
				rate = controls.getRate();
				af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
									 rate,
									 BITS_PER_CHANNEL,
									 channels,
									 channels * BITS_PER_CHANNEL / 8,
									 rate,
									 false);				
			}

			try {
				try {
					// Not working under Linux - OK in Windows ?? ...
					t = (TargetDataLine)
						AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, af));	
				} catch (IllegalArgumentException ia1) {
					// Therefore fallback position - not OK in Windows ??
					t = (TargetDataLine)
						AudioSystem.getLine(new Line.Info(TargetDataLine.class));
				} catch (ClassCastException cc) {
					System.out.println("ClassCastException caught - trying getLine again");
					try {
						System.out.println("Mixers:");
						javax.sound.sampled.Mixer.Info [] mia;
						mia = AudioSystem.getMixerInfo();
						for(int i = 0; i < mia.length; i++) {
							System.out.println(mia[i]);
						}
					} catch (Exception eee) {
						System.out.println("Can't get mixer list: " + eee.getMessage());
					}
					try {
						Line.Info [] lia = AudioSystem.getTargetLineInfo
							(new DataLine.Info(TargetDataLine.class, af));
						System.out.println("Available target data lines: ");
						for(int i = 0; i < lia.length; i++) {
							System.out.println(lia[i]);
						}
					} catch (Exception eee1) {
						System.out.println("Can't get target data line info: " + eee1.getMessage());
					}
					try {
						Line.Info []lia = AudioSystem.getTargetLineInfo
							(new Line.Info(TargetDataLine.class));
						System.out.println("Available target lines: ");
						for(int i = 0; i < lia.length; i++) {
							System.out.println(lia[i]);
						}
					} catch (Exception eee2) {
						System.out.println("Can't get target line info: " + eee2.getMessage());
					}

					t = (TargetDataLine)
						AudioSystem.getLine(new Line.Info(TargetDataLine.class));
				}
				
			} catch (LineUnavailableException e) {
				setError("Audio Line Unavailable (0)");
				return;
			} catch (SecurityException es) {
				setError("Security Exception (0)");
				return;
			} catch (IllegalArgumentException ei) {
				setError("Sound Illegal Argument");
				return;
			}

			try {		   
				t.open(af);
			} catch (LineUnavailableException e) {
				setError("Audio Line Unavailable (1)");
				return;	
			} catch (SecurityException es) {
				setError("Audio Security Exception (1)");
				return;
			} catch (IllegalStateException ei) {
				setError("Sound Illegal State");
				return;
			}

			t.start();			
			{  	
				
				final int BUFF_SIZE = 8192;
				byte[] ba = new byte[BUFF_SIZE * channels];				

				Peaks peaks = null;
				int sampleCount = 0;

				int samplingState = 0;
				int oldSamplingState = -1;
				int valIndex = 0;
				int decayCount = 0;

				int waitCount = 0;


				while(thread != null) {
					int threshold = scope.getThreshold();
					int decayThreshold = scope.getDecayThreshold();
					int samplesRequired = scope.getSamplesRequired();
					
					t.read(ba, 0, BUFF_SIZE * channels);
					
					for(int i = ( ((channels == 1) || (useRightChannel == false)) ? 0 : 2 ) ;
						i < (BUFF_SIZE * channels); 
						i += (channels * 2)) {

						int v1 = ba[i];
						v1 &= 0xff;
						int v2 = ba[i+1];
						v2 <<=8;
						int val = v1 | v2;

						sampleCount++;

						switch(samplingState) {
						case 0:
							if(valIndex < SAMPLE_COUNT) {drawValues[valIndex++] = 0;}
							if(val > threshold) {
								samplingState = 1;
								valIndex = 0;
								decayCount = 0;
								peaks = new Peaks();
								waitCount = scope.getWait();
							}
							break;
						case 1:
							if(waitCount-- <= 0) {
								samplingState = 2;
							}
							break;
						case 2:
						case 3:
							peaks.doVal(val, sampleCount);
							if(valIndex >= SAMPLE_COUNT) {
								samplingState = 3;
							} else {
								drawValues[valIndex++] = val;
							}
							//if(samplingState == 3) {
								if(Math.abs(val) < decayThreshold) {
									decayCount++;
								} else {
									decayCount = 0;
								}
								if(decayCount > 5000) {
									samplingState = 0;
									clipping = false;
									dial.repaint();
								}

								//}
							break;
						}

						if(oldSamplingState != samplingState) {
							oldSamplingState = samplingState;
							dial.setSamplingState(samplingState);
						}
										
					}
					thread.yield();  // Play nicely.
				}
				dial.setSamplingState(-1);
			}
			t.stop();
			t.close();			
		}
		private void setError(String err) {
			initError = err;
			dial.repaint();
			SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						controls.stop();
					}
				});
		}
		public void actionPerformed(ActionEvent e) {
			if(thread != null) {
				stop();
				calcFreq = 0;
				dial.repaint();
			} else {
				start();
			}
		}
		private void start() {
			thread = new Thread(this);
			thread.start();
		}
		public void stop() {
			thread.interrupt();
			thread = null;
		}

		protected void finalize() {
			if(thread != null) {
				stop();				
			}
		}
		private class Peaks {
			private final static int MAX_PEAKS = 60;

			private final String [] SIG_VALS 
				= {"A", "B", "C", "D", "E", "F", "G", "H",
				   "I", "J", "K", "L", "M", "N", "O", "P", "Q"};

			private final java.util.HashMap sigsHash = new java.util.HashMap();

			private int [] peaks = new int[MAX_PEAKS];
			private double [] peakTimes = new double[MAX_PEAKS];
			
			private double [] averages = new double[AV_COUNT_MAX];
			private double freqSum = 0;
			private int avIndex = 0;
			private int avUsed = 0;

			private int index = 0;

			private int valCurrent = 0;

			private int lastVal = 0;
			private int lastTime = 0;

			private boolean lockFreq = false;


			private final ClipTest ct = new ClipTest();

			Peaks () {
				for(int i = 0; i < MAX_PEAKS; i++) {
					peaks[i] = 0;
					peakTimes[i] = 0;
				}
			}

			int doVal(int val, int time) {
				int ret = -1;
				boolean move = false;
				if(lastVal == 0) {	
					if(val != 0) move = true;
				} else {
					if(val == 0) {
						move = true;
					} else {
						if(lastVal > 0 && val < 0) {
							move = true;
						} else if(lastVal < 0 && val > 0) {
							move = true;
						}
					}
				}
				if(move) {
					moveOn(val, time);
					ret = index;
				}
				valCurrent += val;
				/*
				if((Math.abs(val) > 500) && (Math.abs(lastVal - val) < 3)) {
					clipCount++;
					if(clipCount > 10) {
						clipping = true;
						clipCount = 0;
					}
				} else {
					clipCount = 0;
					clipUnCount++;
					if(clipUnCount > 4800) {
						clipping = false;
						clipUnCount = 0;
					}
				}
				*/
				ct.clipTest(val, time);

				lastVal = val;
				lastTime = time;				   
				return ret;
			}
			private class ClipTest {
				private final static int AV_COUNT = 5;
				private int clipUnCount = 0;
				private Averager preAv = new Averager();
				private Averager postAv = new Averager();
				private int max = 0;
				private int maxTime = 0;

				private int state = 0;

				void clipTest(int val, int time) {
					if(val > 10000) {
						switch(state) {
						case 0:
							max = 0;
							state = 1;
							// fall through
						case 1:
							if(val > max) {
								max = val;
								maxTime = time;
								preAv.doAv(val);
								break;
							} else {
								postAv.doAv(max);
								state = 2;
								// fall through
							}
						case 2:
							postAv.doAv(val);
							if((time - maxTime) == (AV_COUNT - 1)) {
								if(postAv.getAv() > (preAv.getAv() * 1.02)) {
									clipUnCount = 0;
									clipping = true;
								}
							}
							break;			
						case 3:
							break;
						}
					} else {
						state = 0;
					}
					if(clipping) {
						clipUnCount++;
						if(clipUnCount > 4800) {
							clipping = false;
						}
					}
				}
				private class Averager {
					int total = 0;
					private int [] arr = new int[AV_COUNT];
					private int index = 0;
					void doAv(int val) {
						total -= arr[index];
						total += (arr[index++] = val);
						if(index >= AV_COUNT){index = 0;}
						
					}
					int getAv() {
						return (int)((double)total / (double)AV_COUNT);
					}
				}
			}
			private void moveOn(int val, int time) {

				double dtime = (double)time - 
					( (time - lastTime) * ((double)val / (double)(val - lastVal)) );

				peaks[index] = valCurrent;
				peakTimes[index] = dtime;
				if(++index == MAX_PEAKS) index = 0;
				doCalc();

				valCurrent = 0;
			}
			private void doCalc() {
				int max = 0;
				int tmp = 0;
				int thresh = 0;
				for(int i = 0; i < MAX_PEAKS; i++) {
					tmp = Math.abs(peaks[i]);
					if(tmp > max) {
						max = tmp;
					}
				}
				thresh = (int)(max * 0.75);

				sigsHash.clear();
			    String s = "";

				int state = 0;
				double timeStart = 0;
				double timeAccum = 0;
				int wavecount = 0;
				int lastsign = 0;
				
				int j = index;
				for(int k = 0; k < MAX_PEAKS; k++) {
					switch(state) {
					case 0: // wait to change sign
					case 3:
						if((lastsign > 0 && peaks[j] < 0) ||
						   (lastsign < 0 && peaks[j] > 0)) {
							state++;
							// fall through
						} else {
							lastsign = peaks[j];						  
							break;
						}
					case 1:
					case 4:
						if(Math.abs(peaks[j]) > thresh) {
							if(state == 4) {
								timeStart = (peakTimes[j] - timeStart);
								if(sigsHash.containsKey(s)) {
									((SigType)sigsHash.get(s)).addVal(timeStart);
								} else {
									sigsHash.put(s, new SigType(timeStart));
								}
								wavecount++;
							}
							state = 2;
							timeStart = peakTimes[j];
							lastsign = peaks[j];
							s = "";
						}
						break;
					case 2:	// wait to change sign
						if(lastsign > 0) {
							if(peaks[j] < 0) state = 3;
						} else {
							if(peaks[j] > 0) state = 3;
						}
						lastsign = peaks[j];
						break;
					}
					s = s + sigChar(peaks[j], max);
					if(++j == MAX_PEAKS) j = 0;
						
				}
			   
				int t1 = sigsHash.size();
				if(t1 > 0) {
					final SigType na [] = new SigType[0];
					SigType [] t = (SigType[])sigsHash.values().toArray(na);
					SigType u = null;
					int maxc = 0;
					for(int i = 0; i < t1; i++) {
						if (t[i].count() > maxc) {
							u = t[i];
							maxc = u.count();
						}					
					}
					if(maxc > 0) {
						double thisFreq = (double) rate / u.av();

						if((avUsed < Math.min(AV_COUNT_MAX, 20)) || 
						   (Math.abs( (calcFreq - thisFreq) / calcFreq ) < 0.02)
						   ) {
							if(avUsed < avCount) {					
								avUsed++;
							} else {								
								freqSum -= averages[avIndex];
								if(avUsed > avCount) {
									avUsed--;
									freqSum -= averages[avIndex + 1 < AV_COUNT_MAX ? avIndex + 1 : 0];
								}
							}
							freqSum += (averages[avIndex] = thisFreq);
							calcFreq = freqSum / avUsed;
							if(++avIndex >= avCount) avIndex = 0;						

							setFreq();

							//tLastCount++;
							//if(Math.abs(tLastCalcFreq - thisFreq) > 0.001) {
							//
							//	System.out.println("[" + tLastCount + "]" + t1 + " " + maxc + " " +  thisFreq + " " + calcFreq);
							//	tLastCount = 0;
							//}
							//tLastCalcFreq = thisFreq;
					   
						}
					}


				}
				dial.repaintPart();
				scope.repaintPart();
			}
			private void setFreq() {
				if(!lockFreq && autoSense) {
					double arr[];
					double logArr[];
					if(autoSenseChromatic) {
						arr = NB.NOTES;
						logArr = NB.LOG_NOTES;
					} else {
						arr = NB.GUITAR_NOTES;													
						logArr = NB.LOG_GUITAR_NOTES;
					}
					double tmpTarget = Math.log(calcFreq);
					int ind = java.util.Arrays.binarySearch(logArr, tmpTarget);

					if(ind < 0) {
						ind = -(ind + 1);
						if(ind > 0) {
							if(ind < logArr.length) {
								double tmp1 = (logArr[ind - 1] + logArr[ind]) / 2.0;
								if(tmpTarget < tmp1) ind--;
							} else {
								ind--;
							}
						}
					}
					targetFreq.setTargetFreq(arr[ind], logArr[ind]);
					dial.setText(arr[ind]);
					lockFreq = true;													
				}
			}
			private String sigChar(int value, double scale) {
				int tmp = (int)((value + scale) / (scale / 8));
				return SIG_VALS[tmp];
			}
			class SigType {
				private double total;
				private int count;
				SigType(double inVal) {
					total = inVal;
					count = 1;
				}
				void addVal(double inVal) {
					total += inVal;
					count++;
				}
				double av() {
					return total / count;
				}
				int count() {
					return count;
				}
			}

		}

	} // SampleGetter

	// Extend MetalSliderUI to get sliders with arrows pointing 
	// left and up.
	private class ReversedMetalSliderUI extends MetalSliderUI {
		protected Icon upPointer = null;
		protected Icon leftPointer = null;
		public void installUI(JComponent c) {
			super.installUI(c);
			upPointer = new Pointer(false);
			leftPointer = new Pointer(true);
		}
		public void paintThumb(Graphics g)  {
			Rectangle rect = thumbRect;
			g.translate(rect.x, rect.y);
		   
			
			if (slider.getOrientation() == JSlider.HORIZONTAL) {
				upPointer.paintIcon(slider, g, 0, 0);
			}
			else {
				leftPointer.paintIcon(slider, g, 0, 0);
			}	

			g.translate(-rect.x, -rect.y);
		}
		private class Pointer implements Icon, Serializable, UIResource {
			protected Bobbles focusedBobbles;
			protected Bobbles unfocusedBobbles;

			private boolean isLeft;

			public Pointer(boolean isLeft) {
				this.isLeft = isLeft;
				focusedBobbles = new Bobbles(true, isLeft);
				unfocusedBobbles = new Bobbles(false, isLeft);
			}

			public void paintIcon( Component c, Graphics g, int x, int y) {
				JSlider slider = (JSlider)c;

				g.translate(x, y);

				// Draw the frame
				if (slider.hasFocus()) {
					g.setColor(MetalLookAndFeel.getPrimaryControlInfo());
				}
				else {
					g.setColor(slider.isEnabled() ? MetalLookAndFeel.getPrimaryControlInfo() :
								MetalLookAndFeel.getControlDarkShadow());
				}

				if(isLeft) {
					//   __
					//  /  |	This shape
					//  \__|
					//
					g.drawLine(  7,0  , 14,0  );
					g.drawLine( 15,1  , 15,13 );
					g.drawLine(  7,14 , 14,14 );
					g.drawLine(  0,7  ,  6,1  );
					g.drawLine(  0,7  ,  6,13 );
				} else {
					//  /\
					// |  |		This shape
					//  --							 
					g.drawLine( 1, 6 ,  7, 0);  //  /
					g.drawLine( 7, 0 , 14, 7);  //   \      //
					g.drawLine( 0,14 ,  0, 7);  // |
					g.drawLine(14,14 , 14, 7);  //    |
					g.drawLine( 1,15 , 13,15);  //  --
				}

				// fill
				if (slider.hasFocus()) {
					g.setColor(c.getForeground());
				} else {
					g.setColor(MetalLookAndFeel.getControl());
				}
			
				if(isLeft) {			

					g.fillRect(7,1,   8,13 );

					g.drawLine(6,3 , 6,12);
					g.drawLine(5,4 , 5,11);
					g.drawLine(4,5 , 4,10);
					g.drawLine(3,6 , 3,9);
					g.drawLine(2,7 , 2,8);
					// bobbles
					if (slider.isEnabled()) {
						if (slider.hasFocus()) {
							focusedBobbles.paintIcon(c, g, 8, 2);
						}
						else {
							unfocusedBobbles.paintIcon(c, g, 8, 2);
						}
					}
				} else {

					g.fillRect(1,7 , 13,8);
					g.drawLine(2,6 , 12,6);
					g.drawLine(3,5 , 11,5);
					g.drawLine(4,4 , 10,4);
					g.drawLine(5,3 ,  9,3);
					g.drawLine(6,2 ,  8,2);
					g.drawLine(7,1 ,  7,1);
					// bobbles
					if (slider.isEnabled()) {
						if (slider.hasFocus()) {
							focusedBobbles.paintIcon(c, g, 3, 8);
						} else {
							unfocusedBobbles.paintIcon(c, g, 3, 8);
						}
					}
	
				}
				// highlights
				if ( slider.isEnabled() ) {
					g.setColor( slider.hasFocus() ? MetalLookAndFeel.getPrimaryControl()
								: MetalLookAndFeel.getControlHighlight() );
					if(isLeft) {
						g.drawLine(8,1  , 14,1); 
						g.drawLine(1,7  ,  7,1); 
					} else {
						g.drawLine(1, 14, 13, 14);
						g.drawLine(1, 14, 1, 7);
					}
				}

				g.translate( -x, -y );
			}

			public int getIconWidth() {
				return isLeft ? 16 : 15;
			}

			public int getIconHeight() {
				return isLeft ? 15 : 16;
			}
		} // Pointer

		private class Bobbles implements Icon {

			static final int SIZE = 64;

			protected final int countX;
			protected final int countY;

			protected Buff buf;
    
			protected boolean isFocused = true;
			public Bobbles(boolean isFocused, boolean isVertical) {
				this.isFocused = isFocused;
				if(isVertical) {
					countX = 3; countY = 5;
				} else {
					countX = 5; countY = 3;
				}
			}
			public void paintIcon(Component c, Graphics g, int x, int y) {
				GraphicsConfiguration gc = (g instanceof Graphics2D) ?
					(GraphicsConfiguration)((Graphics2D)g).
					getDeviceConfiguration() : null;

				if(buf == null) buf = new Buff(gc, isFocused);

				int bufW = SIZE;
				int bufH = SIZE;
				int w = getIconWidth();
				int h = getIconHeight();
				int xx = x + w;
				int yy = y + h;
				int savex = x;

				while (y < yy) {
					int hh = Math.min(yy - y, bufH);
					for (x = savex; x < xx; x += bufW) {
						int ww = Math.min(xx - x, bufW);
						g.drawImage(buf.getImage(),
									x, y, x+ww, y+hh,
									0, 0, ww, hh,
									null);
					}
					y += bufH;
				}
			}

			public int getIconWidth() {
				return countX * 2;
			}

			public int getIconHeight() {
				return countY * 2;
			}

			private class Buff {

				transient Image image;
				Color t; 
				Color s;
				Color b;

				private GraphicsConfiguration gc;

				public Buff(GraphicsConfiguration gc, boolean isFocused) {
					this.gc = gc;
					if(isFocused) {
						t = MetalLookAndFeel.getPrimaryControl();
						s = MetalLookAndFeel.getPrimaryControlDarkShadow();
						b = MetalLookAndFeel.getPrimaryControlShadow();
					} else {
						t = MetalLookAndFeel.getControlHighlight();
						s = MetalLookAndFeel.getControlInfo();
						b = MetalLookAndFeel.getControl();
					}
					createImage();
					fillBuff();
				}

				public Image getImage() {
					return image;
				}

				private void fillBuff() {
					Graphics g = image.getGraphics();

					g.setColor(b);
					g.fillRect(0, 0, SIZE, SIZE);

					g.setColor(t);
					for (int x = 0; x < SIZE; x+=4) {
						for (int y = 0; y < SIZE; y+=4) {
							g.drawLine(x, y, x, y);
							g.drawLine(x+2, y+2, x+2, y+2);
						}
					}

					g.setColor(s);
					for (int x = 0; x < SIZE; x+=4) {
						for (int y = 0; y < SIZE; y+=4) {
							g.drawLine(x+1, y+1, x+1, y+1);
							g.drawLine(x+3, y+3, x+3, y+3);
						}
					}
					g.dispose();
				}

				private void createImage() {
					if (gc != null) {
						image = gc.createCompatibleImage(SIZE, SIZE);
					}
					else {
						int cmap[] = { b.getRGB(), t.getRGB(),
									   s.getRGB() };
						IndexColorModel icm = new IndexColorModel(8, 3, cmap, 0, false, -1,
																  DataBuffer.TYPE_BYTE);
						image = new BufferedImage(SIZE, SIZE,
												  BufferedImage.TYPE_BYTE_INDEXED, icm);
					}
				}

			}

		} // Bobbles

	} // ReversedMetalSliderUI
} // JTuner


