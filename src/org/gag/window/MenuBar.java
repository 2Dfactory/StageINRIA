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

package org.gag.window;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * This class create the menu bar for the viewing window.
 * It inherits from the class Menu.java
 * @author Andre GUENEY
 *
 */

public class MenuBar extends Menu{

	protected JMenuBar menuBar;
	
	/**
	 * Create the contents of the menu bar
	 * @param filePath The path to the file that contains the menu bar (MenuFile.xml)
	 * @param vv
	 */
	public MenuBar(String filePath, VisualizationViewer<?,?> vv, JFrame frame){
		
		this.vv = vv;
		this.file = new File(filePath);
		this.frame = frame;
		//Creation of a SAXBuilder instanciation
		SAXBuilder sx = new SAXBuilder();
				
		try {
			//On cr�e un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est termin� ;)
			menuFile = sx.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(menuFile.hasRootElement()){
			menuRoot = menuFile.getRootElement();
			this.menuBar = new JMenuBar();
			if(!menuRoot.getChildren().isEmpty()){
				char c;
				for(Element childElement : menuRoot.getChildren()){
					JMenu menu = new JMenu();
					menu = ReadMenuItem(childElement);
					c = menu.getText().charAt(0);
					menu.setMnemonic(c);
					this.menuBar.add(menu);				
				}	
			}
		}
				
	}
	
	/**
	 * 
	 * @return
	 */
	public JMenuBar getMenuBar(){
		return this.menuBar;
	}

}


