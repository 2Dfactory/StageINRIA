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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gag.production.Selector;

/**
 * This is the parent class for Constante, Constructor and Variable class
 * @author Andre GUENEY
 * 
 */
public abstract class Term implements Cloneable{
	
	String name;
	protected Constructor parent;
	
	public Term(){
	}
	
	public Term(String name){
		this();
		this.name = name;
	}
	
	/**
	 * Apply the substitution on the term
	 * @param subst
	 * @return The result of the substitution(boolean)
	 */

	public boolean applySubst(Substitution subst){
		return false;
	}	
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 
	 * @param parent
	 */
	public void setParent(Constructor parent){
		this.parent = parent;
	}
	
	/**
	 * 
	 * @return
	 */
	public Constructor getParent(){
		return this.parent;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName(){
		return this.name;
	}	
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public Map<Selector<?>, Term> getChilds(){
		return null;
	}
	
	/**
	 * Return a list of variables contained in this term.
	 * @param term
	 * @return
	 */
	public List<Variable> getListOfVariable(Term term){
		
		List<Variable> listVar = new LinkedList<Variable>();
		
		if(term==null) return null;
		
		if(term.isConstante()) return null;
		
		if(term.isVariable()) {
			listVar.add((Variable) term);
			return listVar;
		}
		
		for(Term childTerm : term.getChilds().values()){
			if(getListOfVariable(childTerm)!=null){
				for(Variable var : getListOfVariable(childTerm)){
					if(var!=null) listVar.add(var);
				}
			}
		}
		return listVar;
		
	}
	
	public String toString(){
		return name;
	}
	
	/**
	 * 
	 * @param term
	 * @param i
	 * @return
	 */
	public boolean add(Selector<?> selector,Term term){
		return false;
	}
	
	/**
	 * 
	 */
	public abstract Term clone();
	
	
	/**
	 * This method check if the current term contains this inputed term
	 * @param term
	 * @return
	 */
	public boolean contains(Term term){
		
		if(term==null) return false;

		if(this.isVariable()) if(this.equals(term)) return true;
		
		if(this.isConstante()) return false;
		
		try {
			for(Term childTerm : this.getChilds().values()){
				if(childTerm.contains(term)) return true;
			}
		} catch (Exception e) {
			//System.err.println("Error in contains method : NullPointerException");
			
			return false;
		}
		
		return false;
	}
		
	/**
	 * 
	 * @param position
	 * @return
	 */
	public Term replace(Selector<?> selector,Term term){
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isVariable(){
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isConstante(){
		return false;
	}
	
	/**
	 * The purpose of match is to produce a substitution (of class Substitution) 
	 * which is a Map between Variables and terms.
	 * This matching tries to match *this* with *other* meaning that Variables in *this*
	 * should match sub-terms of *other*. 
	 * @param other
	 * @return
	 */
	public abstract Substitution match (Term other) ;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 29;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Variable)) {
			return false;
		}
		Variable other = (Variable) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
}
