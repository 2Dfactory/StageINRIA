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
import java.util.List;

import javax.swing.JFrame;

import org.gag.production.Production;

import edu.uci.ics.jung.graph.Graph;

/**
 * Create an inherited JFrame that store the current list of Production
 * and the file that contains the grammar
 * @author Andre GUENEY
 *
 */
public class GAGframe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -678712599475295265L;
	protected Graph<?,?> graph=null;
	protected File file=null;
	protected List<Production> listProd = null;
	
	public GAGframe(){
		super();
	}
	
	public Graph<?,?> getGraph(){
		return this.graph;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public void setGraph(Graph<?,?> graph){
		this.graph = graph;
	}
	
	public List<Production> getListProduction(){
		return this.listProd;
	}
	
	public void setListProduction(List<Production> listProduction){
		this.listProd = listProduction;
	}
	
}
