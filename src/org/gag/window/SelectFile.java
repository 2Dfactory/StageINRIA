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

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * This class open a select file window
 * @author Andre GUENEY
 *
 */
public class SelectFile {
	
	public static File selectFile(Point p){
		
		FileSystemView vueSysteme = FileSystemView.getFileSystemView();		
		
		File home = vueSysteme.getHomeDirectory();

		JFileChooser chooser = new JFileChooser(home);
		
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		//Choose only files

		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml"); //Add filter on extension
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);
		
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("You haven\'t choose a file");
			return null;
		}
		
		File file = chooser.getSelectedFile();

		String nomFichier = file.getName().toLowerCase();
		
		if(!file.exists()) {
			try {
				if(nomFichier.endsWith("xml")) file.createNewFile();
				else{
					String str = file.getAbsolutePath()+".xml";
					File tempo = new File(str);
					tempo.createNewFile();
					return tempo;
				}
			} catch (IOException e) {
				System.err.println("Can't create the save file!!!!");
				e.printStackTrace();
				return null;
			}

		}
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(nomFichier.endsWith("xml")){
				System.out.println("You choose to open this file: " + chooser.getSelectedFile().getName());
				return file;
			}			
			else System.out.println("You choose the wrong file. You must choose a file with a .xml extension");
		}
		
		return null;
	}
}
