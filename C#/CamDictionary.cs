using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CamDictionary
{
    class CamDictionary<E> : Dictionary<E> where E : IComparable
    {
        private Node<E> root;

        private Node<E> lastSearch;

        private string logString;

        private int comparisons;

        public int totalComparisons;

        public int maxComparisons;

        public bool noOptimisations;

        public bool visualFeedback;

        public bool treeModified;

        public CamDictionary()
        {
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

        public bool isEmpty()
        {
            return root == null;
        }

        private bool isRoot(Node<E> node)
        {
            return node == root;
        }

        public bool contains(E item)
        {
            bool doesContain = false;

            if (search(item) != null)
                doesContain = true;

            addToLogString("Contains " + item);

            return doesContain;
        }

        public bool hasPredecessor(E item)
        {
            Node<E> entryNode = search(item);

            comparisons = 1;

            if (entryNode == null)
                return false;

            if (entryNode.left != null)
                return true;

            if (entryNode == root)
                return false;

            if (entryNode == entryNode.parent.left)
            { // If the node is the parent's left child, keep going up the tree until it is the right node
                Node<E> parent = entryNode.parent;
                Node<E> currentNode = entryNode;
                while (parent != null && currentNode == parent.left)
                {
                    comparisons++;
                    currentNode = parent;
                    parent = parent.parent;
                }
                lastSearch = parent;
                return true;
            }
            else
            {
                lastSearch = entryNode.parent;
                return true;
            }
        }

        public bool hasSuccessor(E item)
        {
            Node<E> entryNode = search(item);

            comparisons = 1;

            if (entryNode == null)
                return false;

            if (entryNode.right != null)
            {
                lastSearch = entryNode.right;
                return true;
            }

            if (entryNode == root)
                return false;

            if (entryNode == entryNode.parent.right)
            {
                Node<E> parent = entryNode.parent;
                Node<E> currentNode = entryNode;
                while (parent != null && currentNode == parent.right)
                {
                    comparisons++;
                    currentNode = parent;
                    parent = parent.parent;
                }
                lastSearch = parent;
                return true;
            }
            else
            {
                lastSearch = entryNode.parent;
                return true;
            }
        }

        public E predecessor(E item)
        {
            if (hasPredecessor(item))
            {
                addToLogString("Predecessor of " + item);
                return lastSearch.item;
            }
            else
                throw new Exception("No such entry in dictionary.");
        }

        public E successor(E item)
        {
            if (hasSuccessor(item))
            {
                addToLogString("Successor of " + item);
                return lastSearch.item;
            }
            else
                throw new Exception("No such entry in dictionary.");
        }

        public E min()
        {
            if (!isEmpty())
            {
                Node<E> currentNode = root;
                comparisons = 0;
                while (currentNode.left != null)
                {
                    comparisons++;
                    currentNode = currentNode.left;
                }
                addToLogString("Min");
                return currentNode.item;
            }
            else
                throw new Exception("The dictionary is empty.");
        }

        public E max()
        {
            if (!isEmpty())
            {
                Node<E> currentNode = root;
                comparisons = 0;
                while (currentNode.right != null)
                {
                    comparisons++;
                    currentNode = currentNode.right;
                }
                addToLogString("Max");
                return currentNode.item;
            }
            else
                throw new Exception("The dictionary is empty.");
        }

        public bool add(E item)
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            if (visualFeedback)
			    Console.WriteLine("Adding " + item);
		    Node<E> currentNode = root;
		    comparisons = 0;

            Console.ForegroundColor = ConsoleColor.Magenta;
		    while (true) { // Infinite loop until it finds a suitable place to insert node
			    if (isEmpty()) {
				    root = new Node<E>(item, null, null);
				    root.height = 0;
                    Console.ForegroundColor = ConsoleColor.Blue;
				    if (visualFeedback) {
					    Console.WriteLine("Added " + item);
					    print();
				    }
				    comparisons++;
				    addToLogString("Add " + item);
				    treeModified = true;
				    return true;
			    }
			
			    if (currentNode.CompareTo(item) == 0) { // Equal node found, can't insert
				    if (visualFeedback)
					    Console.WriteLine("Equal!");
				    return false;
			    }
			
			    if (currentNode.CompareTo(item) > 0) { // Lesser node found, go right
				    comparisons++;
				    if (visualFeedback)
					    Console.WriteLine(item + " is larger than " + currentNode.item);
				    if (currentNode.right != null)  // Right child present, traverse down
					    currentNode = currentNode.right;
				    else { // No child present, insert here
					    currentNode.right = new Node<E>(item, null, null);
					    currentNode.right.parent = currentNode;
                        Console.ForegroundColor = ConsoleColor.Blue;
                        if (visualFeedback)
						    Console.WriteLine("Added " + item);
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
			
			    if (currentNode.CompareTo(item) < 0) { // Greater node found, go left
				    comparisons++;
				    if (visualFeedback)
					    Console.WriteLine(item + " is smaller than " + currentNode.item);
				    if (currentNode.left != null) // Left child present, traverse down
					    currentNode = currentNode.left;
				    else { // No child present, insert here
					    currentNode.left = new Node<E>(item, null, null);
					    currentNode.left.parent = currentNode;
                        Console.ForegroundColor = ConsoleColor.Blue;
                        if (visualFeedback)
						    Console.WriteLine("Added " + item);
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

        private void calculateHeight(Node<E> node)
        {
            if (node == null)
                return;

            if (node.height == -1) // If leaf set height to 0, otherwise calculate height
                node.height = 0;
            else
                node.height = Math.Max(height(node.left), height(node.right)) + 1;
        }

        private void balance(Node<E> startingNode) 
        {
		    comparisons++;
		    if (startingNode == null) // Null node can't be balanced
			    return;

		    calculateHeight(startingNode);  // Calculate height of node

		    int balance = height(startingNode.right) - height(startingNode.left);
		    if (balance < -1) { // Tree is left heavy so do rotations
			    if (visualFeedback) {
				    print();
                    Console.ForegroundColor = ConsoleColor.Cyan;
				    Console.WriteLine(startingNode.item + " is left heavy. Optimising tree.");
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
                    Console.ForegroundColor = ConsoleColor.Cyan;
                    Console.WriteLine(startingNode.item + " is right heavy. Optimising tree.");
			    }
			    if (height(startingNode.right.right) >= height(startingNode.right.left))
				    rotateLeft(startingNode);
			    else
				    rotateRightThenLeft(startingNode);
		    } else {
			    this.balance(startingNode.parent); // Rebalance
		    }
	    }

        private void rotateLeft(Node<E> node) 
        {
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
                Console.WriteLine("Rotated left");
	    }

        private void rotateRight(Node<E> node) 
        {
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
                Console.WriteLine("Rotated right");
	    }

        private void rotateLeftThenRight(Node<E> node)
        {
            rotateLeft(node.left);
            rotateRight(node);
        }

        private void rotateRightThenLeft(Node<E> node)
        {
            rotateRight(node.right);
            rotateLeft(node);
        }

        public bool delete(E item)
        {
            Node<E> currentNode = search(item);

            addToLogString("Delete " + item);

            if (currentNode == null) // If node not present in tree do nothing
                return false;

            // Node has no children
            if (currentNode.left == null && currentNode.right == null)
            {
                if (currentNode.parent.left == currentNode)
                {
                    currentNode.parent.left = null;
                }
                else
                {
                    currentNode.parent.right = null;
                }
                balance(currentNode.parent); // Rebalance
                if (visualFeedback)
                    print();
                treeModified = true;
                return true;
            }

            // Node has 1 child
            if (currentNode.left != null ^ currentNode.right != null)
            {
                Node<E> parentNode = currentNode.parent;
                if (currentNode.left != null)
                {
                    if (parentNode.right == currentNode)
                    {
                        parentNode.right = currentNode.left;
                        parentNode.right.parent = parentNode;
                    }
                    else
                    {
                        parentNode.left = currentNode.left;
                        parentNode.left.parent = parentNode;
                    }
                }
                else
                {
                    if (parentNode.right == currentNode)
                    {
                        parentNode.right = currentNode.right;
                        parentNode.right.parent = parentNode;
                    }
                    else
                    {
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

            if (minimumRightNode == minimumRightNode.parent.left)
            {
                minimumRightNode.parent.left = null;
            }
            else
            {
                minimumRightNode.parent.right = null;
            }
            balance(currentNode.parent); // Rebalance
            if (visualFeedback)
                print();
            treeModified = true;
            return true;
        }

        public IIterator<E> iterator()
        {
            return new DictionaryIterator(this);
        }

        public IIterator<E> iterator(E start)
        {
            return new DictionaryIterator(this, start);
        }

        private void addToLogString(string operation)
        {
            logString += "Operation (";
            logString += operation;
            logString += ") completed using [";
            logString += comparisons;
            logString += "] comparisons\n";
            totalComparisons += comparisons; // Used for performance analysis tool
            maxComparisons = Math.Max(maxComparisons, comparisons); // Used for performance analysis tool
        }

        public string getLogString()
        {
            if (logString.Equals(""))
                return "The log is empty.";
            String log = logString;
            logString = "";
            return log;
        }

        private Node<E> search(E entry)
        {
            Node<E> currentNode = root;
		    bool searching = true;
		    comparisons = 0;
		    while (searching) {
			    if (currentNode.CompareTo(entry) == 0) { // Node found
				    comparisons++;
				    if (visualFeedback)
					    Console.WriteLine("Found");
				    lastSearch = currentNode;
				    return currentNode;
			    }
					
			    if (currentNode.CompareTo(entry) < 0) { // Node greater, go left
				    comparisons++;
				    if (currentNode.left != null)
					    currentNode = currentNode.left;
				    else
					    searching = false;
			    }
			
			    if (currentNode.CompareTo(entry) > 0) { // Node lesser, go right
				    comparisons++;
				    if (currentNode.right != null)
					    currentNode = currentNode.right;
				    else
					    searching = false;
			    }
		    }
		    if (visualFeedback)
                Console.WriteLine("Not found");
		    return null;
        }

        private int height(Node<E> node)
        {
            if (node == null)
                return -1;
            else
                return node.height;
        }

        public string toString()
        {
            if (isEmpty()) // Nothing in tree
                return "Empty dictionary.";

            IIterator<E> iterator = this.iterator();
            String dictionaryString = "";
            while (iterator.hasNext())
            {
                dictionaryString += iterator.next();
                dictionaryString += "\n";
            }

            return dictionaryString;
        }

        public void print()
        {
            print(root);
        }

		private void print(Node<E> root)
		{
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("");
        List<List<String>> lines = new List<List<String>>();

        List<Node<E>> level = new List<Node<E>>();
        List<Node<E>> next = new List<Node<E>>();

        level.Add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new List<String>();

            nn = 0;

            foreach (Node<E> n in level) {
                if (n == null) {
                    line.Add(null);

                    next.Add(null);
                    next.Add(null);
                } else {
                    String aa = (String)n.item.ToString();
                    aa += " ";
                    aa += n.height;
                    line.Add(aa);
                    if (aa.Length > widest) widest = aa.Length;

                    next.Add(n.left);
                    next.Add(n.right);

                    if (n.left != null) nn++;
                    if (n.right != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.Add(line);

            List<Node<E>> tmp = level;
            level = next;
            next = tmp;
            next.Clear();
        }

        int perpiece = lines[lines.Count() - 1].Count() * (widest + 4);
        for (int i = 0; i < lines.Count(); i++) {
            List<String> line = lines[i];
            int hpw = (int) Math.Floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.Count(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line[j - 1] != null) {
                            c = (line[j] != null) ? '┴' : '┘'; //┴ ┘
                        } else {
                            if (j < line.Count() && line[j] != null) c = '└'; //└
                        }
                    }
                    Console.Write(c);

                    // lines and spaces
                    if (line[j] == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            Console.Write(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            Console.Write(j % 2 == 0 ? " " : "─");
                        }
                        Console.Write(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            Console.Write(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                Console.WriteLine();
            }

            // print line of numbers
            for (int j = 0; j < line.Count(); j++) {

                String f = line[j];
                if (f == null) f = "";
                int gap1 = (int) Math.Ceiling(perpiece / 2f - f.Length / 2f);
                int gap2 = (int) Math.Floor(perpiece / 2f - f.Length / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    Console.Write(" ");
                }
                Console.Write(f);
                for (int k = 0; k < gap2; k++) {
                    Console.Write(" ");
                }
            }
            Console.WriteLine();

            perpiece /= 2;
        }
        Console.WriteLine("");
        Console.ForegroundColor = ConsoleColor.Gray;
		}

        private class Node<T> : IComparable<T> where T : IComparable
        {
            public T item;
            public Node<T> left;
            public Node<T> right;
            public Node<T> parent;
            public int height;

            public Node(T item, Node<T> left, Node<T> right) 
            {
                this.item = item;
                this.left = left;
                this.right = right;
                this.height = -1;
            }

            public int CompareTo(T e)
            {
                return e.CompareTo(this.item);
            }

            public Node<T> minEntry()
            {
                if (left == null)
                    return this;
                else
                    return left.minEntry();
            }
        }

        private class DictionaryIterator : IIterator<E>
        {
            private Queue<E> queue;

            private bool lastOperationNext;

            private E lastItemReturned;

            private CamDictionary<E> dictionary;

            public DictionaryIterator(CamDictionary<E> dictionary)
            {
                queue = new Queue<E>();
                this.dictionary = dictionary;
                lastOperationNext = false;
                lastItemReturned = default(E);
                buildQueue(dictionary.root);
                dictionary.treeModified = false;
            }

            public DictionaryIterator(CamDictionary<E> dictionary, E startingItem)
            {
                queue = new Queue<E>();
                this.dictionary = dictionary;
                lastOperationNext = false;
                lastItemReturned = default(E);
                Node<E> startingNode = dictionary.search(startingItem);
                buildQueue(dictionary.root, startingNode);
                dictionary.treeModified = false;
            }

            public bool hasNext()
            {
                if (!dictionary.treeModified)
                {
                    lastOperationNext = false;
                    return queue.Count > 0;
                }
                else
                    throw new Exception("This dictionary has been modified.");
            }

            public E next()
            {
                if (!dictionary.treeModified)
                {
                    lastOperationNext = true;
                    lastItemReturned = queue.Dequeue();
                    return lastItemReturned;
                }
                else
                    throw new Exception("This dictionary has been modified.");
            }

            public void remove()
            {
                if (dictionary.treeModified)
                    throw new Exception("The dictionary has been modified.");

                if (lastOperationNext)
                {
                    if (dictionary.visualFeedback)
                        Console.WriteLine("Deleting " + lastItemReturned);
                    dictionary.delete(lastItemReturned);
                    dictionary.treeModified = false;
                    lastOperationNext = false;
                }
                else
                    throw new Exception("Cannot remove immediately after operation that is not next.");
            }

            private void buildQueue(Node<E> node)
            {
                if (node.left != null)
                    buildQueue(node.left);
                queue.Enqueue(node.item);
                if (node.right != null)
                    buildQueue(node.right);
            }

            private void buildQueue(Node<E> node, Node<E> startingNode)
            {
                if (node.left != null)
                    buildQueue(node.left, startingNode);
                if (startingNode.CompareTo(node.item) <= 0)
                    queue.Enqueue(node.item);
                if (node.right != null)
                    buildQueue(node.right, startingNode);
            }
        }
    }
}
