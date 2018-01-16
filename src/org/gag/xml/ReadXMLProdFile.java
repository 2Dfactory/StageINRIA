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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

/**
 * This class read a GAG grammar from a xml file and creates the associates list of production.
 * 
 * @author Andre GUENEY
 *
 */
public class ReadXMLProdFile {
	
	private static org.jdom2.Document documentRead;
	private static Element fileRoot;
	
	protected File file;
	protected SAXBuilder sx;
	
	public ReadXMLProdFile(File file){
		
		if(file==null) return;
		this.file = file;
		
		sx = new SAXBuilder();
		
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est terminé ;)
			documentRead = sx.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(documentRead.hasRootElement()) fileRoot = documentRead.getRootElement();
	}
	
	public Element getFileRoot(){
		return fileRoot;
	}
	
		/*
		 * This method creates all parts for a production
		 */
	private Production createProductions(Element element, Production prod){
		
		Production production = prod;
		
		prod.setProdPath(file);
	
		if(!element.getChildren().isEmpty()){

			for(Element childElement : element.getChildren()){
					
				switch(childElement.getName()){

				case "Sort" : {
					if(childElement.getParentElement().getName()=="Production"){
						if(production.getProdSort()==null) production.setProductionSort(
								new Sort<String>(childElement.getAttribute("Name").getValue()));
					}
						
					if(childElement.getParentElement().getName()=="Profil"){
						Task task = new Task(new Sort<String>(childElement.getAttribute("Name").getValue()),null, null);
						production.setProfile(task);
					}
						
					if(childElement.getParentElement().getName()=="Task"){
						Task task = new Task(new Sort<String>(childElement.getAttribute("Name").getValue()),null, null);
						if(production.getTaskList()==null) production.setTaskList(new LinkedList<Task>());
						production.setTaskList(task);
					}
				}
					
				case "Selector" : {
						
					Term term = null;
					Map<Selector<?>, Term> map = new HashMap<Selector<?>, Term>();
				
					if(childElement.getParentElement().getName()=="Affectation"){
						if(childElement.getParentElement().getParentElement().getName()=="Profil"){
								
							if(!childElement.getChildren().isEmpty()) term = createTerm(childElement);
							
							if(production.getProfile().getAffectations()!=null){
								production.getProfile().getAffectations().put(new Selector<String>(
										childElement.getAttribute("Value").getValue()), term);
							} else {
								map.put(new Selector<String>(childElement.getAttribute("Value").getValue()), term);
								production.getProfile().setAffectation(map);
							}
						}
							
						if(childElement.getParentElement().getParentElement().getName()=="Task"){
							if(production.getTaskList()!=null){
								for(Task task : production.getTaskList()){
									if(task.getSort().getSortName().equals(childElement.getParentElement()
											.getParentElement().getChild("Sort").getAttribute("Name").getValue())){
										
										if(!childElement.getChildren().isEmpty()) term = createTerm(childElement);
										
										if(task.getAffectations()==null){
											map.put(new Selector<String>(childElement.getAttribute("Value").getValue()), term);
											task.setAffectation(map);
										}
									}
								}
							}
						}
					}
						
					if(childElement.getParentElement().getName()=="Contraction"){
						if(childElement.getParentElement().getParentElement().getName()=="Profil"){

							if(!childElement.getChildren().isEmpty()) term = createTerm(childElement);
							
							if(production.getProfile().getContractions()!=null){
								production.getProfile().getContractions().put(new Selector<String>(
										childElement.getAttribute("Value").getValue()), term);
							} else {
								map.put(new Selector<String>(childElement.getAttribute("Value").getValue()), term);
								production.getProfile().setContraction(map);
								
								System.out.println("MapCont1 : "+map);
							}
						}
						
						if(childElement.getParentElement().getParentElement().getName()=="Task"){
							if(production.getTaskList()!=null){
								for(Task task : production.getTaskList()){
									if(task.getSort().getSortName().equals(childElement.getParentElement()
											.getParentElement().getChild("Sort").getAttribute("Name").getValue())){
										
										if(!childElement.getChildren().isEmpty()) term = createTerm(childElement);
										
										if(task.getContractions()==null){
											map.put(new Selector<String>(childElement.getAttribute("Value").getValue()), term);
											task.setContraction(map);
										}
									}
								}
							}
						}
					}
				}
				
				}
				createProductions(childElement, production);
			}	
		}
		return production;
	}
	
	/*
	 * Create the Term from the GAG grammar file
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Term createTerm(Element element){
		
		Term newTerm = null;

		if(element.getChildren().isEmpty()) return null;
		
		for(Element childElement : element.getChildren()){
			
			if(childElement.getName()=="Constante"){
				newTerm = new Constante(new TypeConverter().convertType(childElement.getAttribute("Type")
						.getValue(), childElement.getAttribute("Value").getValue()));
			}
			
			if(childElement.getName()=="Variable"){
				newTerm = new Variable(childElement.getAttribute("Value").getValue());
			}
			
			if(childElement.getName()=="Constructor"){
				newTerm = new Constructor(childElement.getAttribute("Name").getValue());
				for(Element consChild : childElement.getChildren()){
					newTerm.add(new Selector<String>(consChild.getAttribute("Value").getValue()), createTerm(consChild));
				}
			}
			
		}
		return newTerm;
	}
	
	/**
	 * Return the list of the production produced from the GAG file (XML format)
	 * @return
	 */
	public List<Production> createListOfProductions(){
		
		if(file == null) return null;
		
		List<Production> listProd = new LinkedList<Production>();
		
		for(Element childElement : fileRoot.getChildren()){
			if(childElement.getName()=="Production"){
				listProd.add(createProductions(childElement, new Production()));
			}
		}
		return listProd;
	}
	
}