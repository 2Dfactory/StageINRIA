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
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.gag.node.Node;
import org.gag.production.Production;
import org.gag.term.Substitution;
import org.gag.window.GAGwindow.MyInfoPanel;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * This class create the specific popup menu. When we right click on a vertex,
 * we have a popup menu that contains only the applicable production for the selected node.
 * @author Andre GUENEY
 *
 * @param <V>
 * @param <E>
 */
public class PopupVertexMenuMousePlugin<V, E> extends AbstractPopupGraphMousePlugin {
	
    private JPopupMenu initialPopup, vertexPopup = new JPopupMenu();
    
    private JFrame frame = null;
    
    /** Creates a new instance of PopupVertexEdgeMenuMousePlugin */
    public PopupVertexMenuMousePlugin(JFrame frame) {
        this(MouseEvent.BUTTON3_MASK);
        this.frame = frame;
    }
    
    /**
     * Creates a new instance of PopupVertexEdgeMenuMousePlugin
     * @param modifiers mouse event modifiers see the jung visualization Event class.
     */
    public PopupVertexMenuMousePlugin(int modifiers) {
        super(modifiers);
    }
    
    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the 
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param e 
     */
    @SuppressWarnings("unchecked")
	protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        Point2D p = e.getPoint();
        
        GraphElementAccessor<V,E> pickSupport = null;
        
        pickSupport = vv.getPickSupport();

        if(pickSupport != null) {
        	
            V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            
            if((v != null) && ((Node) v).isOpen()) {
            	vertexPopup.removeAll();
            	
                updateVertexMenu(v, (VisualizationViewer<Node, String>) vv, p);

                vertexPopup.show(vv, e.getX(), e.getY());
            } else {
            	initialPopup.revalidate();
            	initialPopup.show(vv, e.getX(), e.getY());

            }
        } 
    }
    
    /*
     * This private method is used to update the popup menu with the applicable production for the selected node
     */
	private void updateVertexMenu(V v, final VisualizationViewer<Node, String> vv, Point2D point) {

		final JMenu menu = new JMenu("Production");
		JMenuItem item;
		final Node node = (Node) v;
		Map<Production, Substitution> map = null;

		if(((GAGframe) this.frame).getListProduction()==null) return;
		
		map = node.PatternMatching(((GAGframe) this.frame).getListProduction());
		
		if(map.isEmpty()) {
			item = new JMenuItem("No applicable production");
			menu.add(item);
			vertexPopup.add(menu);
			
				// TODO must be improved
				/*
				 * Here we update the information panel
				 */
			for(Component comp : this.frame.getContentPane().getComponents()){

				if(comp.getClass().isInstance(new JSplitPane())){
					
					for(Component compJS : ((JSplitPane) (vv.getParent().getParent())).getComponents()){

						if(compJS.getClass().isInstance(new JScrollPane())){

							for(Component compJSP : ((JScrollPane) compJS).getComponents()){

								for(Component jspComp : ((Container) compJSP).getComponents()){

									if(jspComp.getClass().isInstance(new MyInfoPanel())){
										
										String text="";
										text+="Node ID : "+node.getNodeID()+"\n";
										text+="No applicable production.\n";
										

										((MyInfoPanel) jspComp).updateHistory(text);
															
										((MyInfoPanel) jspComp).paintComponent(((MyInfoPanel) jspComp).getGraphics());
									}
								}
							}
						}
					}
				}
			}
			return;	
		}

		for(final Entry<Production, Substitution> entries : map.entrySet()){
			
			if(entries.getKey().getProdSort()!=null) {
				final String str = entries.getKey().getProdSort().toString();
				item = new JMenuItem(str);
				menu.add(item);
							
				item.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						if(ae.getActionCommand().equals(str)){
							
							node.applyProd(entries.getKey(), entries.getValue());
							createTree(node, vv.getGraphLayout().getGraph());
							
								// TODO must be improved
								/*
								 * Here we update the information panel
								 */
							for(Component compJS : ((JSplitPane) (vv.getParent().getParent())).getComponents()){

								if(compJS.getClass().isInstance(new JScrollPane())){

									for(Component compJSP : ((JScrollPane) compJS).getComponents()){

										for(Component jspComp : ((Container) compJSP).getComponents()){
											
											if(jspComp.getClass().isInstance(new MyInfoPanel())){
												
												String text="";
												text+="Node ID : "+node.getNodeID()+"\n";
												text+="Production applied :"+entries.getKey().getProdSort()+".\n";
												text+="Created children node :\n";
												
												if(node.getChildNodeList()==null) text+="No children node created.\n";
												else {
													for(Node childNode : node.getChildNodeList()){
														text+="   Created children node ID : "+childNode.getNodeID()+".\n";
													}
												}

												((MyInfoPanel) jspComp).updateHistory(text);
																	
												((MyInfoPanel) jspComp).paintComponent(((MyInfoPanel) jspComp).getGraphics());
											}
										}
									}
								}
							}

							vertexPositioner(node, vv.getGraphLayout());

							vertexPopup.remove(menu);
							vv.repaint();
						}
					}
				});
			} 
		}
        
		vertexPopup.add(menu);
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
    
    
    /**
     * Getter for the edge popup.
     * @return 
     */
    public JPopupMenu getInitialPopup() {
        return initialPopup;
    }
    
    /**
     * Setter for the Edge popup.
     * @param edgePopup 
     */
    public void setInitialPopup(JPopupMenu edgePopup) {
        this.initialPopup = edgePopup;
    }
    
    /**
     * Getter for the vertex popup.
     * @return 
     */
    public JPopupMenu getVertexPopup() {
        return vertexPopup;
    }
    
    /**
     * Setter for the vertex popup.
     * @param vertexPopup 
     */
    public void setVertexPopup(JPopupMenu vertexPopup) {
        this.vertexPopup = vertexPopup;
    }

    /*
     * This method add the node and the vertex to the current graph
     */
    private static void createTree(Node node, Graph<Node, String> graph){
		String str=null;
		if(!node.getChildNodeList().isEmpty()){
			for(Node childNode : node.getChildNodeList()){
				graph.addVertex(childNode);
				str=node.getNodeID()+childNode.getNodeID();
				((Forest<Node, String>) graph).addEdge(str, node, childNode);
				
				createTree(childNode, graph);
				
			}
		}
	}
}