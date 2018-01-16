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

package org.gag.xml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gag.node.Node;
import org.gag.production.Production;
import org.gag.production.Selector;
import org.gag.production.Sort;
import org.gag.production.Task;
import org.gag.term.Constante;
import org.gag.term.Constructor;
import org.gag.term.Term;
import org.gag.term.Variable;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.xpath.XPathFactory;

/**
 * This class read a saved xml file that contains all the element from a saved GAG tree and its grammar.
 * @author Andre GUENEY
 *
 */
public class ReadXMLGAGFile {

	static org.jdom2.Document document;
	static Element fileRoot;
	static XPathFactory xFactory;
	
	protected File file;
	protected SAXBuilder sx;
	
	public ReadXMLGAGFile(File file){
		
		if(file==null) return;
		this.file = file;
		
		sx = new SAXBuilder();
		
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est terminé ;)
			document = sx.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(document.hasRootElement()) fileRoot = document.getRootElement();
	}
	
	/**
	 * 
	 * @return
	 */
	public File getFile(){
		return this.file;
	}
	
	/**
	 * Extract the production from the saved xml file.
	 * Use the path to the grammar file (xml format) to recreate the list of production.
	 * @param file
	 * @return
	 */
	public List<Production> extractProduction(File file){
		
		File prodFile=null;
		
		if(file==null) return null;
		
		for(Element childElement : fileRoot.getChildren()){
			if(childElement.getName()=="Prod_path"){
				Path path = Paths.get(childElement.getAttribute("Path").getValue().toString());
				System.out.println("Path "+path);
				if(!Files.exists(path)) return null;
				prodFile = path.toFile();
				break;
			}
		}

		ReadXMLProdFile prod = new ReadXMLProdFile(prodFile);
		
		return prod.createListOfProductions();
	}
	
	/**
	 * This method extract the saved tree graph from the selected xml file (edges and vertices).
	 * It recreates also the contents of each node (ID, parent, children, task, open).
	 * @param file
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Node> extractGraph(File file){
		
		//List<String> childIDs = new LinkedList<String>();
		Map<String, String> parentIDMap = new HashMap<String, String>();
		Map<String, List<String>> childIDMap = new HashMap<String, List<String>>();
		
		List<Node> nodeList = new LinkedList<Node>();
		
		if(file==null) return null;
		
		for(Element childElement : fileRoot.getChildren()){
			if(childElement.getName()=="Node"){
				
				Node newNode = new Node();
				
				for(Element nodeData : childElement.getChildren()){
										
					switch(nodeData.getName()){
					case "Node_ID" : 
						newNode.setNodeID(nodeData.getAttribute("Node_ID").getValue().toString());
						break;
					case "Parent" :
						if(!nodeData.getAttributes().isEmpty()){
							parentIDMap.put(newNode.getNodeID(), nodeData.getAttribute("Parent_ID").getValue());
						} else parentIDMap.put(newNode.getNodeID(), null);
						break;
					case "Open" : 
						newNode.setOpen(Boolean.valueOf(nodeData.getAttribute("Open").getValue()));
						break;
					case "Children" :
						List<String> childList = new LinkedList<String>();
						if(nodeData.getChildren()!=null){
							for(Element nodeChild : nodeData.getChildren()){
								for(Attribute nodeAttr : nodeChild.getAttributes()){
									childList.add(nodeAttr.getValue());
								}
							}
							childIDMap.put(newNode.getNodeID(), childList);
						}
						break;
						
							/*
							 * Here we recreate the task part of the node
							 */
					case "Task" : {
						Task newTask = new Task();
						Term term = null;
						
						for(Element taskElement : nodeData.getChildren()){
							switch(taskElement.getName()){
							case "Sort" : {
								Sort sort = new Sort();

								sort.setSortName(new TypeConverter().convertType(taskElement.getAttribute("Type").getValue(), 
												taskElement.getAttribute("Name").getValue()));
								newTask.setSort(sort);
								break;
							}
							case "Affectation" :
								Element affect = taskElement;
								if(affect.getChildren()!=null){
									Map<Selector<?>, Term> affectation = new HashMap<Selector<?>, Term>();
									for(Element affectElem : affect.getChildren()){									
										Selector sel = new Selector();
										sel.setSelectorName(new TypeConverter().convertType(
												affectElem.getAttribute("Type").getValue(), affectElem.getAttribute("Value").getValue()));
										term = createTerm(affectElem);
										affectation.put(sel, term);
									}
									newTask.setAffectation(affectation);	
								}
								break;
							
							case "Contraction" :
									Element contrac = taskElement;
									if(contrac.getChildren()!=null){
										Map<Selector<?>, Term> contraction = new HashMap<Selector<?>, Term>();
										for(Element contracElem : contrac.getChildren()){									
											Selector sel = new Selector();
											sel.setSelectorName(new TypeConverter().convertType(
												contracElem.getAttribute("Type").getValue(),
												contracElem.getAttribute("Value").getValue()));
											term = createTerm(contracElem);
											contraction.put(sel, term);
										}
										newTask.setContraction(contraction);
									}
									break;
								}
							}
							newNode.setTask(newTask);
						}
					}
				}
				
				boolean res = false;
				for(Node node : nodeList){
					if(node.getNodeID().equals(newNode.getNodeID())) res=true;
				}
				if(!res) nodeList.add(newNode);

			}
			
				/*
				 * Here we update the parent information for the current node
				 */
			for(Entry<String, String> entries : parentIDMap.entrySet()){
				for(Node nodeF : nodeList){
					if(nodeF.getNodeID().equals(entries.getKey())){
						for(Node nodeP : nodeList){
							if(nodeP.getNodeID().equals(entries.getValue())) {
								nodeF.setParentNode(nodeP);
							}
						}
					}
				}
			}
				
				/*
				 * Here we update the list of children for the current node
				 */
			for(Entry<String, List<String>> entries : childIDMap.entrySet()){
				for(Node nodeP : nodeList){
					if(nodeP.getNodeID().equals(entries.getKey())){
						for(String childID : entries.getValue()){
							for(Node nodeF : nodeList){
								if(nodeF.getNodeID().equals(childID)) {
									if(!nodeP.getChildNodeList().contains(nodeF)) nodeP.setChildNodeToList(nodeF);
								}
							}
						}
					}
				}
			}
		}
	
		for(Node node : nodeList){
			if(node.getParentNode()!=null) System.out.println("New Node2 ************> "+node.getNodeID()+
					"\n\tParentNode "+node.getParentNode().getNodeID());
		}

		return nodeList;
	}
	
		/*
		 * This private method create a Term from the xml file
		 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Term createTerm(Element element){
		
		Term newTerm = null;
		if(element.getChildren().isEmpty()) return null;
		
		for(Element childElement : element.getChildren()){
			if(childElement.getName()=="Constante"){
				newTerm = new Constante(new TypeConverter().convertType(
						childElement.getAttribute("Type").getValue(), childElement.getAttribute("Value").getValue()));
			}
			
			if(childElement.getName()=="Variable"){
				newTerm = new Variable(childElement.getAttribute("Value").getValue());
			}
			
			if(childElement.getName()=="Constructor"){
				newTerm = new Constructor(childElement.getAttribute("Name").getValue());
				for(Element consChild : childElement.getChildren()){
					Selector sel = new Selector();
					sel.setSelectorName(new TypeConverter().convertType(
							consChild.getAttribute("Type").getValue(), consChild.getAttribute("Value").getValue()));
					newTerm.add(sel, createTerm(consChild));
				}
			}
		}
		return newTerm;
	}
	
	
	
}
