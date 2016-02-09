import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Lab2b {
        public static double[] simplifyShape(double[] poly, int k) {

            DLList list = new DLList();
            Comparator<DLList.Node> comparator = new NodeComparator();
            PriorityQueue<DLList.Node> queue = new PriorityQueue<>(poly.length / 2 - 2, comparator);
            
            //Makes the first two nodes specifically as they are needed in the list, but not in the queue
            double[] firstArray = {0, poly[0], poly[1], 0};
            DLList.Node firstNode = list.addFirst(firstArray);
            double[] lastArray = {0, poly[poly.length-2], poly[poly.length-1], poly.length/2-1};
            list.addLast(lastArray);
            DLList.Node prevListNode = firstNode;

            for (int i = 1; i < poly.length / 2 - 1; i++) { //Skips calculating for end points
                double[] nodeValues = {poly[(i-1)*2], poly[(i-1)*2 + 1], poly[i*2], poly[i*2 + 1],
                        poly[(i+1)*2], poly[(i+1)*2 + 1]}; //To reduce the amount of calls for indexes in Poly
                double valMeas = Math.hypot( nodeValues[0] - nodeValues[2], nodeValues[1] - nodeValues[3]);
                valMeas += Math.hypot( nodeValues[2] -  nodeValues[4],  nodeValues[3] -  nodeValues[5]);
                valMeas -= Math.hypot( nodeValues[0] -  nodeValues[4],  nodeValues[1] -  nodeValues[5]);
                double[] nodeArray = {valMeas, poly[2*i], poly[2*i + 1], i};
                prevListNode = list.insertAfter(nodeArray, prevListNode);
                queue.add(prevListNode);
            }

            while (queue.size() > k - 2) { //k-2 because we don't want to account for end points
                DLList.Node nodeToRemove = queue.poll(); //Takes the least valuable node out of the queue
                list.remove(nodeToRemove); // removes least valuable node from list
                DLList.Node[] nodesToRecalc = {nodeToRemove.getNext(), nodeToRemove.getPrev()};
                for( int i = 0; i < 2; i++){ //Only to get rid of some code and to be able to use an array
                    if((nodesToRecalc[i] != list.getFirst() && nodesToRecalc[i] != list.getLast())  ) {
                        queue.remove(nodesToRecalc[i]);
                        double[] node = (double[]) nodesToRecalc[i].elt;
                        double[] prevNodeElt = (double[]) nodesToRecalc[i].getPrev().elt;
                        double[] nextNodeElt = (double[]) nodesToRecalc[i].getNext().elt;
                        double valMeas = Math.hypot((prevNodeElt[1] - node[1]), prevNodeElt[2] - node[2]);
                        valMeas += Math.hypot((node[1] - nextNodeElt[1]), node[2] - nextNodeElt[2]);
                        valMeas -= Math.hypot((prevNodeElt[1] - nextNodeElt[1]), prevNodeElt[2] - nextNodeElt[2]);
                        ((double[]) nodesToRecalc[i].elt)[0] = valMeas;
                        queue.add(nodesToRecalc[i]);
                    }
                }
            }
            //New queue to reorder the nodes after index instead of value
            Comparator<DLList.Node> indexComparator = new IndexComparator();
            PriorityQueue<DLList.Node> indexQueue = new PriorityQueue<>(k - 2, indexComparator);
            Iterator<DLList.Node> iter = queue.iterator();
            while (iter.hasNext()) { //adds the contents of the first queue to the second
                indexQueue.add(iter.next());
            }
            double[] resultPoly = new double[k * 2];

            //Adds the end points to the array to be returned
            resultPoly[0] = poly[0];
            resultPoly[1] = poly[1];
            resultPoly[resultPoly.length - 2] = poly[poly.length - 2];
            resultPoly[resultPoly.length - 1] = poly[poly.length - 1];

            for (int i = 1; i < k-1; i++) { //Adds all the nodes in indexqueue to resultpoly
                DLList.Node current = indexQueue.poll();
                resultPoly[i*2] = ((double[]) current.elt)[1];
                resultPoly[i*2 + 1] = ((double[]) current.elt)[2];
            }
            return resultPoly;
        }

    /**
     * Comparator for sorting a priorityqueue after the value found in the double[]
     * contained in each node, always found at index 0
     */
    private static class NodeComparator implements Comparator<DLList.Node> {

            @Override
            public int compare(DLList.Node o1, DLList.Node o2) {
                double[] o1list = (double[]) o1.elt;
                double[] o2list = (double[]) o2.elt;
                if (o1list[0] > o2list[0]) {
                    return 1;
                } else if (o1list[0] < o2list[0]) {
                    return -1;
                }
                return -1; //This is so the algorithm works the same as the previous one for fig1.txt
            }
        }

        /**
         * Comparator for sorting a priorityqueue after the index found in the double[]
         * contained in each node, always found at index 3
         */
        private static class IndexComparator implements Comparator<DLList.Node> {

            @Override
            public int compare(DLList.Node o1, DLList.Node o2) {
                double[] o1list = (double[]) o1.elt;
                double[] o2list = (double[]) o2.elt;

                return (int)(o1list[3] - o2list[3]);
            }
        }
}