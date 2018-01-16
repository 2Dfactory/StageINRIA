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

package org.gag.production;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.gag.term.VarIndex;
import org.gag.term.Variable;

/**
 * This java code create the main structure of the Production.
 * A GAG production is composed of some defined parameters
 * 	- A sort : the ID of the production (prodSort)
 * 	- Some profile : (listOfProfils)
 *  - Some tasks : (listOfTasks)
 *  - A list of all the production created from the GAG grammar (static list)
 *  - A link to the directory that contains the GAG grammar file (static String)
 *  
 * @author Andre GUENEY
 */

public class Production {

		
	protected Sort<?> prodSort = null;			//Here this is the identifier of the production
	protected static List<Production> prodList = new LinkedList<Production>();		//Here is the list of productions
	protected static String prodPath;			//Here is the link to the GAG grammar file

		//Here there is the definition of the profile and the tasks
	protected Task profile = null;
	protected List<Task> listOfTasks = null;

		
	/**
	 * This constructor initialize an empty Production
	 */
	public Production(){

	}
	
	/**
	 * This constructor initialize the parameter of one production.
	 * 
	 * @param prodSort The identifier of the Production
	 * @param profilsList The list of profiles of this Production
	 * @param tasksList The list of tasks of this Production
	 */
	public Production(Sort<?> prodSort, Task profil, List<Task> tasksList){
		this.prodSort = prodSort;		
		this.profile = profil;
		this.listOfTasks = tasksList;
		prodList.add(this);
	}
	
	/**
	 * 
	 * @param file
	 */
	public void setProdPath(File file){
		prodPath = file.getAbsolutePath();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProdPath(){
		return prodPath;
	}
	
	/**
	 * Return the list of profiles of this Production
	 * @return Return the list of profiles for this Production
	 */
	public Task getProfile(){
		return this.profile;
	}
	
	/**
	 * Return the list of tasks of this Production
	 * @return Return the list of tasks for this Production
	 */
	public List<Task> getTaskList(){
		return this.listOfTasks;
	}
	
	/**
	 * This method return the list of all created production
	 * @return
	 */
	public List<Production> getProductionList(){
		return prodList;
	}

	/**
	 * Return the identifier of this Production
	 * @return Return the Sort for this Production
	 */
	public Sort<?> getProdSort(){
		return this.prodSort;
	}

	/**
	 * Set the Sort for this Production
	 * @param prodSort
	 */
	public void setProductionSort(Sort<?> prodSort){
		this.prodSort = prodSort;
	}

	/**
	 * Set the list of profiles for this Production
	 * @param profilList
	 */
	public void setProfile(Task profile){
		this.profile = profile;
	}
	
	/**
	 * Set the list of tasks for this Production (replace the existing task list if exist)
	 * @param taskList
	 */
	public void setTaskList(List<Task> taskList){
		this.listOfTasks = taskList;
	}
	
	/**
	 * Add a task to the task list of the production
	 * @param task
	 */
	public void setTaskList(Task task){
		if(task!=null) this.listOfTasks.add(task);
	}
	
	/**
	 * This method return a duplicate version of this production with a renamed version of their variables.
	 * @return
	 */
	public Production duplicate(){

		Production duplicateProd = new Production(this.prodSort,null,null);
		List<Task> duplicateList = new LinkedList<Task>();
		List<Variable> listVar = new LinkedList<Variable>();
		
		if(this.profile!=null) duplicateProd.setProfile(this.getProfile().getClonedTask());
		
		if(this.getTaskList()!=null){
			for(Task task : this.listOfTasks){
				duplicateList.add(task.getClonedTask());
			}
		} else duplicateList=null;

		duplicateProd.setTaskList(duplicateList);

		if(duplicateProd.getProfile()!=null)
			for(Variable var : duplicateProd.getProfile().getListOfVariable()) listVar.add(var);

		if(duplicateProd.getTaskList()!=null){
			for(Task task : duplicateProd.getTaskList()){
				for(Variable var : task.getListOfVariable()) listVar.add(var);
			}
		}

		int newIndex = new VarIndex().getIndex();
		
		for(Variable var : listVar){
			var.setName(var.getName()+newIndex);
		}

		return duplicateProd;
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		String str = new String();
		str = "Prod name : "+this.prodSort;
		//str += "\n\t"+this.profile.sort;
		str += "\n\tProfil : \n"+this.profile;
		if(this.listOfTasks!=null){
			for(Task taskList : this.listOfTasks){
				str += "\n\t"+taskList.sort;
				str += "\n\t"+taskList;
			}
		}		
		return str;
	}

}
