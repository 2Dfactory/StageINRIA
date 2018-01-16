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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gag.term.Term;
import org.gag.term.Variable;

/**
 * This part of the GAG project set the shape of the profile and the task
 * Each profiles and tasks of a production is composed of some defined parameters
 * 	- A sort : the ID of the profile or the task
 * 	- An Affectation : left hand side expression(affectation)
 *  - A contraction : right hand side expression(contraction)
 * @author Andre GUENEY
 *
 */
public class Task{

	protected Sort<?> sort = null;
	protected Map<Selector<?>, Term> affectations = null;
	protected Map<Selector<?>, Term> contractions = null;
	
	public Task(){
	}
	
	/**
	 * This constructor initialize the attributes of the Production profile or task
	 * @param sort The identifier of this profile or task
	 * @param affect The left hand side expression of this profile or task
	 * @param contract The right hand side expression of this profile or task
	 */
	public Task(Sort<?> sort, Map<Selector<?>, Term> affect, Map<Selector<?>, Term> contract){
		this.sort = sort;
		this.affectations = affect;
		this.contractions = contract;
	}

	/**
	 * Return the identifier of this profile or task
	 * @return Return the identifier of this profile or task
	 */
	public Sort<?> getSort(){
		return this.sort;
	}
	
	/**
	 * Return the affectation of this profile or task (LHS)
	 * @return Return the affectation of this profile or task (LHS)
	 */
	public Map<Selector<?>, Term> getAffectations(){
		return this.affectations;
	}
	
	/**
	 * Return the contraction of this profile or task (RHS)
	 * @return Return the contraction of this profile or task (RHS)
	 */
	public Map<Selector<?>, Term> getContractions(){
		return this.contractions;
	}
	
	/**
	 * Return a cloned version of this Task (same object Sort but cloned version of each expression)
	 * @return
	 */
	
	public Task getClonedTask(){
		Map<Selector<?>, Term> copyAff = new HashMap<Selector<?>, Term>();
		Map<Selector<?>, Term> copyCont = new HashMap<Selector<?>, Term>();
		
		if(this.affectations!=null){
			Set<Entry<Selector<?>, Term>> entries = this.affectations.entrySet();
			for(Entry<Selector<?>, Term> affEntries : entries){
				copyAff.put(affEntries.getKey(), affEntries.getValue().clone());
			}
		}else copyAff = null;

		if(this.contractions!=null){
			Set<Entry<Selector<?>, Term>> entries = this.contractions.entrySet();
			for(Entry<Selector<?>, Term> contEntries : entries){
				copyCont.put(contEntries.getKey(), contEntries.getValue().clone());
			}
		} else copyCont=null;
		
		
		Task clonedTask = new Task(this.sort,copyAff,copyCont);
		return clonedTask;
	}
	
	/**
	 * Return a list of the variables contained in this task
	 * @return
	 */
	public List<Variable> getListOfVariable(){
		
		List<Variable> listVar = new LinkedList<Variable>();
		
		if(this.affectations!=null){
			for(Entry<Selector<?>, Term> entries : this.affectations.entrySet()){
				if(entries.getValue().getListOfVariable(entries.getValue())!=null)
					for(Variable var : entries.getValue().getListOfVariable(entries.getValue())) listVar.add(var);
			}
		}
		
		if(this.contractions!=null){
			for(Entry<Selector<?>, Term> entries : this.contractions.entrySet()){
				if(entries.getValue().getListOfVariable(entries.getValue())!=null)
					for(Variable var : entries.getValue().getListOfVariable(entries.getValue())) listVar.add(var);
			}
		}

		return listVar;
	}
	
	/**
	 * Set the identifier of this profile or task
	 * @param sort Set the identifier of this profile or task
	 */
	public void setSort(Sort<?> sort){
		this.sort = sort;
	}
	
	/**
	 * Set the affectation part of this profile or task
	 * @param affect Set the affectation part of this profile or task
	 */
	public void setAffectation(Map<Selector<?>, Term> affect){
		this.affectations = affect;
	}
	
	/**
	 * Set the contraction part of this profile or task
	 * @param contract Set the contraction part of this profile or task
	 */
	public void setContraction(Map<Selector<?>, Term> contract){
		this.contractions = contract;
	}
	
	/**
	 * 
	 */
	public String toString(){
		String str = "";
		str += "\tTask name : "+this.sort.toString();
		if(!(this.affectations==null)) str += "\n\t\tAffectation : "+this.affectations.toString();
		else str += "\n\t\tAffectation : null";
		if(!(this.contractions==null)) str += "\n\t\tContraction : "+this.contractions.toString();
		else str += "\n\t\tContraction : null";
		
		return str;
	}
}
