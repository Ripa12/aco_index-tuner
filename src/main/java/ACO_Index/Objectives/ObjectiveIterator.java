package ACO_Index.Objectives;

import java.util.Iterator;

public class ObjectiveIterator<MyAbstractObjective> implements Iterable<MyAbstractObjective> {

    private MyAbstractObjective[] arrayList;
    private int currentSize;

    public ObjectiveIterator(MyAbstractObjective[] newArray) {
        this.arrayList = newArray;
        this.currentSize = arrayList.length;
    }

    @Override
    public Iterator<MyAbstractObjective> iterator() {
        Iterator<MyAbstractObjective> it = new Iterator<MyAbstractObjective>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize && arrayList[currentIndex] != null;
            }

            @Override
            public MyAbstractObjective next() {
                return arrayList[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            public void reset(){
                currentIndex = 0;
            }
        };
        return it;
    }
}