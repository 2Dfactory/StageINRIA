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

package org.gag.window;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.gag.node.Node;
import org.gag.node.UniqueID;
import org.gag.production.Production;
import org.gag.production.Task;
import org.gag.window.GAGframe;
import org.gag.xml.ReadXMLGAGFile;
import org.gag.xml.ReadXMLProdFile;
import org.gag.xml.SaveXMLGAGFile;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.ObservableCachingLayout;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used to implement all standard actions realized by the menu bar and defined in the MenuFile.xml 
 * @author Andre GUENEY
 *
 */
public class MenuActions {
	
	public static void Actions(JMenuItem item, VisualizationViewer<?,?> vv, JFrame frame){
	
		switch(item.getText()){
			case "Open file" : openFileWindow(item, vv, frame);
			case "Open GAG file" : openGAGwindow(item, vv, frame);
			case "New file" : newFileWindow(item, vv, frame);
			case "Save" : saveFile(item, vv, frame);
			case "Save as" : saveAsFile(item, vv, frame);
			case "Close window" : closeWindow(item);
			case "Annotating" : annotating(item, vv);
			case "Editing" : editing(item, vv);
			case "Help" : // TODO Create help menu
				;
			case "Picking" : picking(item, vv);
			case "Transforming" : transforming(item, vv);
			case "Auto" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.AUTO);
			case "Center" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.CNTR);
			case "East" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.E);
			case "North" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.N);
			case "North East" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.NE);
			case "North West" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.NW);
			case "South" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.S);
			case "South East" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.SE);
			case "South West" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.SW);
			case "West" : setLabelPosition(item, vv, Renderer.VertexLabel.Position.W);
			
		}
	}
	
	/*
	 * This method is used to save the current GAG tree in an existing or new  XML file
	 */
	private static void saveFile(final JMenuItem item, final VisualizationViewer<?,?> vv, final JFrame frame){
		
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Save")) {

					SaveXMLGAGFile gagFile = new SaveXMLGAGFile(((GAGframe) frame).getFile());
					
					if(gagFile.getFile()==null) return;

					if(((GAGframe) frame).getListProduction()!=null){
						gagFile.addProdFilePath(((GAGframe) frame).getListProduction().get(0).getProdPath());
					}
				
					gagFile.addGAGTree((Collection<Node>) vv.getGraphLayout().getGraph().getVertices());
					
					gagFile.enregistre();
				}
			}
		});
	}

	/*
	 * This method is used to save the current GAG tree in an existing or new  XML file
	 */
	private static void saveAsFile(final JMenuItem item, final VisualizationViewer<?,?> vv, final JFrame frame){
		
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Save as")) {
										
					SaveXMLGAGFile gagFile = new SaveXMLGAGFile(SelectFile.selectFile(vv.getMousePosition()));
					
					if(gagFile.getFile()==null) return;

					if(((GAGframe) frame).getListProduction()!=null){
						gagFile.addProdFilePath(((GAGframe) frame).getListProduction().get(0).getProdPath());
					}
				
					gagFile.addGAGTree((Collection<Node>) vv.getGraphLayout().getGraph().getVertices());
					
					gagFile.enregistre();
				}
			}
		});
	}
	
	/*
	 * This method is used to clear the current window and create a new graph
	 */
	private static void newFileWindow(final JMenuItem item, final VisualizationViewer<?,?> vv, final JFrame frame){
		
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("New file")){

					((GAGframe) frame).setFile(SelectFile.selectFile(vv.getMousePosition()));
					new UniqueID().reset();
					final ObservableCachingLayout<Node, String> layout = (ObservableCachingLayout<Node,String>) vv.getGraphLayout();
					layout.setGraph(new DelegateForest<Node,String>());
					layout.reset();
					vv.repaint();
					
				}	
			}
		});
	}
	
	/*
	 * This method is used to select a GAG rules file (in xml format), extract and convert the file in a list of Production
	 * and add a button with the root in the menu bar
	 */
	private static void openGAGwindow(final JMenuItem item, final VisualizationViewer<?,?> vv,final JFrame frame){
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Open GAG file")) {
					
					List<Production> listProd = new LinkedList<Production>();
					ReadXMLProdFile prodFile = new ReadXMLProdFile(SelectFile.selectFile(vv.getMousePosition()));
					listProd = prodFile.createListOfProductions();
					
					System.out.println(listProd);
					
					if(listProd == null) return;
					((GAGframe) frame).setListProduction(listProd);
					final JMenu prodMenu = new JMenu("Production");
					
					for(final Production prod : listProd){
						
						if(prod.getProdSort().getSortName().equals("Root")) {
							
							
							JMenuItem newItem = new JMenuItem(prod.getProdSort().toString());
							prodMenu.add(newItem);
							
							newItem.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ae){

									if(ae.getActionCommand().equals(prod.getProdSort().toString())) {

										for(Task task : prod.getTaskList()){
											Node rootNode = new Node(null,null,task.getClonedTask(), true);
											Graph graph = null;
											try {
												graph = vv.getGraphLayout().getGraph();

											} catch (Exception e) {
																								
												System.out.println("---------------Pas de graphe---------------");
												
												e.printStackTrace();
											}
											if(vv.getGraphLayout().getGraph()==null){
												graph = new DelegateForest<Node, String>();
												vv.getGraphLayout().setGraph(graph);
											}

											graph.addVertex(rootNode);

											ObservableCachingLayout<Node, String> layout = (ObservableCachingLayout<Node,String>) vv.getGraphLayout();
											
											Point2D startPos = new Point2D.Double(vv.getCenter().getX(), vv.getCenter().getY()/4);
											
											layout.setLocation(rootNode, startPos);
											
											vv.repaint();
											
												//remove the root production menu after have being used
											for(int i = 0;i<frame.getJMenuBar().getComponentCount();i++){
												if(frame.getJMenuBar().getMenu(i).getText().equalsIgnoreCase("gag")){
													frame.getJMenuBar().getMenu(i).remove(prodMenu);
												}
											}
										}
									}
								}
							});
						}
						
					}
					
					for(int i = 0;i<frame.getJMenuBar().getComponentCount();i++){
						if(frame.getJMenuBar().getMenu(i).getText().equalsIgnoreCase("gag")){
							frame.getJMenuBar().getMenu(i).add(prodMenu).revalidate();
						}
					}
					
					frame.repaint();
				}
			}
		});
	}
	
	/*
	 * This method is used to open a saved GAG file
	 */
	private static void openFileWindow(final JMenuItem item, final VisualizationViewer<?,?> vv, final JFrame frame){
		// Choose a file to open the window	
		
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Open file")) {
					
					List<Production> listProd = new LinkedList<Production>();
					Graph graph=null;
					Node rootNode = null;

						/*
						 * Extract the production from the saved file
						 */
					ReadXMLGAGFile gagFile = new ReadXMLGAGFile(SelectFile.selectFile(vv.getMousePosition()));
					listProd = gagFile.extractProduction(gagFile.getFile());
					
					if(listProd==null) return;
					
					((GAGframe) frame).setListProduction(listProd);

						/*
						 * Extract the saved tree from the saved file and find the root node
						 */
					for(Node node : gagFile.extractGraph(gagFile.getFile())){
						if(node.getParentNode()==null){
							rootNode = node;
						}
					}
					
						/*
						 * Create a new graph if not exist.
						 * Construct and add the tree in the graph
						 */
					if(vv.getGraphLayout().getGraph()==null){
						graph = new DelegateForest<Node, String>();
						createTree(rootNode, graph);
						vv.getGraphLayout().setGraph(graph);
						vv.repaint();
					} else {
						
						final ObservableCachingLayout<Node, String> layout = (ObservableCachingLayout<Node,String>) vv.getGraphLayout();
						createTree(rootNode, layout.getGraph());

						Map<String, Point2D> vertexPosition = new HashMap<String, Point2D>();
						
							/*
							 * Set the position of the root node
							 */
						Point2D startPos = new Point2D.Double(vv.getCenter().getX(), vv.getCenter().getY()/4);
						layout.setLocation(rootNode, startPos);
						
						System.out.println("Tree height ====> "+((DelegateForest<Node,String>) layout.getGraph()).getHeight());
						
							/*
							 * Set the position of the nodes in the layout
							 */
						vertexPositioner(rootNode, layout);
						
							/*
							 * Get the coordinates of each node
							 */
						for(Node node : layout.getGraph().getVertices()){
							vertexPosition.put(node.getNodeID(), layout.transform(node));
						}
						
						for(Entry<String, Point2D> entries : vertexPosition.entrySet()){
							System.out.println("******************************");
							System.out.println(entries.getKey()+" * "+entries.getValue());
						}

						vv.repaint();
					}

				}
			}
		});
	}
	
	/**
	 * Used to set the coordinates of each nodes of the current graph
	 * @param node
	 * @param layout
	 */
	public static void vertexPositioner(Node node, Layout<Node,String> layout){
		
		Point2D position = layout.transform(node);
		int numOfChild = node.getChildNodeList().size();
		double decalage = (numOfChild-1)*50/2;
		
		Point2D newPos=null;
		
		if(node.getChildNodeList()==null) return;
		
		for(Node childNode : node.getChildNodeList()){	
			
			if(numOfChild==1) {
				newPos = new Point2D.Double(position.getX(), position.getY()+50);
				layout.setLocation(childNode, newPos);
			}
			
			newPos = new Point2D.Double(position.getX()-decalage, position.getY()+50);

			decalage-=50;
			
			layout.setLocation(childNode, newPos);
			
			if(childNode.getChildNodeList()!=null){
				vertexPositioner(childNode,layout);
			}
		}
		
	}

		/*
		 * Used to construct the tree from the rootNode and add it in the graph
		 */
	private static void createTree(Node node, Graph<Node,String> graph){
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
	
	/*
	 * Standard method from the Jung library. Not used
	 */
	private static void annotating(JMenuItem item, final VisualizationViewer<?,?> vv){
		// Annotate the graph
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Annotating")){
					((ModalGraphMouse) vv.getGraphMouse()).setMode(ModalGraphMouse.Mode.ANNOTATING);
				}
			}
		});
	}
	
	/*
	 * The method that close the window
	 */
	private static void closeWindow(JMenuItem item){
		// Close the window		
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Close window")) System.exit(0);
			}
		});
	}
	
	/*
	 * This is a standard method from the Jung library. Not used
	 */
	private static void editing(JMenuItem item, final VisualizationViewer<?,?> vv){
		// Editing the graph
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Editing")){
					((ModalGraphMouse) vv.getGraphMouse()).setMode(ModalGraphMouse.Mode.EDITING);
				}
			}
		});
	}
	
	/*
	 * A method that display a help menu. Not functional
	 */
	private static void help(JMenuItem item, final VisualizationViewer<?,?> vv){
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Help")){
					// TODO complete the help menu action listener action
				}
			}
		});
	}
	
	/*
	 * This is a standard method from the Jung library. Used to set the position of all label for all vertex
	 */
	private static void setLabelPosition(final JMenuItem item, final VisualizationViewer<?,?> vv, final Renderer.VertexLabel.Position pos){
			// Modify the position of the label (all label)
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				switch(ae.getActionCommand()){
				
				case "Auto" : labelPosition(vv, pos);
				case "Center" : labelPosition(vv, pos);
				case "East" : labelPosition(vv, pos);
				case "North" : labelPosition(vv, pos);
				case "North East" : labelPosition(vv, pos);
				case "North West" : labelPosition(vv, pos);
				case "South" : labelPosition(vv, pos);
				case "South East" : labelPosition(vv, pos);
				case "South West" : labelPosition(vv, pos);
				case "West" : labelPosition(vv, pos);
				}
			}
				//Update the position of the label and repaint the viewer
			public void labelPosition(final VisualizationViewer<?,?> vv, final Renderer.VertexLabel.Position pos){
				vv.getRenderer().getVertexLabelRenderer().setPosition(pos);
				vv.repaint();
			}
		});
	}
	
	/*
	 * This is a standard method from the Jung library. Used to move all the tree in the window
	 */
	private static void transforming(JMenuItem item, final VisualizationViewer<?,?> vv){
		// Transforming the graph
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Transforming")){
					((ModalGraphMouse) vv.getGraphMouse()).setMode(ModalGraphMouse.Mode.TRANSFORMING);
				}
			}
		});
	}
	
	/*
	 * This is a standard method from the Jung library. Used to pick one or more vertices from the tree and move them.
	 */
	private static void picking(JMenuItem item, final VisualizationViewer<?,?> vv){
		// Picking the graph
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(ae.getActionCommand().equals("Picking")){
					((ModalGraphMouse) vv.getGraphMouse()).setMode(ModalGraphMouse.Mode.PICKING);
				}
			}
		});
	}
	
}
