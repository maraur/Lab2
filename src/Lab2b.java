import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Lab2b {
    public static double[] simplifyShape(double[] poly, int k) {
        DLList list = new DLList();
        Comparator<DLList.Node> comparator = new NodeComparator();
        PriorityQueue<DLList.Node> queue = new PriorityQueue<>(poly.length / 2 - 2, comparator);
        double[] firstArray = {100000, poly[0], poly[1], 0};
        DLList.Node firstNode = list.addFirst(firstArray);
        double[] lastArray = {100000, poly[poly.length-2], poly[poly.length-1], poly.length/2-1};
        list.addLast(lastArray);
        DLList.Node prevListNode = firstNode;
        for (int i = 1; i < poly.length / 2 - 1; i++) { //skippa första och sista noden
            double valMeas = Math.hypot((poly[(i - 1) * 2] - poly[i * 2]), poly[(i - 1) * 2 + 1] - poly[i * 2 + 1]);
            valMeas += Math.hypot((poly[i * 2] - poly[(i + 1) * 2]), poly[(i * 2) + 1] - poly[(i + 1) * 2 + 1]);
            valMeas -= Math.hypot((poly[(i - 1) * 2] - poly[(i + 1) * 2]), poly[((i - 1) * 2) + 1] - poly[(i + 1) * 2 + 1]);
            double[] nodeArray = {valMeas, poly[2*i], poly[2*i + 1], i};
            prevListNode = list.insertAfter(nodeArray, prevListNode);
            queue.add(prevListNode);
            System.out.println(poly[i * 2] + ", "+ poly[(i * 2) + 1] ); //todo remove
        }

        while (queue.size() > k - 2) { //k-2 eftersom vi kan lägga till start och slut precis innan vi returnerar
            DLList.Node nodeToRemove = queue.poll(); //Tar ut noden ur priorityqueue
            list.remove(nodeToRemove); // removes least valueable Node
            DLList.Node nextNode = nodeToRemove.getNext();
            DLList.Node prevNode = nodeToRemove.getPrev();
            DLList.Node[] nodesToRecalc = {nodeToRemove.getNext(), nodeToRemove.getPrev()};
            queue.remove(nextNode);
            queue.remove(prevNode);
            for( int i = 0; i < 2; i++){
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

        Comparator<DLList.Node> indexComparator = new IndexComparator();
        PriorityQueue<DLList.Node> indexQueue = new PriorityQueue<>(k - 2, indexComparator);
        Iterator<DLList.Node> iter = queue.iterator();
        System.out.println(queue.size());
        while (iter.hasNext()) {
            indexQueue.add(iter.next());
        }
        System.out.println(indexQueue.size()); //todo remove
        double[] resultPoly = new double[k * 2];

        resultPoly[0] = poly[0];
        resultPoly[1] = poly[1];
        resultPoly[resultPoly.length - 2] = poly[poly.length - 2];
        resultPoly[resultPoly.length - 1] = poly[poly.length - 1];

        for (int i = 1; i < k-1; i++) { //enklare för indexering
            DLList.Node current = indexQueue.poll();
            resultPoly[i*2] = ((double[]) current.elt)[1];
            resultPoly[i*2 + 1] = ((double[]) current.elt)[2];
            System.out.println(((double[])current.elt)[3] +" index" ); //todo remove
        }
        System.out.println(resultPoly.length); //todo remove
        return resultPoly;
    }

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
            return 0;
        }
    }

    private static class IndexComparator implements Comparator<DLList.Node> {

        @Override
        public int compare(DLList.Node o1, DLList.Node o2) {
            double[] o1list = (double[]) o1.elt;
            double[] o2list = (double[]) o2.elt;

            return (int)(o1list[3] - o2list[3]);
        }
    }
}