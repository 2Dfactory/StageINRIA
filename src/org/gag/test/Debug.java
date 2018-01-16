/* **************************************************************************************
 * 																						*
 *	Copyright (C) 2015  Andre GUENEY													*
 *																						*
 *  This library is free software; you can redistribute it and/or						*
 *  modify it under the terms of the GNU Lesser General Public							*
 *  License as published by the Free Software Foundation; either						*
 *  version 2.1 of the License, or (at your option) any later version.					*
 *																						*
 *  This library is distributed in the hope that it will be useful,						*
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of						*
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU					*
 *  Lesser General Public License for more details.										*
 *																						*
 *  You should have received a copy of the GNU Lesser General Public					*
 *  License along with this library; if not, write to the Free Software					*
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA		*
 *  																					*
 ****************************************************************************************/

package org.gag.test;

/**
 * Debug class
 * @author Andre GUENEY
 *
 */
public class Debug {

	private static boolean debug, visuDebug;
	
	public static boolean debug(){
		return debug;
	}
	
	public static void setDebug(boolean debug){
		Debug.debug = debug;
	}

	public static boolean visuDebug() {
		return visuDebug;
	}

	public static void setVisuDebug(boolean visuDebug) {
		//Debug.debug = debug;
		Debug.visuDebug = visuDebug;
	}
}
