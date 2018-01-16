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

import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdom2.Element;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * This class create the menu contains in the menu bar from the contents of the MenuFile.xml
 * 
 * @author Andre GUENEY
 *
 */
public class Menu {

	static org.jdom2.Document menuFile;
	static Element menuRoot;
	
	protected File file;
	protected VisualizationViewer<?,?> vv;
	protected JFrame frame;
	
	public Menu(){
	}
	
	/**
	 * Read the contents of the file and create the associated menu
	 * @param element
	 * @return
	 */
	public JMenu ReadMenuItem(Element element){
		
		JMenu newMenu = new JMenu(element.getName());
		JMenuItem newItem;
		
		String str = "";
		
		if(!element.getChildren().isEmpty()){
				for(Element childElement : element.getChildren()){
					if(childElement.getChildren().isEmpty()) {
						if(childElement.getName().contains("Separator")) {
							newMenu.addSeparator();
						} else	{
							if(childElement.getName().contains("_")){
								str=childElement.getName();
								str=str.replace("_", " ");
							} else str = childElement.getName();
							newItem = new JMenuItem(str);
							if(!childElement.getAttributes().isEmpty()){
								 	// create a shortcut key for some menu
								newItem.setAccelerator(KeyStroke.getKeyStroke(childElement.getAttribute("QuickKey")
										.getValue().charAt(0), KeyEvent.SHIFT_DOWN_MASK));
							}
							MenuActions.Actions(newItem,vv, frame);
							newMenu.add(newItem);
						}
					} else 	newMenu.add(ReadMenuItem(childElement));
				}
			}
		return newMenu;
	}
	
}
