package project;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.List; // ONLY FOR THIRD PARTY TREE DISPLAY
import java.util.ArrayList; // ONLY FOR THIRD PARTY TREE DISPLAY

/**
 * CamDictionary is a class that creates an AVL Tree which can be modified, searched and examined.
 * @author Cameron Armstrong 21194619
 */
public class CamDictionary<E extends Comparable<E>> implements Dictionary<E> {
	/**
	 * The root of the dictionary
	 */
	public Node<E> root;
	
	/**
	 * The node returned by the last search
	 */
	private Node<E> lastSearch;
	
	/**
	 * A log string recording dictionary operations
	 */
	private String logString;
	
	/**
	 * A count of the number of comparisons in the last operation
	 */
	private int comparisons;
	
	/**
	 * A count of all total comparisons in lifetime. Used for performance analysis only.
	 */
	public int totalComparisons;
	
	/**
	 * A count of the max comparison count for all operations. Used for performance analysis only.
	 */
	public int maxComparisons;
	
	/**
	 * A switch toggling tree balancing
	 */
	public boolean noOptimisations;
	
	/**
	 * A switch toggling visual feedback of tree operations
	 */
	public boolean visualFeedback;
	
	/**
	 * Creates a new dictionary
	 */
	
	/**
	 * A switch toggled if the tree has been modified. Used for fail-safe iterator.
	 */
	public boolean treeModified;
	
	public CamDictionary() {
		root = null;
		lastSearch = null;
		logString = "";
		comparisons = 0;
		totalComparisons = 0;
		maxComparisons = 0;
		noOptimisations = false;
		visualFeedback = false;
		treeModified = false;
	}

	/**
	 * Checks if dictionary is empty
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Checks if the node is root
	 * @param node the node to check
	 * @return true if root
	 */
	public boolean isRoot(Node<E> node) {
		return node == root;
	}
	
	/**
	 * Checks if the dictionary contains an item
	 * @param item the item to search for
	 * @return true if found
	 */
	public boolean contains(E item) {
		boolean doesContain = false;
		
		if (search(item) != null)
			doesContain = true;
		
		addToLogString("Contains " + item);
		
		return doesContain;
	}

	/**
	 * Checks if there is a smaller item in the dictionary
	 * @param item the item to compare with
	 * @return true if there is a smaller item
	 */
	public boolean hasPredecessor(E item) {
		Node<E> entryNode = search(item);
		
		comparisons = 1;
		
		if (entryNode == null)
			return false;
		
		if (entryNode.left != null)
			return true;
		
		if (entryNode == root)
			return false;
		
		if (entryNode == entryNode.parent.left) { // If the node is the parent's left child, keep going up the tree until it is the right node
			Node<E> parent = entryNode.parent;
			Node<E> currentNode = entryNode;
			while (parent != null && currentNode == parent.left) {
				comparisons++;
				currentNode = parent;
				parent = parent.parent;
			}
			lastSearch = parent;
			return true;
		} else {
			lastSearch = entryNode.parent;
			return true;
		}
	}

	/**
	 * Checks if there is a larger item in the dictionary
	 * @param item the item to compare with
	 * @return true if there is a larger item
	 */
	public boolean hasSuccessor(E item) { // If the node is the parent's right child, keep going up the tree until it is the left node
		Node<E> entryNode = search(item);
		
		comparisons = 1;
		
		if (entryNode == null)
			return false;
		
		if (entryNode.right != null) {
			lastSearch = entryNode.right;
			return true;
		}
		
		if (entryNode == root)
			return false;
		
		if (entryNode == entryNode.parent.right) {
			Node<E> parent = entryNode.parent;
			Node<E> currentNode = entryNode;
			while (parent != null && currentNode == parent.right) {
				comparisons++;
				currentNode = parent;
				parent = parent.parent;
			}
			lastSearch = parent;
			return true;
		} else {
			lastSearch = entryNode.parent;
			return true;
		}
	}

	/**
	 * Retrieves the largest smaller item in the dictionary
	 * @param item the item to compare with
	 * @exception NoSuchElementException no such item exists
	 * @return the largest smaller item
	 */
	public E predecessor(E item) throws NoSuchElementException {
		if (hasPredecessor(item)) {
			addToLogString("Predecessor of " + item);
			return lastSearch.item;
		}
		else
			throw new NoSuchElementException("No such entry in dictionary.");
	}

	/**
	 * Retrieves the smallest larger item in the dictionary
	 * @param item the item to compare with
	 * @exception NoSuchElementException no such item exists
	 * @return the smallest larger item
	 */
	public E successor(E item) {
		if (hasSuccessor(item)) {
			addToLogString("Successor of " + item);
			return lastSearch.item;
		}
		else
			throw new NoSuchElementException("No such entry in dictionary.");
	}

	/**
	 * Retrieves the smallest item in the dictionary
	 * @exception NoSuchElementException the dictionary is empty
	 * @return the smallest item
	 */
	public E min() throws NoSuchElementException {
		if (!isEmpty()) {
			Node<E> currentNode = root;
			comparisons = 0;
			while (currentNode.left != null) {
				comparisons++;
				currentNode = currentNode.left;
			}
			addToLogString("Min");
			return currentNode.item;
		} else
			throw new NoSuchElementException("The dictionary is empty.");
	}

	/**
	 * Retrieves the largest item in the dictionary
	 * @exception NoSuchElementException the dictionary is empty
	 * @return the largest item
	 */
	public E max() throws NoSuchElementException {
		if (!isEmpty()) {
			Node<E> currentNode = root;
			comparisons = 0;
			while (currentNode.right != null) {
				comparisons++;
				currentNode = currentNode.right;
			}
			addToLogString("Max");
			return currentNode.item;
		} else
			throw new NoSuchElementException("The dictionary is empty.");
	}

	/**
	 * Adds an item into the dictionary
	 * @param item the item to be added
	 * @return true if added
	 */
	public boolean add(E item) {
		if (visualFeedback)
			System.out.println("Adding " + item);
		Node<E> currentNode = root;
		comparisons = 0;
		
		while (true) { // Infinite loop until it finds a suitable place to insert node
			if (isEmpty()) {
				root = new Node<E>(item, null, null);
				root.height = 0;
				if (visualFeedback) {
					System.out.println("Added " + item);
					print();
				}
				comparisons++;
				addToLogString("Add " + item);
				treeModified = true;
				return true;
			}
			
			if (currentNode.compareTo(item) == 0) { // Equal node found, can't insert
				if (visualFeedback)
					System.out.println("Equal!");
				return false;
			}
			
			if (currentNode.compareTo(item) > 0) { // Lesser node found, go right
				comparisons++;
				if (visualFeedback)
					System.out.println(item + " is larger than " + currentNode.item);
				if (currentNode.right != null)  // Right child present, traverse down
					currentNode = currentNode.right;
				else { // No child present, insert here
					currentNode.right = new Node<E>(item, null, null);
					currentNode.right.parent = currentNode;
					if (visualFeedback)
						System.out.println("Added " + item);
					if (!noOptimisations)
						balance(currentNode.right);
					if (visualFeedback)
						print();
					addToLogString("Add " + item);
					treeModified = true;
					return true;
				}
				continue;
			}
			
			if (currentNode.compareTo(item) < 0) { // Greater node found, go left
				comparisons++;
				if (visualFeedback)
					System.out.println(item + " is smaller than " + currentNode.item);
				if (currentNode.left != null) // Left child present, traverse down
					currentNode = currentNode.left;
				else { // No child present, insert here
					currentNode.left = new Node<E>(item, null, null);
					currentNode.left.parent = currentNode;
					if (visualFeedback)
						System.out.println("Added " + item);
					if (!noOptimisations)
						balance(currentNode.left);
					if (visualFeedback)
						print();
					addToLogString("Add " + item);
					treeModified = true;
					return true;
				}
				continue;
			}
		}
	}
	
	/**
	 * Calculates and sets the height of a node
	 * @param node the node to calculate the height of
	 */
	private void calculateHeight(Node<E> node) {
		if (node == null)
			return;
		
		if (node.height == -1) // If leaf set height to 0, otherwise calculate height
			node.height = 0;
		else
			node.height = Math.max(height(node.left), height(node.right)) + 1;
	}
	
	/**
	 * Balances the tree to conform to AVL Tree specification
	 * @param startingNode the node to start optimisation from
	 */
	private void balance(Node<E> startingNode) {
		comparisons++;
		if (startingNode == null) // Null node can't be balanced
			return;

		calculateHeight(startingNode);  // Calculate height of node

		int balance = height(startingNode.right) - height(startingNode.left);
		if (balance < -1) { // Tree is left heavy so do rotations
			if (visualFeedback) {
				print();
				System.out.println(startingNode.item + " is left heavy. Optimising tree.");
			}
			if (height(startingNode.left.left) >= height(startingNode.left.right)) {
				rotateRight(startingNode);
			}
			else
				rotateLeftThenRight(startingNode);
			if (visualFeedback)
				print();
		}	else if (balance > 1) { // Tree is right heavy so do rotations
			if (visualFeedback) {
				print();
				System.out.println(startingNode.item + " is right heavy. Optimising tree.");
			}
			if (height(startingNode.right.right) >= height(startingNode.right.left))
				rotateLeft(startingNode);
			else
				rotateRightThenLeft(startingNode);
		} else {
			balance(startingNode.parent); // Rebalance
		}
	}

	/**
	 * Performs a left rotation
	 * @param node the pivot node of the rotation
	 */
	private void rotateLeft(Node<E> node) {
		Node<E> rightNode = node.right;
		
		if (node.parent == null)
			root = rightNode;
		
		rightNode.parent = node.parent;
		node.right = rightNode.left;
		
		if (node.right != null)
			node.right.parent = node;
		
		rightNode.left = node;
		node.parent = rightNode;
		
		if (rightNode.parent != null) {
			if (rightNode.parent.right == node)
				rightNode.parent.right = rightNode;
			else
				rightNode.parent.left = rightNode;
		}
		
		calculateHeight(node); // Calculate new heights
		calculateHeight(rightNode);
		
		if (visualFeedback)
			System.out.println("Rotated left");
	}
	
	/**
	 * Performs a right rotation
	 * @param node the pivot node of the rotation
	 */
	private void rotateRight(Node<E> node) {
		Node<E> leftNode = node.left;
		
		if (node.parent == null)
			root = leftNode;
		
        leftNode.parent = node.parent;
        node.left = leftNode.right;
 
        if (node.left != null)
            node.left.parent = node;
 
        leftNode.right = node;
        node.parent = leftNode;
 
        if (leftNode.parent != null) {
            if (leftNode.parent.right == node)
                leftNode.parent.right = leftNode;
            else
                leftNode.parent.left = leftNode;
        }
        
        calculateHeight(node); // Calculate new heights
		calculateHeight(leftNode);
		
		if (visualFeedback)
			System.out.println("Rotated right");
	}
	
	/**
	 * Performs a left rotation then a right rotation
	 * @param node the pivot node of the rotation
	 */
	private void rotateLeftThenRight(Node<E> node) {
		rotateLeft(node.left);
		rotateRight(node);
	}
	
	/**
	 * Performs a right rotation then a left rotation
	 * @param node the pivot node of the rotation
	 */
	private void rotateRightThenLeft(Node<E> node) {
		rotateRight(node.right);
		rotateLeft(node);
	}
	
	/**
	 * Deletes an item from the dictionary
	 * @param item the item to delete
	 * @return true if successful
	 */
	public boolean delete(E item) {
		Node<E> currentNode = search(item);
		
		addToLogString("Delete " + item);
		
		if (currentNode == null) // If node not present in tree do nothing
			return false;
		
		// Node has no children
		if (currentNode.left == null && currentNode.right == null) {
			if (currentNode.parent.left == currentNode) {
				currentNode.parent.left = null;
			} else {
				currentNode.parent.right = null;
			}
			balance(currentNode.parent); // Rebalance
			if (visualFeedback)
				print();
			treeModified = true;
			return true;
		}
		
		// Node has 1 child
		if (currentNode.left != null ^ currentNode.right != null) {
			Node<E> parentNode = currentNode.parent;
			if (currentNode.left != null) {
				if (parentNode.right == currentNode) {
					parentNode.right = currentNode.left;
					parentNode.right.parent = parentNode;
				} else {
					parentNode.left = currentNode.left;
					parentNode.left.parent = parentNode;
				}
			} else {
				if (parentNode.right == currentNode) {
					parentNode.right = currentNode.right;
					parentNode.right.parent = parentNode;
				} else {
					parentNode.left = currentNode.right;
					parentNode.left.parent = parentNode;
				}
			}
			balance(currentNode.parent); // Rebalance
			if (visualFeedback)
				print();
			treeModified = true;
			return true;
		}
		
		// Node has 2 children
		Node<E> minimumRightNode = currentNode.right.minEntry();
		currentNode.item = minimumRightNode.item;
		
		if (minimumRightNode == minimumRightNode.parent.left) {
			minimumRightNode.parent.left = null;
		} else {
			minimumRightNode.parent.right = null;
		}
		balance(currentNode.parent); // Rebalance
		if (visualFeedback)
			print();
		treeModified = true;
		return true;
	}

	/**
	 * Provides an iterator for the dictionary starting with the smallest item
	 * @return the iterator
	 */
	public Iterator<E> iterator() {
		return new DictionaryIterator(this);
	}

	/**
	 * Provides an iterator for the dictionary starting with item provided
	 * @param start where to start iterating from
	 * @return the iterator
	 */
	public Iterator<E> iterator(E start) {
		return new DictionaryIterator(this, start);
	}

	/**
	 * Adds an entry to the log
	 * @param operation the operation to add
	 */
	private void addToLogString(String operation) {
		logString += "Operation (";
		logString += operation;
		logString += ") completed using [";
		logString += comparisons;
		logString += "] comparisons\n";
		totalComparisons += comparisons; // Used for performance analysis tool
		maxComparisons = Math.max(maxComparisons, comparisons); // Used for performance analysis tool
	}
	
	/**
	 * Gets the log string then clears it
	 * @return the log string
	 */
	public String getLogString() {
		if (logString.equals(""))
			return "The log is empty.";
		String log = logString;
		logString = "";
		return log;
	}
	
	/**
	 * Performs a search in the dictionary
	 * @param entry the entry to search for
	 * @return the node when found, or null if not found
	 */
	public Node<E> search(E entry) {
		Node<E> currentNode = root;
		boolean searching = true;
		comparisons = 0;
		while (searching) {
			if (currentNode.compareTo(entry) == 0) { // Node found
				comparisons++;
				if (visualFeedback)
					System.out.println("Found");
				lastSearch = currentNode;
				return currentNode;
			}
					
			if (currentNode.compareTo(entry) < 0) { // Node greater, go left
				comparisons++;
				if (currentNode.left != null)
					currentNode = currentNode.left;
				else
					searching = false;
			}
			
			if (currentNode.compareTo(entry) > 0) { // Node lesser, go right
				comparisons++;
				if (currentNode.right != null)
					currentNode = currentNode.right;
				else
					searching = false;
			}
		}
		if (visualFeedback)
			System.out.println("Not found");
		return null;
	}
	
	/**
	 * Gets the height of a node
	 * @param node the node
	 * @return the height
	 */
	private int height(Node<E> node) {
		if (node == null) // Null nodes have a height of -1 to make height calculation work 
			return -1; 
		else
			return node.height;
	}
	
	/**
	 * Gets a string representation of the dictionary
	 * @return string representation
	 */
	public String toString() {
		if (isEmpty()) // Nothing in tree
			return "Empty dictionary.";
		
		Iterator<E> iterator = iterator();
		String dictionaryString = "";
		while (iterator.hasNext()) {
			dictionaryString += iterator.next();
			dictionaryString += "\n";
		}
		
		return dictionaryString;
	}
	
	/**
	 * Calls the third party print method on this dictionary
	 */
	public void print(){
		print(root);
	}
	
	/**
	 * Prints a diagram of the dictionary to the console.
	 * Note: This method uses the java.util collection classes but my code does not.
	 * Source: http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * @author MightyPork
	 * @param root the root of the tree
	 */
	private void print(Node<E> root) // Not my code. Requires java.util Collections
    {
		System.out.println("");
        List<List<String>> lines = new ArrayList<List<String>>();

        List<Node<E>> level = new ArrayList<Node<E>>();
        List<Node<E>> next = new ArrayList<Node<E>>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (Node<E> n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = (String)n.item.toString();
                    aa += " ";
                    aa += n.height;
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.left);
                    next.add(n.right);

                    if (n.left != null) nn++;
                    if (n.right != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<Node<E>> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '|' : 'J'; //┴ ┘
                        } else {
                            if (j < line.size() && line.get(j) != null) c = 'L'; //└
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
        System.out.println("");
    }  
	
	/**
	 * A node in the dictionary
	 * @author Cameron Armstrong 21194619
	 */
	public class Node<T extends Comparable<T>> implements Comparable<T> {
		/**
		 * The data stored in the node
		 */
		public T item;
		
		/**
		 * The node's left child
		 */
		public Node<T> left;
		
		/**
		 * The node's right child
		 */
		public Node<T> right;
		
		/**
		 * The node's parent
		 */
		public Node<T> parent;
		
		/**
		 * The node's height
		 */
		private int height;
		
		/**
		 * Creates a new node
		 * @param item the item to add to the node
		 * @param left the left child
		 * @param right the right child
		 */
		public Node(T item, Node<T>left, Node<T>right) {
			this.item = item;
			this.left = left;
			this.right = right;
			this.height = -1;
		}

		/**
		 * Compares node to item
		 * @param e item to compare with
		 * @return -1 if less, 0 if equal, 1 if greater
		 */
		public int compareTo(T e) {
			return e.compareTo(this.item);
		}
		
		/**
		 * Calculates the minimum right node of the node
		 * @return the minimum right node
		 */
		public Node<T> minEntry() { // Required for rotations
			if (left == null)
				return this;
			else
				return left.minEntry();
		}
	}
	
	/**
	 * An iterator for the dictionary
	 * @author Cameron Armstrong 21194619
	 */
	private class DictionaryIterator implements Iterator<E> {		
		/**
		 * The backing queue for the iterator
		 */
		private Queue<E> queue; // Uses own built queue in Queue.java
		
		/**
		 * Boolean for if the last operation was next
		 */
		private boolean lastOperationNext;
		
		/**
		 * The last item returned
		 */
		private E lastItemReturned;
		
		/**
		 * The dictionary to iterate over
		 */
		private CamDictionary<E> dictionary;
		
		/**
		 * Creates a new iterator starting from the minimum entry
		 * @param dictionary the dictionary to iterate over
		 */
		public DictionaryIterator(CamDictionary<E> dictionary) {
			queue = new Queue<E>();
			this.dictionary = dictionary;
			lastOperationNext = false;
			lastItemReturned = null;
			buildQueue(dictionary.root);
			dictionary.treeModified = false;
		}
		
		/**
		 * Creates a new iterator starting from the provided entry
		 * @param dictionary the dictionary to iterate over
		 * @param startingItem the starting item
		 */
		public DictionaryIterator(CamDictionary<E> dictionary, E startingItem) {
			queue = new Queue<E>();
			this.dictionary = dictionary;
			lastOperationNext = false;
			lastItemReturned = null;
			Node<E> startingNode = dictionary.search(startingItem);
			buildQueue(dictionary.root, startingNode);
			dictionary.treeModified = false;
		}
		
		/**
		 * Checks if there is another item
		 * @return true if there is another item
		 */
		public boolean hasNext() throws ConcurrentModificationException {
			if (!dictionary.treeModified) {
				lastOperationNext = false;
				return !queue.isEmpty();
			} else
				throw new ConcurrentModificationException("The dictionary has been modified since iterator's creation.");
		}

		/**
		 * Retrieves the next item
		 * @return the next item
		 */
		public E next() throws ConcurrentModificationException {
			if (!dictionary.treeModified) {
				lastOperationNext = true;
				lastItemReturned = queue.dequeue();
				return lastItemReturned;
			} else
				throw new ConcurrentModificationException("The dictionary has been modified since iterator's creation.");
		}

		/**
		 * Remove the last item retrieved by next
		 * @exception UnsupportedOperatoinException the last operation was not next
		 */
		public void remove() throws UnsupportedOperationException, ConcurrentModificationException {
			if (dictionary.treeModified)
				throw new ConcurrentModificationException("The dictionary has been modified since the iterator's creation.");
			
			if (lastOperationNext) {
				if (visualFeedback)
					System.out.println("Deleting " + lastItemReturned);
				dictionary.delete(lastItemReturned);
				dictionary.treeModified = false;
				lastOperationNext = false;
			} else
				throw new UnsupportedOperationException("Cannot remove immediately after operation that is not next.");
		}
		
		/**
		 * Builds a backing queue of entire tree
		 * @param node root of tree
		 */
		private void buildQueue(Node<E> node) {
			if (node.left != null)
				buildQueue(node.left);
			queue.enqueue(node.item);
			if (node.right != null)
				buildQueue(node.right);
		}
		
		/**
		 * Builds a backing queue of tree with items greater or equal to provided value
		 * @param node root of tree
		 * @param startingNode minimum value
		 */
		private void buildQueue(Node<E> node, Node<E> startingNode) {
			if (node.left != null)
				buildQueue(node.left, startingNode);
			if (startingNode.compareTo(node.item) <= 0)
				queue.enqueue(node.item);
			if (node.right != null)
				buildQueue(node.right, startingNode);
		}
	}
}