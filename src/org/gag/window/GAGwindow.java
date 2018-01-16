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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Paint;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.commons.collections15.Transformer;
import org.gag.node.Node;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 * This class create the visualization window 
 * @author Andre GUENEY
 *
 */
public class GAGwindow {
	
	protected Dimension dimension=null;
	protected static VisualizationViewer<Node,String> vv=null;
	protected TreeLayout<Node,String> treeLayout = null;
	protected Graph<Node,String> graph = null;
	protected JFrame frame = null;
	protected static JPopupMenu jpm = new JPopupMenu();
	
	public GAGwindow(){
		
	}

	public void createWindow(String menuFilePath, String popUpFilePath, Dimension dimension, Graph<Node,String> gagGraph){
		
		this.dimension = dimension;
		this.graph = gagGraph;
		frame = new GAGframe();
		
			//Create the layout that content the graph
		this.treeLayout = new TreeLayout<Node,String>((DelegateForest<Node, String>) graph);
		
			//Create the viewer
		vv = new VisualizationViewer<Node,String>(treeLayout, this.dimension);
		
        vv.setBackground(Color.white);
        
        	/*
        	 * EdgeShape.Line() is used to define the shape of the edges
        	 */
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        
        	/*
        	 * MyVertexLabeller is a specific class used to take the ID from the node and
        	 * use it to put a label on the vertex
        	 */
        vv.getRenderContext().setVertexLabelTransformer(new MyVertexLabeller<Node>());
        
        	/*
        	 * PickableEdgePaintTransformer is a JUNG class that allow to change the color of an edge
        	 * when we click on it.
        	 */
        vv.getRenderContext().setEdgeDrawPaintTransformer(
        		new PickableEdgePaintTransformer<String>(vv.getPickedEdgeState(), Color.black, Color.cyan));
        
        	/*
        	 * MyVertexFillPaintFunction is a specific class used to color a vertex.
        	 * Green when the vertex is an open node
        	 * Red when the vertex is a closed node
        	 * Yellow when the vertex is selected
        	 */
        vv.getRenderContext().setVertexFillPaintTransformer(new MyVertexFillPaintFunction<Node>());
        
        	/*
        	 * This ToolTip is used to show the affectation and contraction part of the node under
        	 * the mouse cursor
        	 */
        vv.setVertexToolTipTransformer(new Transformer<Node,String>() {
			public String transform(Node node) {
				String str="";
				if(node.getTask().getAffectations()==null){
					str+="<html>Affectation: empty";
				} else str+="<html>Affectation: "+node.getTask().getAffectations().toString();
				
				if(node.getTask().getContractions()==null){
					str+="<p>Contraction: empty";
				} else str+="<p>Contraction: "+node.getTask().getContractions().toString();
				
				return str;
			}});

        	/*
        	 * This renderer set the position of the label for all vertex(node)
        	 */
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        final DefaultModalGraphMouse<Node, String> graphMouse = new DefaultModalGraphMouse<Node, String>();
        
        	/*
        	 * Create the mouse plugin to create our own popup menu
        	 */
        PopupVertexMenuMousePlugin<Node, String> myPlugin = new PopupVertexMenuMousePlugin<Node, String>(frame);
        
        jpm = new PopUpMenu(popUpFilePath,vv).getPopUpMenu();
        
        myPlugin.setInitialPopup(jpm);

		graphMouse.add(myPlugin);   // Add our new plugin to the mouse

		vv.addKeyListener(graphMouse.getModeKeyListener());

        vv.setGraphMouse(graphMouse);

        	/*
        	 * Set the ModalGraphMouse in picking mode
        	 */
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        final GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
//        content.add(gzsp);
        
        
        	/*
        	 * Create a scrolling panel where we put some information (right side of the split panel window)
        	 */
        MyInfoPanel infoPanel = new MyInfoPanel();
        JScrollPane jsp = new JScrollPane(infoPanel);
        jsp.setBackground(Color.WHITE);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        	/*
        	 * Add a panel in the bottom of the window (Not used yet. For a future use)
        	 */
        Container contSouth = new Container();
        contSouth=new JPanel();

        
        	/*
        	 * Create the split panel that contains two other panel :
        	 * 	- Left side : the viewing Graph part.
        	 * 	- Right side : the information part
        	 */
        JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,gzsp,jsp);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setResizeWeight(0.8);
        splitPanel.setBackground(Color.WHITE);
        
        content.add(splitPanel);
        content.add(contSouth, BorderLayout.SOUTH);

        	//Add a menu bar to the viewing window
        frame.setJMenuBar(new MenuBar(menuFilePath, vv, frame).getMenuBar());

        frame.setContentPane(content);
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	/**
	 * This class is used to get the ID of the nodes to labelling the vertex
	 * @author Andre GUENEY
	 *
	 * @param <V>
	 */
	public class MyVertexLabeller<V> implements Transformer<V,String> {

		@Override
		public String transform(V v) {
			return ((Node) v).getNodeID();
		}
		
	}

	/**
	 * This class is used to color the vertex. Green when it is open, red when it is closed and yellow when it is selected.
	 * @author Andre GUENEY
	 *
	 * @param <V>
	 */
	
	public class MyVertexFillPaintFunction<V> implements Transformer<V,Paint> {

		public Paint transform( V v ) {
			if(vv.getPickedVertexState().isPicked((Node) v)) return Color.YELLOW;
			else {
				if (((Node) v).isOpen()) {
					return Color.GREEN;
				} else return Color.RED;
			}
		}
	}
	
	
	/**
	 * This class update the text in tthe information panel
	 * @author Andre GUENEY
	 *
	 */
	public static class MyInfoPanel extends JPanel {
		
		// TODO Need to fixed some bug here. When the text exceed the information panel, scroll bars don't appears.
		
		protected static String history="";
		
		public void updateHistory(String text){
			history+=text;
		}

		public void paintComponent(Graphics g){

			Font font = new Font("Arial", Font.BOLD, 10);
			g.setFont(font);
			int y = 10;
			for(String line : history.split("\n")) {
				g.drawString(line, 10, y+=g.getFontMetrics().getHeight());
			}
		  }   
	}
	
	
}
	
	
	


