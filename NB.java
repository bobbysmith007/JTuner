// NB.java
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

public final class NB {

	public final static double A1 = 27.5;
	public final static double A2 = 55.0;
	public final static double A3 = 110.0;
	public final static double A4 = 220.0;
	public final static double A5 = 440.0;
	public final static double A6 = 880.0;
	public final static double A7 = 1760.0;
	public final static double A8 = 3520.0;
	public final static double A9 = 7040.0;

	public final static double SEMI_TONE = Math.pow(2, 1.0/12.0);

	public final static double AS1 = A1 * Math.pow(SEMI_TONE, 1);
	public final static double B1 = A1 * Math.pow(SEMI_TONE, 2);
	public final static double C1 = A1 * Math.pow(SEMI_TONE, 3);
	public final static double CS1 = A1 * Math.pow(SEMI_TONE, 4);
	public final static double D1 = A1 * Math.pow(SEMI_TONE, 5);
	public final static double DS1 = A1 * Math.pow(SEMI_TONE, 6);
	public final static double E1 = A1 * Math.pow(SEMI_TONE, 7);
	public final static double F1 = A1 * Math.pow(SEMI_TONE, 8);
	public final static double FS1 = A1 * Math.pow(SEMI_TONE, 9);
	public final static double G1 = A1 * Math.pow(SEMI_TONE, 10);
	public final static double GS1 = A1 * Math.pow(SEMI_TONE, 11);

	public final static double AS2 = A2 * Math.pow(SEMI_TONE, 1);
	public final static double B2 = A2 * Math.pow(SEMI_TONE, 2);
	public final static double C2 = A2 * Math.pow(SEMI_TONE, 3);
	public final static double CS2 = A2 * Math.pow(SEMI_TONE, 4);
	public final static double D2 = A2 * Math.pow(SEMI_TONE, 5);
	public final static double DS2 = A2 * Math.pow(SEMI_TONE, 6);
	public final static double E2 = A2 * Math.pow(SEMI_TONE, 7);
	public final static double F2 = A2 * Math.pow(SEMI_TONE, 8);
	public final static double FS2 = A2 * Math.pow(SEMI_TONE, 9);
	public final static double G2 = A2 * Math.pow(SEMI_TONE, 10);
	public final static double GS2 = A2 * Math.pow(SEMI_TONE, 11);

	public final static double AS3 = A3 * Math.pow(SEMI_TONE, 1);
	public final static double B3 = A3 * Math.pow(SEMI_TONE, 2);
	public final static double C3 = A3 * Math.pow(SEMI_TONE, 3);
	public final static double CS3 = A3 * Math.pow(SEMI_TONE, 4);
	public final static double D3 = A3 * Math.pow(SEMI_TONE, 5);
	public final static double DS3 = A3 * Math.pow(SEMI_TONE, 6);
	public final static double E3 = A3 * Math.pow(SEMI_TONE, 7);
	public final static double F3 = A3 * Math.pow(SEMI_TONE, 8);
	public final static double FS3 = A3 * Math.pow(SEMI_TONE, 9);
	public final static double G3 = A3 * Math.pow(SEMI_TONE, 10);
	public final static double GS3 = A3 * Math.pow(SEMI_TONE, 11);

	public final static double AS4 = A4 * Math.pow(SEMI_TONE, 1);
	public final static double B4 = A4 * Math.pow(SEMI_TONE, 2);
	public final static double C4 = A4 * Math.pow(SEMI_TONE, 3);
	public final static double CS4 = A4 * Math.pow(SEMI_TONE, 4);
	public final static double D4 = A4 * Math.pow(SEMI_TONE, 5);
	public final static double DS4 = A4 * Math.pow(SEMI_TONE, 6);
	public final static double E4 = A4 * Math.pow(SEMI_TONE, 7);
	public final static double F4 = A4 * Math.pow(SEMI_TONE, 8);
	public final static double FS4 = A4 * Math.pow(SEMI_TONE, 9);
	public final static double G4 = A4 * Math.pow(SEMI_TONE, 10);
	public final static double GS4 = A4 * Math.pow(SEMI_TONE, 11);

	public final static double AS5 = A5 * Math.pow(SEMI_TONE, 1);
	public final static double B5 = A5 * Math.pow(SEMI_TONE, 2);
	public final static double C5 = A5 * Math.pow(SEMI_TONE, 3);
	public final static double CS5 = A5 * Math.pow(SEMI_TONE, 4);
	public final static double D5 = A5 * Math.pow(SEMI_TONE, 5);
	public final static double DS5 = A5 * Math.pow(SEMI_TONE, 6);
	public final static double E5 = A5 * Math.pow(SEMI_TONE, 7);
	public final static double F5 = A5 * Math.pow(SEMI_TONE, 8);
	public final static double FS5 = A5 * Math.pow(SEMI_TONE, 9);
	public final static double G5 = A5 * Math.pow(SEMI_TONE, 10);
	public final static double GS5 = A5 * Math.pow(SEMI_TONE, 11);

	public final static double AS6 = A6 * Math.pow(SEMI_TONE, 1);
	public final static double B6 = A6 * Math.pow(SEMI_TONE, 2);
	public final static double C6 = A6 * Math.pow(SEMI_TONE, 3);
	public final static double CS6 = A6 * Math.pow(SEMI_TONE, 4);
	public final static double D6 = A6 * Math.pow(SEMI_TONE, 5);
	public final static double DS6 = A6 * Math.pow(SEMI_TONE, 6);
	public final static double E6 = A6 * Math.pow(SEMI_TONE, 7);
	public final static double F6 = A6 * Math.pow(SEMI_TONE, 8);
	public final static double FS6 = A6 * Math.pow(SEMI_TONE, 9);
	public final static double G6 = A6 * Math.pow(SEMI_TONE, 10);
	public final static double GS6 = A6 * Math.pow(SEMI_TONE, 11);

	public final static double AS7 = A7 * Math.pow(SEMI_TONE, 1);
	public final static double B7 = A7 * Math.pow(SEMI_TONE, 2);
	public final static double C7 = A7 * Math.pow(SEMI_TONE, 3);
	public final static double CS7 = A7 * Math.pow(SEMI_TONE, 4);
	public final static double D7 = A7 * Math.pow(SEMI_TONE, 5);
	public final static double DS7 = A7 * Math.pow(SEMI_TONE, 6);
	public final static double E7 = A7 * Math.pow(SEMI_TONE, 7);
	public final static double F7 = A7 * Math.pow(SEMI_TONE, 8);
	public final static double FS7 = A7 * Math.pow(SEMI_TONE, 9);
	public final static double G7 = A7 * Math.pow(SEMI_TONE, 10);
	public final static double GS7 = A7 * Math.pow(SEMI_TONE, 11);

	public final static double AS8 = A8 * Math.pow(SEMI_TONE, 1);
	public final static double B8 = A8 * Math.pow(SEMI_TONE, 2);
	public final static double C8 = A8 * Math.pow(SEMI_TONE, 3);
	public final static double CS8 = A8 * Math.pow(SEMI_TONE, 4);
	public final static double D8 = A8 * Math.pow(SEMI_TONE, 5);
	public final static double DS8 = A8 * Math.pow(SEMI_TONE, 6);
	public final static double E8 = A8 * Math.pow(SEMI_TONE, 7);
	public final static double F8 = A8 * Math.pow(SEMI_TONE, 8);
	public final static double FS8 = A8 * Math.pow(SEMI_TONE, 9);
	public final static double G8 = A8 * Math.pow(SEMI_TONE, 10);
	public final static double GS8 = A8 * Math.pow(SEMI_TONE, 11);

	public final static double AS9 = A9 * Math.pow(SEMI_TONE, 1);
	public final static double B9 = A9 * Math.pow(SEMI_TONE, 2);
	public final static double C9 = A9 * Math.pow(SEMI_TONE, 3);
	public final static double CS9 = A9 * Math.pow(SEMI_TONE, 4);
	public final static double D9 = A9 * Math.pow(SEMI_TONE, 5);
	public final static double DS9 = A9 * Math.pow(SEMI_TONE, 6);
	public final static double E9 = A9 * Math.pow(SEMI_TONE, 7);
	public final static double F9 = A9 * Math.pow(SEMI_TONE, 8);
	public final static double FS9 = A9 * Math.pow(SEMI_TONE, 9);
	public final static double G9 = A9 * Math.pow(SEMI_TONE, 10);
	public final static double GS9 = A9 * Math.pow(SEMI_TONE, 11);

	//--------------------------------------------

	public final static double LOG_A1 = Math.log(A1);
	public final static double LOG_AS1 = Math.log(AS1);
	public final static double LOG_B1 = Math.log(B1);
	public final static double LOG_C1 = Math.log(C1);
	public final static double LOG_CS1 = Math.log(CS1);
	public final static double LOG_D1 = Math.log(D1);
	public final static double LOG_DS1 = Math.log(DS1);
	public final static double LOG_E1 = Math.log(E1);
	public final static double LOG_F1 = Math.log(F1);
	public final static double LOG_FS1 = Math.log(FS1);
	public final static double LOG_G1 = Math.log(G1);
	public final static double LOG_GS1 = Math.log(GS1);

	public final static double LOG_A2 = Math.log(A2);
	public final static double LOG_AS2 = Math.log(AS2);
	public final static double LOG_B2 = Math.log(B2);
	public final static double LOG_C2 = Math.log(C2);
	public final static double LOG_CS2 = Math.log(CS2);
	public final static double LOG_D2 = Math.log(D2);
	public final static double LOG_DS2 = Math.log(DS2);
	public final static double LOG_E2 = Math.log(E2);
	public final static double LOG_F2 = Math.log(F2);
	public final static double LOG_FS2 = Math.log(FS2);
	public final static double LOG_G2 = Math.log(G2);
	public final static double LOG_GS2 = Math.log(GS2);

	public final static double LOG_A3 = Math.log(A3);
	public final static double LOG_AS3 = Math.log(AS3);
	public final static double LOG_B3 = Math.log(B3);
	public final static double LOG_C3 = Math.log(C3);
	public final static double LOG_CS3 = Math.log(CS3);
	public final static double LOG_D3 = Math.log(D3);
	public final static double LOG_DS3 = Math.log(DS3);
	public final static double LOG_E3 = Math.log(E3);
	public final static double LOG_F3 = Math.log(F3);
	public final static double LOG_FS3 = Math.log(FS3);
	public final static double LOG_G3 = Math.log(G3);
	public final static double LOG_GS3 = Math.log(GS3);

	public final static double LOG_A4 = Math.log(A4);
	public final static double LOG_AS4 = Math.log(AS4);
	public final static double LOG_B4 = Math.log(B4);
	public final static double LOG_C4 = Math.log(C4);
	public final static double LOG_CS4 = Math.log(CS4);
	public final static double LOG_D4 = Math.log(D4);
	public final static double LOG_DS4 = Math.log(DS4);
	public final static double LOG_E4 = Math.log(E4);
	public final static double LOG_F4 = Math.log(F4);
	public final static double LOG_FS4 = Math.log(FS4);
	public final static double LOG_G4 = Math.log(G4);
	public final static double LOG_GS4 = Math.log(GS4);

	public final static double LOG_A5 = Math.log(A5);
	public final static double LOG_AS5 = Math.log(AS5);
	public final static double LOG_B5 = Math.log(B5);
	public final static double LOG_C5 = Math.log(C5);
	public final static double LOG_CS5 = Math.log(CS5);
	public final static double LOG_D5 = Math.log(D5);
	public final static double LOG_DS5 = Math.log(DS5);
	public final static double LOG_E5 = Math.log(E5);
	public final static double LOG_F5 = Math.log(F5);
	public final static double LOG_FS5 = Math.log(FS5);
	public final static double LOG_G5 = Math.log(G5);
	public final static double LOG_GS5 = Math.log(GS5);

	public final static double LOG_A6 = Math.log(A6);
	public final static double LOG_AS6 = Math.log(AS6);
	public final static double LOG_B6 = Math.log(B6);
	public final static double LOG_C6 = Math.log(C6);
	public final static double LOG_CS6 = Math.log(CS6);
	public final static double LOG_D6 = Math.log(D6);
	public final static double LOG_DS6 = Math.log(DS6);
	public final static double LOG_E6 = Math.log(E6);
	public final static double LOG_F6 = Math.log(F6);
	public final static double LOG_FS6 = Math.log(FS6);
	public final static double LOG_G6 = Math.log(G6);
	public final static double LOG_GS6 = Math.log(GS6);

	public final static double LOG_A7 = Math.log(A7);
	public final static double LOG_AS7 = Math.log(AS7);
	public final static double LOG_B7 = Math.log(B7);
	public final static double LOG_C7 = Math.log(C7);
	public final static double LOG_CS7 = Math.log(CS7);
	public final static double LOG_D7 = Math.log(D7);
	public final static double LOG_DS7 = Math.log(DS7);
	public final static double LOG_E7 = Math.log(E7);
	public final static double LOG_F7 = Math.log(F7);
	public final static double LOG_FS7 = Math.log(FS7);
	public final static double LOG_G7 = Math.log(G7);
	public final static double LOG_GS7 = Math.log(GS7);

	public final static double LOG_A8 = Math.log(A8);
	public final static double LOG_AS8 = Math.log(AS8);
	public final static double LOG_B8 = Math.log(B8);
	public final static double LOG_C8 = Math.log(C8);
	public final static double LOG_CS8 = Math.log(CS8);
	public final static double LOG_D8 = Math.log(D8);
	public final static double LOG_DS8 = Math.log(DS8);
	public final static double LOG_E8 = Math.log(E8);
	public final static double LOG_F8 = Math.log(F8);
	public final static double LOG_FS8 = Math.log(FS8);
	public final static double LOG_G8 = Math.log(G8);
	public final static double LOG_GS8 = Math.log(GS8);

	public final static double LOG_A9 = Math.log(A9);
	public final static double LOG_AS9 = Math.log(AS9);
	public final static double LOG_B9 = Math.log(B9);
	public final static double LOG_C9 = Math.log(C9);
	public final static double LOG_CS9 = Math.log(CS9);
	public final static double LOG_D9 = Math.log(D9);
	public final static double LOG_DS9 = Math.log(DS9);
	public final static double LOG_E9 = Math.log(E9);
	public final static double LOG_F9 = Math.log(F9);
	public final static double LOG_FS9 = Math.log(FS9);
	public final static double LOG_G9 = Math.log(G9);
	public final static double LOG_GS9 = Math.log(GS9);

	//--------------------------------------------

	public final static double R_A1 = Math.round(A1 * 1000.0) / 1000.0;
	public final static double R_AS1 = Math.round(AS1 * 1000.0) / 1000.0;
	public final static double R_B1 = Math.round(B1 * 1000.0) / 1000.0;
	public final static double R_C1 = Math.round(C1 * 1000.0) / 1000.0;
	public final static double R_CS1 = Math.round(CS1 * 1000.0) / 1000.0;
	public final static double R_D1 = Math.round(D1 * 1000.0) / 1000.0;
	public final static double R_DS1 = Math.round(DS1 * 1000.0) / 1000.0;
	public final static double R_E1 = Math.round(E1 * 1000.0) / 1000.0;
	public final static double R_F1 = Math.round(F1 * 1000.0) / 1000.0;
	public final static double R_FS1 = Math.round(FS1 * 1000.0) / 1000.0;
	public final static double R_G1 = Math.round(G1 * 1000.0) / 1000.0;
	public final static double R_GS1 = Math.round(GS1 * 1000.0) / 1000.0;

	public final static double R_A2 = Math.round(A2 * 1000.0) / 1000.0;
	public final static double R_AS2 = Math.round(AS2 * 1000.0) / 1000.0;
	public final static double R_B2 = Math.round(B2 * 1000.0) / 1000.0;
	public final static double R_C2 = Math.round(C2 * 1000.0) / 1000.0;
	public final static double R_CS2 = Math.round(CS2 * 1000.0) / 1000.0;
	public final static double R_D2 = Math.round(D2 * 1000.0) / 1000.0;
	public final static double R_DS2 = Math.round(DS2 * 1000.0) / 1000.0;
	public final static double R_E2 = Math.round(E2 * 1000.0) / 1000.0;
	public final static double R_F2 = Math.round(F2 * 1000.0) / 1000.0;
	public final static double R_FS2 = Math.round(FS2 * 1000.0) / 1000.0;
	public final static double R_G2 = Math.round(G2 * 1000.0) / 1000.0;
	public final static double R_GS2 = Math.round(GS2 * 1000.0) / 1000.0;

	public final static double R_A3 = Math.round(A3 * 1000.0) / 1000.0;
	public final static double R_AS3 = Math.round(AS3 * 1000.0) / 1000.0;
	public final static double R_B3 = Math.round(B3 * 1000.0) / 1000.0;
	public final static double R_C3 = Math.round(C3 * 1000.0) / 1000.0;
	public final static double R_CS3 = Math.round(CS3 * 1000.0) / 1000.0;
	public final static double R_D3 = Math.round(D3 * 1000.0) / 1000.0;
	public final static double R_DS3 = Math.round(DS3 * 1000.0) / 1000.0;
	public final static double R_E3 = Math.round(E3 * 1000.0) / 1000.0;
	public final static double R_F3 = Math.round(F3 * 1000.0) / 1000.0;
	public final static double R_FS3 = Math.round(FS3 * 1000.0) / 1000.0;
	public final static double R_G3 = Math.round(G3 * 1000.0) / 1000.0;
	public final static double R_GS3 = Math.round(GS3 * 1000.0) / 1000.0;

	public final static double R_A4 = Math.round(A4 * 1000.0) / 1000.0;
	public final static double R_AS4 = Math.round(AS4 * 1000.0) / 1000.0;
	public final static double R_B4 = Math.round(B4 * 1000.0) / 1000.0;
	public final static double R_C4 = Math.round(C4 * 1000.0) / 1000.0;
	public final static double R_CS4 = Math.round(CS4 * 1000.0) / 1000.0;
	public final static double R_D4 = Math.round(D4 * 1000.0) / 1000.0;
	public final static double R_DS4 = Math.round(DS4 * 1000.0) / 1000.0;
	public final static double R_E4 = Math.round(E4 * 1000.0) / 1000.0;
	public final static double R_F4 = Math.round(F4 * 1000.0) / 1000.0;
	public final static double R_FS4 = Math.round(FS4 * 1000.0) / 1000.0;
	public final static double R_G4 = Math.round(G4 * 1000.0) / 1000.0;
	public final static double R_GS4 = Math.round(GS4 * 1000.0) / 1000.0;

	public final static double R_A5 = Math.round(A5 * 1000.0) / 1000.0;
	public final static double R_AS5 = Math.round(AS5 * 1000.0) / 1000.0;
	public final static double R_B5 = Math.round(B5 * 1000.0) / 1000.0;
	public final static double R_C5 = Math.round(C5 * 1000.0) / 1000.0;
	public final static double R_CS5 = Math.round(CS5 * 1000.0) / 1000.0;
	public final static double R_D5 = Math.round(D5 * 1000.0) / 1000.0;
	public final static double R_DS5 = Math.round(DS5 * 1000.0) / 1000.0;
	public final static double R_E5 = Math.round(E5 * 1000.0) / 1000.0;
	public final static double R_F5 = Math.round(F5 * 1000.0) / 1000.0;
	public final static double R_FS5 = Math.round(FS5 * 1000.0) / 1000.0;
	public final static double R_G5 = Math.round(G5 * 1000.0) / 1000.0;
	public final static double R_GS5 = Math.round(GS5 * 1000.0) / 1000.0;

	public final static double R_A6 = Math.round(A6 * 1000.0) / 1000.0;
	public final static double R_AS6 = Math.round(AS6 * 1000.0) / 1000.0;
	public final static double R_B6 = Math.round(B6 * 1000.0) / 1000.0;
	public final static double R_C6 = Math.round(C6 * 1000.0) / 1000.0;
	public final static double R_CS6 = Math.round(CS6 * 1000.0) / 1000.0;
	public final static double R_D6 = Math.round(D6 * 1000.0) / 1000.0;
	public final static double R_DS6 = Math.round(DS6 * 1000.0) / 1000.0;
	public final static double R_E6 = Math.round(E6 * 1000.0) / 1000.0;
	public final static double R_F6 = Math.round(F6 * 1000.0) / 1000.0;
	public final static double R_FS6 = Math.round(FS6 * 1000.0) / 1000.0;
	public final static double R_G6 = Math.round(G6 * 1000.0) / 1000.0;
	public final static double R_GS6 = Math.round(GS6 * 1000.0) / 1000.0;

	public final static double R_A7 = Math.round(A7 * 1000.0) / 1000.0;
	public final static double R_AS7 = Math.round(AS7 * 1000.0) / 1000.0;
	public final static double R_B7 = Math.round(B7 * 1000.0) / 1000.0;
	public final static double R_C7 = Math.round(C7 * 1000.0) / 1000.0;
	public final static double R_CS7 = Math.round(CS7 * 1000.0) / 1000.0;
	public final static double R_D7 = Math.round(D7 * 1000.0) / 1000.0;
	public final static double R_DS7 = Math.round(DS7 * 1000.0) / 1000.0;
	public final static double R_E7 = Math.round(E7 * 1000.0) / 1000.0;
	public final static double R_F7 = Math.round(F7 * 1000.0) / 1000.0;
	public final static double R_FS7 = Math.round(FS7 * 1000.0) / 1000.0;
	public final static double R_G7 = Math.round(G7 * 1000.0) / 1000.0;
	public final static double R_GS7 = Math.round(GS7 * 1000.0) / 1000.0;

	public final static double R_A8 = Math.round(A8 * 1000.0) / 1000.0;
	public final static double R_AS8 = Math.round(AS8 * 1000.0) / 1000.0;
	public final static double R_B8 = Math.round(B8 * 1000.0) / 1000.0;
	public final static double R_C8 = Math.round(C8 * 1000.0) / 1000.0;
	public final static double R_CS8 = Math.round(CS8 * 1000.0) / 1000.0;
	public final static double R_D8 = Math.round(D8 * 1000.0) / 1000.0;
	public final static double R_DS8 = Math.round(DS8 * 1000.0) / 1000.0;
	public final static double R_E8 = Math.round(E8 * 1000.0) / 1000.0;
	public final static double R_F8 = Math.round(F8 * 1000.0) / 1000.0;
	public final static double R_FS8 = Math.round(FS8 * 1000.0) / 1000.0;
	public final static double R_G8 = Math.round(G8 * 1000.0) / 1000.0;
	public final static double R_GS8 = Math.round(GS8 * 1000.0) / 1000.0;

	public final static double R_A9 = Math.round(A9 * 1000.0) / 1000.0;
	public final static double R_AS9 = Math.round(AS9 * 1000.0) / 1000.0;
	public final static double R_B9 = Math.round(B9 * 1000.0) / 1000.0;
	public final static double R_C9 = Math.round(C9 * 1000.0) / 1000.0;
	public final static double R_CS9 = Math.round(CS9 * 1000.0) / 1000.0;
	public final static double R_D9 = Math.round(D9 * 1000.0) / 1000.0;
	public final static double R_DS9 = Math.round(DS9 * 1000.0) / 1000.0;
	public final static double R_E9 = Math.round(E9 * 1000.0) / 1000.0;
	public final static double R_F9 = Math.round(F9 * 1000.0) / 1000.0;
	public final static double R_FS9 = Math.round(FS9 * 1000.0) / 1000.0;
	public final static double R_G9 = Math.round(G9 * 1000.0) / 1000.0;
	public final static double R_GS9 = Math.round(GS9 * 1000.0) / 1000.0;

	public final static String[] NOTE_NAMES = { "A1", "A#1", "B1", "C1", "C#1", "D1", "D#1", "E1", "F1", "F#1", "G1", "G#1", 
									"A2", "A#2", "B2", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", 
									"A3", "A#3", "B3", "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", 
									"A4", "A#4", "B4", "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", 
									"A5", "A#5", "B5", "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", 
									"A6", "A#6", "B6", "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", 
									"A7", "A#7", "B7", "C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", 
									"A8", "A#8", "B8", "C8", "C#8", "D8", "D#8", "E8", "F8", "F#8", "G8", "G#8", 
									"A9", "A#9", "B9", "C9", "C#9", "D9", "D#9" }; //, "E9", "F9", "F#9", "G9", "G#9" };

	public final static double[] NOTES = { A1, AS1, B1, C1, CS1, D1, DS1, E1, F1, FS1, G1, GS1, 
									A2, AS2, B2, C2, CS2, D2, DS2, E2, F2, FS2, G2, GS2, 
									A3, AS3, B3, C3, CS3, D3, DS3, E3, F3, FS3, G3, GS3, 
									A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4, 
									A5, AS5, B5, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5, 
									A6, AS6, B6, C6, CS6, D6, DS6, E6, F6, FS6, G6, GS6, 
									A7, AS7, B7, C7, CS7, D7, DS7, E7, F7, FS7, G7, GS7, 
									A8, AS8, B8, C8, CS8, D8, DS8, E8, F8, FS8, G8, GS8, 
									A9, AS9, B9, C9, CS9, D9, DS9 }; //, E9, F9, FS9, G9, GS9 };

	public final static double[] LOG_NOTES = { LOG_A1, LOG_AS1, LOG_B1, LOG_C1, LOG_CS1, LOG_D1, LOG_DS1, LOG_E1, LOG_F1, LOG_FS1, LOG_G1, LOG_GS1, 
									LOG_A2, LOG_AS2, LOG_B2, LOG_C2, LOG_CS2, LOG_D2, LOG_DS2, LOG_E2, LOG_F2, LOG_FS2, LOG_G2, LOG_GS2, 
									LOG_A3, LOG_AS3, LOG_B3, LOG_C3, LOG_CS3, LOG_D3, LOG_DS3, LOG_E3, LOG_F3, LOG_FS3, LOG_G3, LOG_GS3, 
									LOG_A4, LOG_AS4, LOG_B4, LOG_C4, LOG_CS4, LOG_D4, LOG_DS4, LOG_E4, LOG_F4, LOG_FS4, LOG_G4, LOG_GS4, 
									LOG_A5, LOG_AS5, LOG_B5, LOG_C5, LOG_CS5, LOG_D5, LOG_DS5, LOG_E5, LOG_F5, LOG_FS5, LOG_G5, LOG_GS5, 
									LOG_A6, LOG_AS6, LOG_B6, LOG_C6, LOG_CS6, LOG_D6, LOG_DS6, LOG_E6, LOG_F6, LOG_FS6, LOG_G6, LOG_GS6, 
									LOG_A7, LOG_AS7, LOG_B7, LOG_C7, LOG_CS7, LOG_D7, LOG_DS7, LOG_E7, LOG_F7, LOG_FS7, LOG_G7, LOG_GS7, 
									LOG_A8, LOG_AS8, LOG_B8, LOG_C8, LOG_CS8, LOG_D8, LOG_DS8, LOG_E8, LOG_F8, LOG_FS8, LOG_G8, LOG_GS8, 
									LOG_A9, LOG_AS9, LOG_B9, LOG_C9, LOG_CS9, LOG_D9, LOG_DS9 }; //, LOG_E9, LOG_F9, LOG_FS9, LOG_G9, LOG_GS9 };

	public final static double[] R_NOTES = { R_A1, R_AS1, R_B1, R_C1, R_CS1, R_D1, R_DS1, R_E1, R_F1, R_FS1, R_G1, R_GS1, 
									R_A2, R_AS2, R_B2, R_C2, R_CS2, R_D2, R_DS2, R_E2, R_F2, R_FS2, R_G2, R_GS2, 
									R_A3, R_AS3, R_B3, R_C3, R_CS3, R_D3, R_DS3, R_E3, R_F3, R_FS3, R_G3, R_GS3, 
									R_A4, R_AS4, R_B4, R_C4, R_CS4, R_D4, R_DS4, R_E4, R_F4, R_FS4, R_G4, R_GS4, 
									R_A5, R_AS5, R_B5, R_C5, R_CS5, R_D5, R_DS5, R_E5, R_F5, R_FS5, R_G5, R_GS5, 
									R_A6, R_AS6, R_B6, R_C6, R_CS6, R_D6, R_DS6, R_E6, R_F6, R_FS6, R_G6, R_GS6, 
									R_A7, R_AS7, R_B7, R_C7, R_CS7, R_D7, R_DS7, R_E7, R_F7, R_FS7, R_G7, R_GS7, 
									R_A8, R_AS8, R_B8, R_C8, R_CS8, R_D8, R_DS8, R_E8, R_F8, R_FS8, R_G8, R_GS8, 
									R_A9, R_AS9, R_B9, R_C9, R_CS9, R_D9, R_DS9 }; //, R_E9, R_F9, R_FS9, R_G9, R_GS9 };

	public final static double[] GUITAR_NOTES = {E2, A3, D3, E3, G3, A4, B4, D4, E4, G4, B5, E5};
												 
	public final static double[] LOG_GUITAR_NOTES = {LOG_E2, LOG_A3, LOG_D3, LOG_E3, LOG_G3, LOG_A4, 
													 LOG_B4, LOG_D4, LOG_E4, LOG_G4, LOG_B5, LOG_E5};
	
	
}


