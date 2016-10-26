using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CamDictionary
{
    interface IIterator<E>
    {
        bool hasNext();

        E next();

        void remove();
    }
}
