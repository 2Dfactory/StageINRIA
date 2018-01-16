/* **************************************************************************************
 *												*
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

package org.gag.node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import org.gag.production.Selector;
import org.gag.production.Task;
import org.gag.production.Production;
import org.gag.term.Substitution;
import org.gag.term.Term;
import org.gag.term.Variable;

/**
 * The node is a result of an applied production.
 * It is composed of:
 * 	- A list of open nodes (static list).
 * 	- A link to the root node (static node).
 * 	- A unique ID.
 * 	- A link to its parent node.
 * 	- A link to all of its children (list).
 * 	- A boolean that indicate if the node is open or close.
 * 
 * @author Andre GUENEY
 *
 */
public class Node {

	static List<Node> listOfOpenNodes = new LinkedList<Node>();	//Contains the list of open nodes
	static Node rootNode = null;								//Link to the root node
	
	protected String nodeID;									//Contains a unique ID to identify the node
	protected Node parentNode;									//Link to the parent node
	protected List<Node> childNodes = new LinkedList<Node>();	//Link to the childs of this node
	protected Task task = new Task();							//Contains the Task of this node
	protected boolean openNode;									//Indicates if the node is open or not
	
	
	public Node(){
		this.openNode = true;
	}
	
	/**
	 * Constructor of a node
	 * @param nodeID 
	 * @param parentNode
	 * @param childNodes
	 * @param task
	 */
	public Node(Node parentNode, List<Node> childNodes, Task task, boolean open){
		this.setNodeID(new UniqueID().getUniqueID());
		if(parentNode==null){
			this.parentNode = parentNode;
			rootNode=this;
		}
		this.childNodes = childNodes;
		this.task = task;
		this.openNode = open;
		if(!listOfOpenNodes.contains(this)){
			listOfOpenNodes.add(this);
		}
	}
	
	/**
	 * Return the ID of the node
	 * @return 
	 */
	public String getNodeID(){
		return this.nodeID;
	}
	
	/**
	 * Return the parent of the node
	 * @return
	 */
	public Node getParentNode(){
		return this.parentNode;
	}
	
	/**
	 * Return the list of children of the node
	 * @return
	 */
	public List<Node> getChildNodeList(){
		return this.childNodes;
	}
	
	/**
	 * Return the task.
	 * @return
	 */
	public Task getTask(){
		return this.task;
	}
	
	/**
	 * Return the list of the open nodes.
	 * @return
	 */
	public List<Node> getListOfOpenNodes(){
		return listOfOpenNodes;
	}
	
	/**
	 * Return the root node.
	 * @return
	 */
	public Node isRootNode(){
		return rootNode;
	}
	
	/**
	 * Allow to change the node ID.
	 * @param nodeID
	 */
	public void setNodeID(String nodeID){
		
		new UniqueID().addID(Integer.valueOf(nodeID));
		this.nodeID = nodeID;
	}
	
	/**
	 * 
	 * @param parentNode
	 */
	public void setParentNode(Node parentNode){
		this.parentNode = parentNode;
	}
	
	/**
	 * 
	 * @param task
	 */
	public void setTask(Task task){
		this.task = task;
	}
	
	/**
	 * Add a node to the list of children
	 * @param childNode
	 */
	public void setChildNodeToList(Node childNode){
		this.childNodes.add(childNode);
	}
	
	/**
	 * Add a list of children
	 * @param childNodes
	 */
	public void setChildNodes(List<Node> childNodes){
		ListIterator<Node> childIt = childNodes.listIterator();
		while(childIt.hasNext()){
			this.childNodes.add(childIt.next());
		}
	}
	
	/**
	 * 
	 * @param open
	 */
	public void setOpen(boolean open){
		this.openNode = open;
		if(open) listOfOpenNodes.add(this);
	}
	
	/**
	 * This method is used to know if the node is open or not.
	 * @return
	 */
	public boolean isOpen(){
		return this.openNode;
	}

	/**
	 * This method is used to create a new node.
	 * @param task
	 * @return
	 */
	public Node createNode(Task task){

		Node newNode = new Node();
		newNode.setNodeID(new UniqueID().getUniqueID());
		newNode.setTask(task);
		newNode.setParentNode(this);
		
		System.out.println("NewNode = "+newNode);
		
		if(!listOfOpenNodes.contains(newNode)){
			listOfOpenNodes.add(newNode);
		}
		try {
			if(rootNode.equals(null));
		} catch (NullPointerException e) {
			rootNode = newNode;
		}
		
		if(this.childNodes==null) this.childNodes = new LinkedList<Node>(); 
		this.childNodes.add(newNode);		
		
		System.out.println("Liste open node : "+listOfOpenNodes);
		
		return newNode;
	}
	
	/**
	 * The patternmatching method test all the defined production from the GAG file.
	 * If the motif of the selected production match with the task of the node, this
	 * method create the substitution and add this and the production in a map.
	 * This map represent the pool of compatible production with this node.
	 * The user must select a production from this pool to apply it on the tree of nodes.
	 * 
	 * @param inputNode
	 * @param listProd
	 * @return
	 */

	public Map<Production, Substitution> PatternMatching(List<Production> listProd){

		Map<Production, Substitution> matchingProd = new HashMap<Production, Substitution>();
		Substitution newSubst=new Substitution();
		
		for(Production prod : listProd){

			if(prod.getProfile()!=null){

				Production dupProd = prod.duplicate();
				
					/*
					 * Check if the task name of the current node match with the profile name of 
					 * the production
					 */
				if(this.getTask().getSort().getSortName().equals(dupProd.getProfile().getSort().getSortName())){
					newSubst=new Substitution();
					
						/*
						 * Check if the affectation part of the current node task match with the 
						 * contraction part of the production profile.
						 * If yes, we create the first step of the substitution creation.
						 */
					if(this.task.getAffectations()!=null && dupProd.getProfile().getContractions() != null){
						for(Selector<?> nodeAff : this.task.getAffectations().keySet()){
							if(dupProd.getProfile().getContractions().containsKey(nodeAff)){
								newSubst.merge(dupProd.getProfile().getContractions()
										.get(nodeAff).match(this.task.getAffectations().get(nodeAff)));
								System.out.println("NewSubst patternMatching phase 1 ===============>"+newSubst);
							} else {
								System.out.println(dupProd.getProdSort()+" => Echec PatternMatching(Phase1).");
								newSubst.fail();
								break;
							}
						}

							/*
							 * Check if the contraction part of the current node task match with the 
							 * affectation part of the production profile.
							 * If yes, we create the second step of the substitution creation.
							 */
						if(!newSubst.isFailed()){
							if(this.task.getContractions()!=null && dupProd.getProfile().getAffectations() != null){
								for(Selector<?> nodeSous : this.task.getContractions().keySet()){
									if(dupProd.getProfile().getAffectations().containsKey(nodeSous)){
										Term tempoT = dupProd.getProfile().getAffectations().get(nodeSous).clone();
										tempoT.applySubst(newSubst);
										if(this.task.getContractions().get(nodeSous).isVariable()){
											newSubst.setSubstitution((Variable) this.task.getContractions().get(nodeSous), tempoT);
											System.out.println("NewSubst patternMatching phase 2 ===============>"+newSubst);
										}
									} else {
										
										System.out.println(dupProd.getProdSort()+" => Echec PatternMatching(phase 2).");
										newSubst.fail();
										break;
									}
								}
							}	
						}
					}
					
						/*
						 * Test if there are no circular reference in the substitution
						 */
					if(!newSubst.occurCheck()) {
						System.out.println(dupProd.getProdSort()+" => Echec PatternMatching(OccurCheck).");
						continue;
					}

					if(!newSubst.isFailed()){
						matchingProd.put(dupProd, newSubst);
					}
				} else System.out.println(dupProd.getProdSort()+" => Echec PatternMatching(name).");
			}
			
		}

		return matchingProd;
	}
		
	/**
	 * This method apply the substitution on all of the open nodes of the tree.
	 * First of all, the substitution is applied on the left member of the node's task.
	 * Then, the substitution is applied on the right member of the node's task.
	 * @param node
	 * @param subst
	 * @return
	 */
	public boolean applySubst(Substitution subst){
		
		System.out.println("/****************Substitution à appliquer : "+subst+"**********************/");
		boolean res=false;

		for(Node node : listOfOpenNodes){
			res=false;
			
			res = this.check(node.getTask().getContractions(), subst);
			System.out.println("Res cont = "+res);

			res = this.check(node.getTask().getAffectations(), subst);
			System.out.println("Res aff = "+res);

			if(res){
				System.out.println(node.getTask().getSort()+" / Substitution OK...... ");
			} else System.out.println(node.getTask().getSort()+" / Echec de la substitution!!!!!");
		}
		return res;
	}
	
	/**
	 * This private method is used to check if an expression (right or left) match with a part of the substitution.
	 * If yes, this method replace the term (variable) in the expression by a cloned version of the substituted term.
	 * Example :
	 * 		If an expression contains the variable x and a substitution contains x = cons(y,z).
	 * 		Then x is replaced in the expression by cons(y,z)(cloned).
	 * @param exp
	 * @param subst
	 * @return
	 */
	private boolean check(Map<Selector<?>, Term> exp, Substitution subst){
		
		boolean res = false;
		Set<Entry<Selector<?>, Term>> expEntries;
		
		if(exp!=null){
			expEntries = exp.entrySet();
			for(Entry<Selector<?>, Term> entries : expEntries){
				if(entries.getValue().isVariable()) {
					for(Variable var : subst.getSubstitution().keySet()){
						if(entries.getValue().equals(var)) {
							exp.put(entries.getKey(), subst.getSubstitution().get(var).clone());
							res=true;
						}
					}
				} else if(entries.getValue().applySubst(subst)) res=true;
			}
		} else 	System.out.println("Cette partie de la tache est vide");
		
		return res;
	}
		
	/**
	 * This method is used to apply the chosen production to the node (create the child nodes)
	 * @param chosenProd
	 * @param subst
	 * @return
	 */
	public boolean applyProd(Production chosenProd, Substitution subst ){
		
		if(chosenProd != null){
			if(chosenProd.getTaskList()!=null){
				
				// TODO Here to add a popup window to update the variable
				
				JOptionPane.showMessageDialog(null, "Ajouter une fenetre de saisie utilisateur\ndans la classe Node entre les lignes 395 et 401.");
				
				for(Task subTask : chosenProd.getTaskList()){
					createNode(subTask.getClonedTask());
				}
			}
		} else return false;
		
		if(subst!=null){
			this.applySubst(subst);
		}
		
		this.openNode=false;
		listOfOpenNodes.remove(this);
		
		return true;
	}
	
	/**
	 * Return the content of the node and the content of its children
	 */
	public String toString(){
		String str="Node ID : "+this.nodeID;
		str += "\n"+this.task;
		if(this.childNodes != null){
			for(Node childNode : this.childNodes){
				str += "\nChild node : " +childNode.toString();
			}
		}
		return str;
	}
	
}
