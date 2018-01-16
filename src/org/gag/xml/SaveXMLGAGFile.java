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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.gag.node.Node;
import org.gag.production.Selector;
import org.gag.term.Constante;
import org.gag.term.Constructor;
import org.gag.term.Term;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * This class transform all the contents of the current graph in a xml formatted file.
 * 
 * @author Andre GUENEY
 *
 */
public class SaveXMLGAGFile {
	
	protected File file;
	private static org.jdom2.Document documentSaved;
	private Element root = new Element("root");
	
	
	public File getFile(){
		return this.file;
	}
	
	public SaveXMLGAGFile(File file){
		
		if(file==null) return;
				
		if(file.length()!=0){
			String str = file.getAbsolutePath();
			file.delete();
			file = new File(str);
		}
		this.file = file;
		documentSaved = new Document(root);
	}
	
	/**
	 * Add the path of the GAG files used to create the tree
	 * @param path
	 */
	public void addProdFilePath(String path){
		Element prodPath = new Element("Prod_path");
		Attribute path1 = new Attribute("Path", path);
		prodPath.setAttribute(path1);
		root.addContent(prodPath);
	}
	
	/**
	 * Transform the tree in a XML format and put it in a file
	 * @param tree
	 */
	public void addGAGTree(Collection<Node> tree){
		Iterator<Node> it = tree.iterator();
		
		while(it.hasNext()){
			Node node = it.next();
			Element summit = new Element("Node");
			
			/*
			 * Add the node ID in the XML file
			 */
			Element nodeId = new Element("Node_ID");
			summit.addContent(nodeId);
			Attribute attr = new Attribute("Node_ID",node.getNodeID());
			nodeId.setAttribute(attr);
			
			/*
			 * Add the parent node in the XML file in the current node
			 */
			Element parent = new Element("Parent");
			if(node.getParentNode()!=null){
				Attribute parentID = new Attribute("Parent_ID", node.getParentNode().getNodeID());
				parent.setAttribute(parentID);
			}
			summit.addContent(parent);
			
			/*
			 * Add the status of the node in the XML file
			 */
			String nodeStatus;
			Element openNode = new Element("Open");
			if(node.getListOfOpenNodes().contains(node)) nodeStatus="true";
			else nodeStatus="false";
			Attribute open = new Attribute("Open",nodeStatus);
			openNode.setAttribute(open);
			summit.addContent(openNode);
			
			/*
			 * Add the children nodes in the XML file in the current node
			 */
			if(node.getChildNodeList()!=null){
				int i = 0;
				Element childList = new Element("Children");
				for(Node childNode : node.getChildNodeList()){
					Element child = new Element("child"+i);
					Attribute childAttr = new Attribute("Child_ID", childNode.getNodeID());
					child.setAttribute(childAttr);
					childList.addContent(child);
					i++;
				}
				summit.addContent(childList);
			}
			
			/*
			 * Add the task in the XML file
			 */
				//Add the sort part of the task
			Element task = new Element("Task");
			Element taskSort = new Element("Sort");
			Attribute attrSortType = new Attribute("Type", node.getTask().getSort().getSortName().getClass().toString());
			Attribute attrSort = new Attribute("Name",node.getTask().getSort().toString());
			taskSort.setAttribute(attrSortType);
			taskSort.setAttribute(attrSort);
			task.addContent(taskSort);
			
				//Add the affectation part of the task
			Element affectation = new Element("Affectation");
			if(node.getTask().getAffectations()!=null){
				for(Entry<Selector<?>, Term> entries : node.getTask().getAffectations().entrySet()){
					Element sel = new Element("Selector");
					Attribute selAttrType = new Attribute("Type", entries.getKey().getSelectorName().getClass().toString());
					Attribute selAttr = new Attribute("Value",entries.getKey().toString());
					sel.setAttribute(selAttrType);
					sel.setAttribute(selAttr);
					sel.addContent(termToXml(entries.getValue()));
					affectation.addContent(sel);
				}
			}
			task.addContent(affectation);
			
				//Add the contraction part of the task
			Element contraction = new Element("Contraction");
			if(node.getTask().getContractions()!=null){
				for(Entry<Selector<?>, Term> entries : node.getTask().getContractions().entrySet()){
					Element sel = new Element("Selector");
					Attribute selAttrType = new Attribute("Type", entries.getKey().getSelectorName().getClass().toString());
					Attribute selAttr = new Attribute("Value",entries.getKey().toString());
					sel.setAttribute(selAttrType);
					sel.setAttribute(selAttr);
					sel.addContent(termToXml(entries.getValue()));
					contraction.addContent(sel);
				}
			}
			task.addContent(contraction);
			
			summit.addContent(task);
			
			root.addContent(summit);
		}
	}
	
	/*
	 * This private method convert a task from a node to a some of element that can be put in a XML file
	 */
	private Element termToXml(Term term){
		Element elem = null;
		if(term.isVariable()){
			elem = new Element("Variable");
			Attribute varAttr = new Attribute("Value", term.getName());
			elem.setAttribute(varAttr);
			return elem;
		}
		
		if(term.isConstante()){
			elem = new Element("Constante");
			Attribute varAttrType = new Attribute("Type", ((Constante<?>) term).getConstValue().getClass().toString());
			Attribute varAttr = new Attribute("Value", ((Constante<?>) term).getConstValue().toString());
			elem.setAttribute(varAttrType);
			elem.setAttribute(varAttr);
			return elem;
		}
		
		elem = new Element("Constructor");
		Attribute consAttr = new Attribute("Name", term.getName());
		elem.setAttribute(consAttr);
		for(Entry<Selector<?>, Term> entries : ((Constructor) term).getChilds().entrySet()){
			Element sel = new Element("Selector");
			Attribute selType = new Attribute("Type", entries.getKey().getSelectorName().getClass().toString());
			Attribute selAttr = new Attribute("Value", entries.getKey().toString());
			sel.setAttribute(selType);
			sel.setAttribute(selAttr);
			sel.addContent(termToXml(entries.getValue()));
			elem.addContent(sel);
		}
		
		return elem;
	}
	
	/**
	 * This method allow to save the GAG tree and all of its components in a xml file.
	 */
	public void enregistre(){
		
		String path = this.file.getPath();
		try {
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(documentSaved, new FileOutputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
