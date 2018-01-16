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

import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * This class create the default popup menu for the viewing window.
 * It inherits from the class Menu.java
 * @author Andre GUENEY
 *
 */
public class PopUpMenu extends Menu{
	
	protected JPopupMenu popUpMenu;
	
	/**
	 * Create the content of the popup menu
	 * @param popUpMenuFilePath
	 * @param vv
	 */
	public PopUpMenu(String popUpMenuFilePath, VisualizationViewer<?,?> vv){
		
		this.vv = vv;
		this.file = new File(popUpMenuFilePath);
		SAXBuilder sx = new SAXBuilder();
		
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est terminé ;)
			menuFile = sx.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		if(menuFile.hasRootElement()){
			menuRoot = menuFile.getRootElement();
			this.popUpMenu = new JPopupMenu();
			if(!menuRoot.getChildren().isEmpty()){
				char c;
				for(Element childElement : menuRoot.getChildren()){
					JMenu menu = new JMenu();
					menu = ReadMenuItem(childElement);
					c = menu.getText().charAt(0);
					menu.setMnemonic(c);
					this.popUpMenu.add(menu);				
				}	
			}
		}
	}
	
	public JPopupMenu getPopUpMenu(){
		return this.popUpMenu;
	}
	
}
