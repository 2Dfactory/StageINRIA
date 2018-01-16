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

package Main;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gag.node.Node;
import org.gag.production.Production;
import org.gag.production.Task;
import org.gag.term.Substitution;
import org.gag.test.Debug;
import org.gag.window.GAGwindow;
import org.gag.xml.ReadXMLProdFile;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class check the path for the files used to create the menu, create a test list of production,
 * create a tree from the production and launch the visualization window.
 * @author Andre GUENEY
 *
 */
public class Start {
	
	public static void start(){
		String sep = System.getProperty("file.separator");
		String homeName = System.getProperty("user.home");
		String filesRoot = homeName+sep+"git"+sep+"gag-implem"+sep+"gag_git"+sep+"XML files";
		String pathMenu = filesRoot+sep+"MenuFile.xml";
		String pathPopUpMenu = filesRoot+sep+"PopUpMenuFile.xml";
		
		/* *********************Part to be removed********************** */
		String pathName = filesRoot+sep+"GAGprod.xml";

		Node rootNode= null;
		
		List<Production> listProd = new LinkedList<Production>();
		
		/*-----------------------------Variables de debuggage--------------------------------------------------------*/
		Debug.setDebug(false);
		Debug.setVisuDebug(false);
		/*-----------------------------Creation du contenu de la production test-------------------------------------*/
		/*-----------------------------Utilisation d'un fichier GAG pour construire la liste des productions---------*/

		
		if(Debug.debug()){
			//File file = new File("C:\\Users\\agueney\\git\\gag-implem\\gag_git\\XML files\\GAGprod.xml");
			File file = new File(pathName);
			
			ReadXMLProdFile test = new ReadXMLProdFile(file);
			
			listProd = test.createListOfProductions();
			
			for(Production prod : listProd){
				System.out.println(prod);	

			}
		}
		
		/*-----------------------------Creation du contenu de la tache du noeud test-------------------------------------*/
		
		if(Debug.debug()){
			
			Task nodeTask=null;
			
			for(Production prod : listProd){
				if(prod.getProdSort().getSortName().equals("Root")){
					for(Task task : prod.getTaskList()){
						nodeTask = task.getClonedTask();
					}
				}
			}

			rootNode = new Node(null,null,nodeTask, true);
			
			/*-----------------------------Affichage du contenu du noeud construit-------------------------------------*/

			System.out.println("**************************Root Node*******************************");
			System.out.println("Node ID : "+rootNode.isRootNode().getNodeID());
			System.out.println("**************************Contenu du noeud test*******************************");
			System.out.println(rootNode);
			
		}
	
		/*-----------------------------Creation test patternMatching + affichage contenu substitution-------------------------------------*/
		
		if(Debug.debug()){
			Map<Production, Substitution> mapContent = new HashMap<Production, Substitution>();
			mapContent = rootNode.PatternMatching(listProd);
			
			for(Entry<Production, Substitution> mapEntries : mapContent.entrySet()){
				System.out.println("Prod applicable : \n"+mapEntries.getKey());
				System.out.println("Substitution calculée : \n\t"+mapEntries.getValue());
				rootNode.applyProd(mapEntries.getKey(), mapEntries.getValue());
			}

			System.out.println("**************************Contenu du noeud test*******************************");
			System.out.println(rootNode);
			
			System.out.println("**************************Liste des noeud ouvert*******************************");
			for(Node node : rootNode.getListOfOpenNodes()){
				System.out.println("Open NodeID : "+node.getNodeID());
			}
			System.out.println("**************************Fin liste des noeud ouvert*******************************");

			for(Node node : rootNode.getChildNodeList()){
				mapContent = node.PatternMatching(listProd);
				for(Entry<Production, Substitution> mapEntries : mapContent.entrySet()){
					System.out.println("Prod applicable : \n"+mapEntries.getKey());
					System.out.println("Substitution calculée : \n\t"+mapEntries.getValue());
					node.applyProd(mapEntries.getKey(), mapEntries.getValue());
				}

				System.out.println("**************************Contenu du noeud test*******************************");
				System.out.println(node);
				
				System.out.println("**************************Liste des noeud ouvert*******************************");
				for(Node openNode : node.getListOfOpenNodes()){
					System.out.println("Open NodeID : "+openNode.getNodeID());
				}
				System.out.println("**************************Fin liste des noeud ouvert*******************************");
			}
		}
		
		/*-----------------------------Test Affichage -------------------------------------*/
			
			//---------------------------Graphical view of the tree node created before-------------------------
		
		
			if(Debug.visuDebug()){

				Dimension dim = new Dimension();
				dim.setSize(600, 600);
				
				Graph<Node, String> graph = new DelegateForest<Node, String>();
				
				graph.addVertex(rootNode);
				System.out.println(rootNode.getChildNodeList());
				createTree(rootNode, graph);
				
				GAGwindow window = new GAGwindow();
				
				window.createWindow(pathMenu, pathPopUpMenu, dim, graph);
				
			}
			else {
				/* *********************End part to be removed********************** */
				
				
				/* ***********************Part to be keep********************** */
				Dimension dim = new Dimension();
				dim.setSize(600, 600);
				
				Graph<Node, String> graph = new DelegateForest<Node, String>();
				GAGwindow window = new GAGwindow();
				
				window.createWindow(pathMenu, pathPopUpMenu, dim, graph);
				
				/* *********************End part to be keep********************** */
				
			}
	}
	
	// Must be removed when the debugging phase will be ok.
	public static void createTree(Node node, Graph<Node, String> graph){
		String str=null;
		if(!node.getChildNodeList().isEmpty()){
			for(Node childNode : node.getChildNodeList()){
				graph.addVertex(node);
				str=node.getNodeID()+childNode.getNodeID();
				((Forest<Node, String>) graph).addEdge(str, node, childNode);
				
				createTree(childNode, graph);
			}
		}
	}
}

	


