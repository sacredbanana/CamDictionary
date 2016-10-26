using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CamDictionary
{
    interface Dictionary<E>
    {
	    bool isEmpty();
        
	    bool contains(E item);

    	bool hasPredecessor(E item);

	    bool hasSuccessor(E item);

	    E predecessor(E item);

	    E successor(E item);

	    E min();
	
	    E max();
	
	    bool add(E item);

	    bool delete(E item);

	    IIterator<E> iterator();

	    IIterator<E> iterator(E start);

	    String getLogString();

	    String toString();
    }
}
