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

import java.util.LinkedList;
import java.util.List;
//import java.util.Random;

/**
 * This class generates a random ID.
 * This ID is used to identify a node for the GAG tree.
 * Each ID is unique.
 * An ID is composed by a head String "ID_" followed by an Integer number.
 * 
 * @author Andre GUENEY
 *
 */
public class UniqueID {

	protected static List<Integer> listOfNodeIDs = new LinkedList<Integer>();
	
	protected int index = 0;
	
	public UniqueID(){
	}
	
	public String getUniqueID(){
		return CreateNodeID();
	}
	
	/**
	 * Remove the ID from a suppressed node
	 * @param IDtoBeRemove
	 * @return True if successfully removed, false otherwise
	 */
	public boolean RemoveID(String IDtoBeRemove){

		if(SearchID(Integer.valueOf(IDtoBeRemove))){
			listOfNodeIDs.remove(index);
			return true;
		} else return false;		
	}
	
	/**
	 * This method create the unique new ID
	 * @return The unique new ID
	 */
	private String CreateNodeID(){
		
		int newNodeID=0;
		boolean exit = false;

		while(!exit){
			
			//Create the new ID
			
			/*
				//Create a complex ID for each node
			Random randomPartOfID = new Random();
			newNodeID = "ID_"+ String.valueOf(randomPartOfID.nextLong()); //A modifier nextInt -> nextLong

				//Verify if the new ID is not already used by another node
			if(!SearchID(newNodeID)){
				listOfNodeIDs.add(newNodeID);
				exit = true;
			}
			//*/
			
				//Create a simple ID for each node
			if(!listOfNodeIDs.isEmpty()) {

				int j = 0;
				for(int i : listOfNodeIDs){
					if(i>j) j=i;
				}
				newNodeID=j+1;
			}
			else newNodeID = 1;

			if(!SearchID(newNodeID)){
				listOfNodeIDs.add(newNodeID);
				exit = true;
			}
		}
		return String.valueOf(newNodeID);
	}
	
	
	/**
	 * Add an ID to the list of Node ID.
	 * The result of operation is true if the new ID isn't in the list, false otherwise.
	 * @param id
	 * @return
	 */
	public boolean addID(int id){
		if(SearchID(id)){
			return false;
		}
		listOfNodeIDs.add(id);
		
		System.err.println("Add ID");
		
		return true;
	}
	
	/**
	 * This method search if the selected ID already exists in the node ID list
	 * @param searchID
	 * @return
	 */
	private boolean SearchID(int searchID){

		if(listOfNodeIDs.isEmpty()) return false;

		for(int id : listOfNodeIDs){
			if(searchID==id){
				return true;
			}
			index++;
		}
		return false;
	}
	
	public void reset(){
		listOfNodeIDs = new LinkedList<Integer>();
	}
	
}
