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

package org.gag.term;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gag.production.Selector;

/**
 * This class contains the substitution calculate during the pattern matching
 * @author Andre GUENEY
 *
 */
public class Substitution {

	/**
	 * If ever theSubst becomes equals to null the substitution is failed.
	 */
	protected Map<Variable,Term> theSubst;
	
	public Substitution(){
		theSubst=new HashMap<Variable,Term>();
	}
	
	/**
	 * 
	 * @param var
	 * @param term
	 */
	public void setSubstitution(Variable var, Term term){
		if(!var.equals(term)) this.theSubst.put(var, term);
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<Variable,Term> getSubstitution(){
		return this.theSubst;
	}

	/**
	 * This function merges *this* with *other*. We choose to add any value for a variable not already in the substitution.
	 * If a variable is present in both substitutions they should be equal otherwise the merge operation transforms this into the
	 * *failed* substitution.
	 *  
	 * @param other
	 */
	public void merge(Substitution other){
		
		if(this.theSubst==null || other==null) {
			this.theSubst=null;
			return;
		}
		
		try {
			for (Variable v: other.getSubstitution().keySet()){
				if (this.theSubst.containsKey(v)){
					if (!this.theSubst.get(v).equals(other.theSubst.get(v))){
						this.theSubst=null;
						break;
					}
				}
				else{
					this.theSubst.put(v, other.getSubstitution().get(v));
				}
			}
		} catch (NullPointerException e) {
			this.theSubst=null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFailed(){
		return this.theSubst==null;
	}
	
	/**
	 * 
	 */
	public void fail(){
		this.theSubst=null;
	}
	
	/**
	 * This method return a composition of two substitution
	 * @param subst
	 * @return
	 */
	public Substitution compose(Substitution subst){
		for(Entry<Variable, Term> substE : this.theSubst.entrySet()){
			Term value = substE.getValue();
			if(value.isVariable()){
				if(subst.getSubstitution().containsKey(value)){
					this.theSubst.put(substE.getKey(), subst.getSubstitution().get(value));
				}
			}
			if(!value.isConstante()){
				value.applySubst(subst);
			}
		}		
		return this;
	}
	
	/**
	 * This method check if they haven't a circular reference in the substitution
	 * @return
	 */
	public boolean occurCheck(){

		if(this.theSubst==null) return false;
		
		Set<Entry<Variable,Term>> substEntries = this.theSubst.entrySet();
				
		for(Entry<Variable,Term> entries : substEntries){
			
			if(entries.getValue()==null) continue;
			
			if(entries.getValue().contains(entries.getKey())) return false;
			
			for(Entry<Variable,Term> entry : substEntries){
				
				if(entry.getValue()==null) continue;
				
				if(entry.getValue().contains(entries.getKey())) {
					replace(this.theSubst.get(entry.getKey()), entries.getKey(), this.theSubst.get(entries.getValue()));
				}
				
				if(entries.getValue().contains(entries.getKey())) return false;
			}
			
		}
		
		return true;
	}
	
	/*
	 * This method replace a term by another term
	 * Used during the occurCheck method
	 */
	private void replace(Term origin,Variable checkVar,Term newTerm){

		if(origin.isConstante()) return;
		
		if(origin.getChilds()==null) return;
		
		for(Selector<?> childKey : origin.getChilds().keySet()){
			if(origin.getChilds().get(childKey).equals(checkVar)) 
				origin.getChilds().replace(childKey, newTerm);
			else replace(origin.getChilds().get(childKey), checkVar,newTerm);
		}
	}

	/**
	 * 
	 */
	@Override
	public String toString(){
		String res="";
		if(this.theSubst==null) {
			res="No substitution";
			return res;
		}
		if (this.theSubst.keySet().isEmpty())
			res="Id";
		for (Variable v: this.theSubst.keySet()){
			if (!res.equals(""))res+=", ";
			res+=v.toString()+"->"+this.theSubst.get(v);
		}
		return res;
	}
	
}
