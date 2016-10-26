package project;

import java.util.Iterator;
import java.util.ArrayDeque;

import project.CamDictionary.Node;

public class LevelIterator implements Iterator {

	Node root;
	
	ArrayDeque<Node> backingQueue = new ArrayDeque<Node>();
	
	public LevelIterator(CamDictionary dictionary) {
		this.root = dictionary.root;
		
		ArrayDeque<Node> nodeQueue = new ArrayDeque<Node>();
		nodeQueue.add(root);
		while (!nodeQueue.isEmpty()) {
			Node node = nodeQueue.poll();
			backingQueue.add(node);
			if (node.left != null)
				nodeQueue.add(node.left);
			if (node.right != null)
				nodeQueue.add(node.right);
		}
	}

	public boolean hasNext() {
		return !backingQueue.isEmpty();
	}

	public String next() {
		if (hasNext()) {
			return backingQueue.poll().item.toString();
		} else
			return null;
	}

	public void remove() {
	}

}
